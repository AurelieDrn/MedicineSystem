package com.example.lab708.tcmsystem.dao;

import android.util.Log;

import com.example.lab708.tcmsystem.model.NewRequirement;
import com.example.lab708.tcmsystem.model.Requirement;
import com.example.lab708.tcmsystem.model.Medicine;

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

    public ArrayList<Requirement> getRequirements() throws SQLException {
        ArrayList<Requirement> requirementList = new ArrayList<>();
        List<String> pickupNumList = new ArrayList<>();

        // Get pickup numbers
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pick_id` FROM `Pickup`");
        while(result.next()) {
            pickupNumList.add(result.getString("pick_id"));
        }

        for(String pic_num : pickupNumList) {
            Requirement requirement = new Requirement();
            requirement.setNumber(pic_num);

            // Get pickup status and progress
            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT `pick_emergency`, `pick_progress` FROM `Pickup` WHERE `pick_id` = \'"+pic_num+"\'");

            while(result2.next()) {
                requirement.setEmergency(Integer.valueOf(result2.getString("pick_emergency")));
                requirement.setProgress(Integer.valueOf(result2.getString("pick_progress")));
            }

            List<String> medNumList = new ArrayList<>();
            // Get pickup med numbers
            ResultSet result3 = this.connect.createStatement().executeQuery("SELECT `med_id` FROM `PickupMed` WHERE `pick_id` = \'"+pic_num+"\'");
            while(result3.next()) {
                medNumList.add(result3.getString("med_id"));
            }

            // Set medicine numbers
            requirement.setMedicineNumbers(medNumList);

            for(String medNum : medNumList) {
                Medicine medicine = new Medicine();
                // Get medicine
                ResultSet result4 = this.connect.createStatement().executeQuery("SELECT * FROM `Medicine` WHERE `med_id` = \'"+medNum+"\'");
                while(result4.next()) {
                    medicine.setSerialNumber(result4.getString("med_id"));
                    medicine.setName(result4.getString("med_name"));
                    medicine.setIngredients(result4.getString("med_ingr"));
                    medicine.setBrand(result4.getString("med_brand"));
                    medicine.setFirmNumber(result4.getString("firm_id"));
                    medicine.setExperienceQuantity(result4.getString("med_quantity"));
                    requirement.addMedicine(medicine);
                }
            }
            requirementList.add(requirement);
        }
        return requirementList;
    }

    public void createNewRequirements(List<NewRequirement> requirements, int status) throws SQLException {
        this.connect.createStatement().executeQuery("INSERT INTO `Pickup`(`pick_emergency`, `pick_progress`) VALUES ("+status+",0)");
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pick_id` FROM `Pickup` ORDER BY `pick_id` DESC;");
        int pickupNumber = -1;
        if(result.next()) {
            pickupNumber = result.getInt(1);
        }

        for(NewRequirement nr : requirements) {
            String medNum = nr.getMedicineNumber();
            int quantity = nr.getQuantity();
            Log.d("RequirementDAO quantity", String.valueOf(quantity));
            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT * FROM `PickupMed` WHERE `pick_id` = "+pickupNumber+" AND `med_id` = "+medNum);
            if(result2.next()) {
                //String pickup_mednum = result2.getString("med_id");
                //int pickup_quantity = result2.getInt("pickup_quantity");
                //int q = quantity + pickup_quantity;
                //this.connect.createStatement().executeQuery("UPDATE `PickupMed` SET `pickup_quantity`="+q+" WHERE `med_id` = "+pickup_mednum+" AND `pick_id` = "+pickupNumber);
            }
            else {
                this.connect.createStatement().executeQuery("INSERT INTO `PickupMed`(`med_id`, `pick_id`, `pickmed_quantity`) VALUES ('"+medNum+"',"+pickupNumber+", "+quantity+")");
            }
        }
    }

    public void deleteRequirement(int id) throws SQLException {
        this.connect.createStatement().executeQuery("DELETE FROM `Pickup` WHERE `pick_id` = "+id);
        this.connect.createStatement().executeQuery("DELETE FROM `PickupMed` WHERE `pick_id` = "+id);
    }

    // check if the pickup requirement cannot be fulfilled
    public boolean medOutOfStock(int requirementId) throws SQLException {
        boolean outOfStock = false;
        int quantity = 0;
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `med_id` FROM `PickupMed` WHERE `pick_id` = "+requirementId);
        while(result.next()) {
            String medNum = result.getString("med_id");

            //int quantity = result.getInt("pickup_quantity");
            ResultSet result1 = this.connect.createStatement().executeQuery("SELECT `med_quantity` FROM `Medicine` WHERE `med_id` = "+medNum);
            while(result1.next()) {
                quantity = result1.getInt("med_quantity");
                //Log.d("RequirementDAO", String.valueOf(quantity));
            }
            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT `pile_quantity` FROM `Pile` WHERE `med_id` = "+medNum);
            int quantityInDatabase = 0;
            while(result2.next()) {
                quantityInDatabase += result2.getInt("pile_quantity");
                //Log.d("quant in db", String.valueOf(quantityInDatabase));
            }
            if(quantityInDatabase < quantity) {
                //Log.d("out of stock", String.valueOf(quantityInDatabase<quantity));
                outOfStock = true;
            }
        }
        return outOfStock;
    }

    public int getQuantity(int requirementId, String medId) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pickmed_quantity` FROM `pickupmed` WHERE `pick_id` = "+requirementId+" AND `med_id` = "+medId);
        int res = 0;
        while(result.next()) {
            res = result.getInt("pickmed_quantity");
        }
        Log.d("res", String.valueOf(res));
        return res;
    }
}
