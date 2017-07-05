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

    @Override
    public void connect() {
        try {
            this.connect = DriverManager.getConnection("jdbc:mariadb://120.105.161.89/TCMSystem", "TCMSystem", "TCMSystem");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean create(Staff obj) {
        return false;
    }

    @Override
    public boolean delete(Staff obj) {
        return false;
    }

    @Override
    public boolean update(Staff obj) {
        return false;
    }

    @Override
    public boolean find(Staff staff) {
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
