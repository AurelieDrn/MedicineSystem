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
        ResultSet result = this.connect.createStatement().executeQuery("SELECT * FROM Medicine WHERE med_id = \'"+code+"\'");
        if(result.first()) {
            return true;
        }
        else {
            return false;
        }
    }

    public Medicine select(String code) throws SQLException {
        Medicine med = new Medicine();
        ResultSet result = this.connect.createStatement().executeQuery("SELECT * FROM Medicine WHERE med_id = '"+code+"'");
        while(result.next()) {
            med.setName(result.getString("med_name"));
            med.setSerialNumber(result.getString("med_id"));
            med.setExperienceQuantity(result.getString("med_quantity"));
            med.setBrand(result.getString("med_brand"));
            med.setFirmNumber(result.getString("firm_id"));
            med.setIngredients(result.getString("med_ingr"));
            med.setShelfNumber(result.getInt("med_shelf"));
        }
        return med;
    }

    public int getExperienceQuantity(String medName) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `med_quantity` FROM `Medicine` WHERE `med_name` = '"+medName+"'");
        while(result.next()) {
            return result.getInt("med_quantity");
        }
        return -1;
    }

}
