package com.example.lab708.tcmsystem.dao;

import android.util.Log;

import com.example.lab708.tcmsystem.adaptater.Requirement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aurelie on 07/07/2017.
 */

public class RequirementDAO extends DAO<Requirement>{

    public RequirementDAO(Connection conn) {
        super(conn);
    }

    public List<Requirement> getRequirements() throws SQLException {
        List<String> pickupNumList = new ArrayList<>();

        // Get pickup numbers
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pic_num` FROM `Pickup`");
        while(result.next()) {
            pickupNumList.add(result.getString("pic_num"));
            Log.d("MONTAG", result.getString("pic_num"));
        }

        Log.d("pickupNumList size", String.valueOf(pickupNumList.size()));
        Requirement requirement = new Requirement();
        for(String pic_num : pickupNumList) {
            requirement.setNumber(pic_num);

            // Get pickup status and progress
            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT `pic_sta`, `pic_pro` FROM `Pickup` WHERE `pic_num` = "+pic_num);

            while(result2.next()) {
                requirement.setEmergency(Integer.valueOf(result2.getString("pic_sta")));
                requirement.setProgress(Integer.valueOf(result2.getString("pic_pro")));
            }

            List<String> medNumList = new ArrayList<>();
            // Get pickup med numbers
            ResultSet result3 = this.connect.createStatement().executeQuery("SELECT `pickup_mednum` FROM `PickupMed` WHERE `pic_num` = "+pic_num);
            while(result3.next()) {
                medNumList.add(result3.getString("pickup_mednum"));
            }
            Log.d("MONTAG2", medNumList.toString());

            // Set medicine numbers
            requirement.setMedicineNumbers(medNumList);

            for(String medNum : medNumList) {
                Medicine medicine = new Medicine();
                // Get medicine
                ResultSet result4 = this.connect.createStatement().executeQuery("SELECT * FROM `Medicine` WHERE `med_num` = \'"+medNum+"\'");
                while(result4.next()) {
                    medicine.setSerialNumber(result4.getString("med_num"));
                    medicine.setName(result4.getString("med_name"));
                    medicine.setIngredients(result4.getString("med_con"));
                    medicine.setBrand(result4.getString("med_bra"));
                    medicine.setFirmNumber(result4.getString("med_firnum"));
                    medicine.setExperienceQuantity(result4.getString("med_expquan"));
                    requirement.addMedicine(medicine);
                }
            }
            Log.d("MONTAG3", requirement.toString());
        }

        return null;
    }
}
