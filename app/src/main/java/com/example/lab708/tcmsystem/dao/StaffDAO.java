package com.example.lab708.tcmsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class StaffDAO extends DAO<Staff>{

    public StaffDAO(Connection conn) {
        super(conn);
    }

    public boolean check(Staff staff) {
        ResultSet result = null;
        try {
            result = this.connect.createStatement().executeQuery("SELECT * FROM Staff WHERE sta_acc = '"+staff.getAccount()+"' AND sta_pas = '"+staff.getPassword()+"'");
            if(result.first()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
