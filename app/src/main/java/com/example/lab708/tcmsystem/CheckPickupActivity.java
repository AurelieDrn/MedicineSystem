package com.example.lab708.tcmsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lab708.tcmsystem.adaptater.Requirement;
import com.example.lab708.tcmsystem.dao.DAO;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.RequirementDAO;

import java.sql.SQLException;

public class CheckPickupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pickup);

        RequirementDAO requirementDAO = DAOFactory.getRequirementDAO();
        try {
            requirementDAO.getRequirements();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
