package com.example.lab708.tcmsystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.classe.RequirementDetail;
import com.example.lab708.tcmsystem.adapter.RequirementDetailsAdapter;

import java.util.ArrayList;

public class CheckPickupDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pickup_details);

        Bundle data = getIntent().getExtras();
        ArrayList<RequirementDetail> reqDetailList = (ArrayList) data.getParcelableArrayList("reqDetailList");
        //Log.d("REQ", reqDetailList.toString());
        // Create the adapter to convert the array to views

        RequirementDetailsAdapter adapter = new RequirementDetailsAdapter(this, reqDetailList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.check_pickup_details_lv);
        listView.setAdapter(adapter);
    }
}
