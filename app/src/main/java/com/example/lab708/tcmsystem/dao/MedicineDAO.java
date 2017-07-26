package com.example.lab708.tcmsystem.dao;

import com.example.lab708.tcmsystem.model.Medicine;

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

    public boolean find(String code) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("SELECT * FROM Medicine WHERE med_num = \'"+code+"\'");
        if(result.first()) {
            return true;
        }
        else {
            return false;
        }
    }

    public Medicine select(String code) throws SQLException {
        Medicine med = new Medicine();
        ResultSet result = this.connect.createStatement().executeQuery("SELECT * FROM Medicine WHERE med_num = '"+code+"'");
        while(result.next()) {
            med.setName(result.getString("med_name"));
            med.setSerialNumber(result.getString("med_num"));
            med.setExperienceQuantity(result.getString("med_expquan"));
            med.setBrand(result.getString("med_bra"));
            med.setFirmNumber(result.getString("med_firnum"));
            med.setIngredients(result.getString("med_con"));
        }
        return med;
    }

    public int getExperienceQuantity(String medName) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `med_expquan` FROM `Medicine` WHERE `med_name` = '"+medName+"'");
        while(result.next()) {
            return result.getInt("med_expquan");
        }
        return -1;
    }

}
