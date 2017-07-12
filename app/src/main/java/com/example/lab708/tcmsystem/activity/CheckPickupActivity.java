package com.example.lab708.tcmsystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.classe.Requirement;
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

        // Create the adapter to convert the array to views
        RequirementsAdapter adapter = new RequirementsAdapter(this, requirementList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.check_pickup_lv);
        listView.setAdapter(adapter);
    }
}
