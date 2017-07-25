package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lab708.tcmsystem.CameraPreview;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.model.NewRequirement;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.MedicineDAO;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    private ArrayList<NewRequirement> newReqMedNum;

    // String staffacc;
    private String nextFunction;
    private Intent intent;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // 設定螢幕為垂直畫面 Set the screen to a vertical screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //取得相機 Get the camera
        autoFocusHandler = new Handler();
        //mCamera = getCameraInstance();
        newOpenCamera();

        //這三行就是library幫你坐的東西 These three lines is the library to help you do things
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        // new 一個自定義的class New a custom class
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);

        intent = this.getIntent();

        // Get bundle
        Bundle bcode = this.getIntent().getExtras();
        nextFunction = bcode.getString("toFunction");
        // staffacc = bcode.getString("staffacc");
        if(nextFunction.equals("NewRequirementActivity")) {
            newReqMedNum = ((ArrayList<NewRequirement>) bcode.getSerializable("newRequirementList"));
        }

        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        //設定畫面 Set the screen
        if (barcodeScanned) {
            barcodeScanned = false;
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
    }

    //在進入待機的時候運行的函式 In the standby time to run the function
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        releaseCamera();
    }

    // 放掉使用相機的記憶體 Remove the memory using the camera
    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    // 取得相機使用權
    public static Camera getCameraInstance() {
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void oldOpenCamera() {
        try {
            mCamera = Camera.open();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() { //掃到條碼的callback//scan the barcode to the callback
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                String str = null;
                for (Symbol sym : syms) {
                    try {
                        str = new String(sym.getData().getBytes("sjis"),"UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if(str.equals("")){
                        // Toast Please re-scan
                        Toast toast = Toast.makeText(ScanActivity.this, "請重新掃描!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else {
                        // Intent to ExecuteShelvesActivity or NewExtraPickup or NewRequire
                        Intent intent = new Intent();

                        Bundle bcode = new Bundle();
                        bcode.putString("Code_Num", str);
                        //bcode.putString("staffacc",staffacc);

                        if(nextFunction.equals("ExecuteShelvesActivity")) {
                            intent.setClass(ScanActivity.this, ExecuteShelvesActivity.class);
                        }
                        else if(nextFunction.equals("NewRequirementActivity")) {
                            intent.setClass(ScanActivity.this, NewRequirementActivity.class);
                            bcode.putSerializable("newRequirementList", newReqMedNum);
                        }
                        intent.putExtras(bcode);
                        startActivity(intent);
                        ScanActivity.this.finish() ;
                        barcodeScanned = true;
                    }
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    private void newOpenCamera() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera();
        }
    }
    private CameraHandlerThread mThread = null;
    private class CameraHandlerThread extends HandlerThread {
        Handler mHandler = null;

        CameraHandlerThread() {
            super("CameraHandlerThread");
            start();
            mHandler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    oldOpenCamera();
                    notifyCameraOpened();
                }
            });
            try {
                wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScanActivity.this, HomeActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean checkCodeInDatabase(String code) {
        MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();
        boolean check = true;
        try {
            if(medicineDAO.find(code)) {
                check = true;
            }
            else {
                check = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

}

