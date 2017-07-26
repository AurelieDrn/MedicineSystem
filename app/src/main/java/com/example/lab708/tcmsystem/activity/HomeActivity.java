package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.lab708.tcmsystem.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //final String account = getIntent().getExtras().getString("account");
        Button shelves = (Button) findViewById(R.id.shelves_btn);
        Button checkPickup = (Button) findViewById(R.id.check_pickup_btn);
        Button newRequirement = (Button) findViewById(R.id.new_requirement);

        shelves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanActivity = new Intent();
                scanActivity.setClass(HomeActivity.this, ScanActivity.class);
                // scanActivity.putExtra("account", account);
                scanActivity.putExtra("toFunction", "ExecuteShelvesActivity");
                startActivity(scanActivity);
            }
        });

        checkPickup.setOnClickListener(new View.OnClickListener() {//If you choose to view the requirements
            @Override
            public void onClick(View v) {
              // TODO Auto-generated method stub
              Intent checkReq = new Intent() ;
              checkReq.setClass(HomeActivity.this, CheckPickupActivity.class);
              //Bundle bacc = new Bundle();
              // bacc.putString("staffacc", acc);
              //checkReq.putExtras(bacc);
              startActivity(checkReq);
            }
        });

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

    @Override
    public void onBackPressed() {

    }
}
