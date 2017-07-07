package com.example.lab708.tcmsystem.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class MedicineDAO extends DAO<Medicine>{

    public MedicineDAO(Connection conn) {
        super(conn);
    }

    public boolean find(String code) {
        ResultSet result = null;
        try {
            result = this.connect.createStatement().executeQuery("SELECT * FROM Medicine WHERE med_num = \'"+code+"\'");
            if(result.first()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Medicine select(String code) {
        Medicine med = new Medicine();
        ResultSet result = null;
        try {
            result = this.connect.createStatement().executeQuery("SELECT * FROM Medicine WHERE med_num = '"+code+"'");
            while(result.next()) {
                med.setName(result.getString("med_name"));
                med.setSerialNumber(result.getString("med_num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return med;
    }

}
