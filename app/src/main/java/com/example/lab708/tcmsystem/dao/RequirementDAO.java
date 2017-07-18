package com.example.lab708.tcmsystem.dao;

import android.util.Log;

import com.example.lab708.tcmsystem.classe.NewRequirement;
import com.example.lab708.tcmsystem.classe.Requirement;
import com.example.lab708.tcmsystem.classe.Medicine;

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
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pic_num` FROM `Pickup`");
        while(result.next()) {
            pickupNumList.add(result.getString("pic_num"));
        }

        for(String pic_num : pickupNumList) {
            Requirement requirement = new Requirement();
            requirement.setNumber(pic_num);

            // Get pickup status and progress
            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT `pic_sta`, `pic_pro` FROM `Pickup` WHERE `pic_num` = \'"+pic_num+"\'");

            while(result2.next()) {
                requirement.setEmergency(Integer.valueOf(result2.getString("pic_sta")));
                requirement.setProgress(Integer.valueOf(result2.getString("pic_pro")));
            }

            List<String> medNumList = new ArrayList<>();
            // Get pickup med numbers
            ResultSet result3 = this.connect.createStatement().executeQuery("SELECT `pickup_mednum` FROM `PickupMed` WHERE `pic_num` = \'"+pic_num+"\'");
            while(result3.next()) {
                medNumList.add(result3.getString("pickup_mednum"));
            }

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
            requirementList.add(requirement);
        }
        return requirementList;
    }

    public void createNewRequirements(List<NewRequirement> requirements, int status) throws SQLException {
        this.connect.createStatement().executeQuery("INSERT INTO `Pickup`(`pic_sta`, `pic_pro`) VALUES ("+status+",0)");
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pic_num` FROM `Pickup` ORDER BY `pic_num` DESC;");
        int pickupNumber = -1;
        if(result.next()) {
            pickupNumber = result.getInt(1);
        }

        for(NewRequirement nr : requirements) {
            String medNum = nr.getMedicineNumber();
            int quantity = nr.getQuantity();

            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT * FROM `PickupMed` WHERE `pickup_mednum` = "+pickupNumber);
            if(result2.next()) {
                String pickup_mednum = result2.getString("pickupickup_mednump_mednum");
                int pickup_quantity = result2.getInt("pickup_quantity");
                int q = quantity + pickup_quantity;
                this.connect.createStatement().executeQuery("UPDATE `PickupMed` SET `pickup_quantity`="+q+" WHERE `pickup_mednum` = "+pickup_mednum+" AND `pic_num` = "+pickupNumber);
            }
            else {
                this.connect.createStatement().executeQuery("INSERT INTO `PickupMed`(`pickup_mednum`, `pic_num`, `pickup_quantity`) VALUES ("+medNum+","+pickupNumber+","+quantity+")");
            }
        }
    }

    public void deleteRequirement(int id) throws SQLException {
        this.connect.createStatement().executeQuery("DELETE FROM `Pickup` WHERE `pic_num` = "+id);
        this.connect.createStatement().executeQuery("DELETE FROM `PickupMed` WHERE `pic_num` = "+id);
    }

    // check if the pickup requirement cannot be fulfilled
    public boolean medOutOfStock(int requirementId) throws SQLException {
        boolean outOfStock = false;
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pickup_mednum`, `pickup_quantity` FROM `PickupMed` WHERE `pic_num` = "+requirementId);
        while(result.next()) {
            String medNum = result.getString("pickup_mednum");
            int quantity = result.getInt("pickup_quantity");
            ResultSet result2 = this.connect.createStatement().executeQuery("SELECT `pil_quan` FROM `Pile` WHERE `pile_mednum` = "+medNum);
            int quantityInDatabase = 0;
            while(result2.next()) {
                quantityInDatabase = result2.getInt("pil_quan");
            }
            if(quantityInDatabase < quantity) {
                outOfStock = true;
            }
        }
        return outOfStock;
    }
}
