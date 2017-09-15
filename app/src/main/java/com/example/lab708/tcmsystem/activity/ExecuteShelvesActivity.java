package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab708.tcmsystem.CustomDialog;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.MedicineDAO;
import com.example.lab708.tcmsystem.dao.PileDAO;
import com.example.lab708.tcmsystem.model.Medicine;
import com.example.lab708.tcmsystem.model.Pile;

import java.sql.SQLException;

public class ExecuteShelvesActivity extends AppCompatActivity{

    private TextView serialNumber_tv, medName_tv, shelfLocation_tv;
    private EditText quantity_et, layer_et, pile_et;
    private Button submit_btn, add_btn;
    private DatePicker expDate_dp;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_shelves);

        // to fix ClassLoader referenced unknown path
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        serialNumber_tv = (TextView) findViewById(R.id.execute_shelves_med_number) ;
        medName_tv = (TextView) findViewById(R.id.execute_shelve_med_name) ;
        shelfLocation_tv = (TextView) findViewById(R.id.execute_shelves_shelfloc);
        expDate_dp = (DatePicker) findViewById(R.id.execute_shelves_date);
        quantity_et = (EditText) findViewById(R.id.execute_shelves_quantity) ;
        layer_et = (EditText) findViewById(R.id.execute_shelves_layerloc);
        pile_et = (EditText) findViewById(R.id.execute_shelves_pileloc);
        submit_btn = (Button) findViewById(R.id.execute_shelves_submit) ;
        add_btn = (Button) findViewById(R.id.execute_shelves_add) ;

        add_btn.setVisibility(View.INVISIBLE);

        // Get scanned code
        Bundle bcode = this.getIntent().getExtras();
        code = bcode.getString("Code_Num");

       showInformation();

        // Submit
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(missingFields()) {
                    // Some fields are empty
                    Toast.makeText(ExecuteShelvesActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
                }
                else {
                    int year = expDate_dp.getYear();
                    int month = expDate_dp.getMonth()+1;
                    int day = expDate_dp.getDayOfMonth();

                    String date = checkDigit(year) + "-" + checkDigit(month) + "-" + checkDigit(day);

                    int quantity = Integer.valueOf(String.valueOf(quantity_et.getText()));

                    String layer = layer_et.getText().toString();
                    String pile = pile_et.getText().toString();

                    insertInDatabase(layer, pile, quantity, date);
                }
            }
        });

    }

    /**
     * Change a single digit number to a two digits number.
     * For example, if we have the date 4/4/2016, we want to have 04/04/2016.
     * This function takes one number and adds a '0' if it is less than 9.
     * @param number
     * @return the number with a '0' in front of it if the number is less than 9, else returns the number
     */
    public String checkDigit(int number) {
        return number <=9 ? "0" + number : String.valueOf(number);
    }

    /**
     * Display the medicine information.
     */
    private void showInformation() {
        MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();

        try {
            // The medicine exists in the database.
            if(medicineDAO.find(this.code)) {
                Medicine m = medicineDAO.select(this.code);
                serialNumber_tv.setText(m.getSerialNumber());
                medName_tv.setText(m.getName());
                shelfLocation_tv.setText(m.getShelfNumber()+"");
            }
            else {
                // The medicine cannot be found in the database.
                Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
                intent.putExtra("toFunction", "ExecuteShelvesActivity");
                CustomDialog.showErrorMessage(ExecuteShelvesActivity.this, "Error", "查無此藥品，請至資料庫新增! "+code+" not in database", intent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Check if the user has not filled all the fields.
     * @return true if the user forgot to fill in a field, else false
     */
    private boolean missingFields() {
        if(quantity_et.getText().toString().matches("")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Execute a request to insert the medicine data in the database
     * @param layer second part of the location of a medicine
     * @param pile third part of the location of a medicine
     * @param quantity quantity of medicine we put in shelves
     * @param date expiration date of the medicine
     */
    private void insertInDatabase(String layer, String pile, int quantity, String date) {
        Pile p = new Pile(layer, pile, code, quantity, date);
        PileDAO pileDAO = DAOFactory.getPileDAO();
        try {
            pileDAO.create(p);
        } catch (SQLException e) {
            showErrorDialog();
            e.printStackTrace();
        }

        // Alert dialog success
        showSuccessDialog();
    }

    /**
     * Show error dialog when the error occurred when inserting in the database
     */
    private void showErrorDialog() {
        Intent intent = new Intent(ExecuteShelvesActivity.this, HomeActivity.class);
        CustomDialog.showErrorDialogOneOption(ExecuteShelvesActivity.this, "Error!", "Error inserting in database", intent, R.string.back_home);
    }

    /**
     * Show alert dialog after success with 'back to home' or 'keep scanning' options
     */
    private void showSuccessDialog() {
        Intent intent1 = new Intent(ExecuteShelvesActivity.this, HomeActivity.class);
        Intent intent2 = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
        intent2.putExtra("toFunction", "ExecuteShelvesActivity");
        CustomDialog.showSuccessDialogTwoOptions(ExecuteShelvesActivity.this, "Success!", "上架成功!", intent2, intent1, R.string.keep_scanning, R.string.back_home);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
        intent.putExtra("toFunction", "ExecuteShelvesActivity");
        startActivity(intent);
    }
}