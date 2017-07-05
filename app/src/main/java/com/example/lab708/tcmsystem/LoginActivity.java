package com.example.lab708.tcmsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab708.tcmsystem.dao.DAO;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.Staff;
import com.example.lab708.tcmsystem.dao.StaffDAO;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // to fix ClassLoader referenced unknown path
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final EditText accountName = (EditText) findViewById(R.id.account_et);
        final EditText password = (EditText) findViewById(R.id.password_et);

        final Button logIn = (Button) findViewById(R.id.log_in);

        logIn.setEnabled(false);


        // log in listener
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin(accountName.getText().toString(), password.getText().toString())) {
                    Intent homeActivityIntent = new Intent();
                    homeActivityIntent.setClass(LoginActivity.this, HomeActivity.class);
                    homeActivityIntent.putExtra("account", accountName.getText().toString());
                    startActivity(homeActivityIntent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "登入失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // disable or enable log in button
        accountName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(noLoginPasswordInput(accountName, password)) {
                    logIn.setEnabled(false);
                }
                else {
                    logIn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // disable or enable log in button
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(noLoginPasswordInput(accountName, password)) {
                    logIn.setEnabled(false);
                }
                else {
                    logIn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // check if the user did not enter his password or login
    private boolean noLoginPasswordInput(EditText account, EditText password) {
        return (account.getText().toString().matches("") || password.getText().toString().matches(""));
    }

    // function to check if the account name and password are correct
    private boolean checkLogin(String account, String password) {
        DAO<Staff> staffDAO = DAOFactory.getStaffDAO();
        if(staffDAO.find(new Staff(account, password))) {
            return true;
        }
        return false;
    }

}


