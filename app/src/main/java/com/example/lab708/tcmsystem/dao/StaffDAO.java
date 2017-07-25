package com.example.lab708.tcmsystem.dao;

import com.example.lab708.tcmsystem.model.Staff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class StaffDAO extends DAO<Staff>{

    public StaffDAO(Connection conn) {
        super(conn);
    }

    public boolean check(Staff staff) throws SQLException {
        ResultSet result = null;

        result = this.connect.createStatement().executeQuery("SELECT * FROM Staff WHERE sta_acc = '"+staff.getAccount()+"' AND sta_pas = '"+staff.getPassword()+"'");
        if(result.first()) {
            return true;
        }

        return false;
    }

}
