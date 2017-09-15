package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.model.Requirement;
import com.example.lab708.tcmsystem.adapter.RequirementsAdapter;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.RequirementDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class CheckPickupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pickup);

        // Construct the data source
        ArrayList<Requirement> requirementList = new ArrayList<Requirement>();
        RequirementDAO requirementDAO = DAOFactory.getRequirementDAO();
        try {
            requirementList = requirementDAO.getRequirements();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // There is no requirement
        if(requirementList.isEmpty()) {
            RelativeLayout checkPickup = (RelativeLayout)findViewById(R.id.activity_check_pickup);
            TextView title = (TextView) checkPickup.findViewById(R.id.check_pickup_details_tv);
            TableLayout tableTitle = (TableLayout) checkPickup.findViewById(R.id.tab_title);

            checkPickup.removeView(title);
            checkPickup.removeView(tableTitle);

            TextView textView = (TextView) findViewById(R.id.activity_check_pickup_txt);
            textView.setVisibility(View.VISIBLE);

        }
        else {
            // Create the adapter to convert the array to views
            RequirementsAdapter adapter = new RequirementsAdapter(this, requirementList);
            Log.d("Check pickup require list", requirementList.toString());
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.check_pickup_lv);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckPickupActivity.this, HomeActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
