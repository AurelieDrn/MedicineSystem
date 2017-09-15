package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.lab708.tcmsystem.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // to fix ClassLoader referenced unknown path
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button shelves = (Button) findViewById(R.id.shelves_btn);
        Button checkPickup = (Button) findViewById(R.id.check_pickup_btn);
        Button newRequirement = (Button) findViewById(R.id.new_requirement);

        // Go to ExecuteShelvesActivity
        shelves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanActivity = new Intent();
                scanActivity.setClass(HomeActivity.this, ScanActivity.class);
                scanActivity.putExtra("toFunction", "ExecuteShelvesActivity");
                startActivity(scanActivity);
            }
        });

        // Go to CheckPickupActivity
        checkPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // TODO Auto-generated method stub
              Intent checkReq = new Intent() ;
              checkReq.setClass(HomeActivity.this, CheckPickupActivity.class);
              startActivity(checkReq);
            }
        });

        // Go to NewRequirementActivity
        newRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanActivity = new Intent();
                scanActivity.setClass(HomeActivity.this, ScanActivity.class);
                scanActivity.putExtra("toFunction", "NewRequirementActivity");
                startActivity(scanActivity);
            }
        });
    }
}
