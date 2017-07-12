package com.example.lab708.tcmsystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lab708.tcmsystem.classe.Pickup;
import com.example.lab708.tcmsystem.R;

import java.util.ArrayList;
import java.util.Collections;

public class ExecutePickupActivity extends AppCompatActivity {

    private TextView serialNumber_tv;
    private TextView medicineName_tv;
    private TextView medicineLocation_tv;
    private TextView quantityStock_tv;
    private EditText quantityToPick_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_pickup);

        Bundle data = getIntent().getExtras();
        ArrayList<Pickup> pickupList = (ArrayList) data.getParcelableArrayList("pickupList");

        serialNumber_tv = (TextView) findViewById(R.id.exe_pu_serial_number);
        medicineName_tv = (TextView) findViewById(R.id.exe_pu_med_name);
        medicineLocation_tv = (TextView) findViewById(R.id.exe_pu_med_location);
        quantityStock_tv = (TextView) findViewById(R.id.exe_pu_quantity_stock);
        quantityToPick_et = (EditText) findViewById(R.id.exe_pu_quant_pick);

        Collections.sort(pickupList);
        Log.d("pickupList", pickupList.toString());

    }
}
