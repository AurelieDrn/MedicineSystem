package com.example.lab708.tcmsystem.dao;

/**
 * Created by Aurelie on 06/07/2017.
 */

import android.util.Log;

import com.example.lab708.tcmsystem.model.Pickup;
import com.example.lab708.tcmsystem.model.QuantityLocation;
import com.example.lab708.tcmsystem.model.Pile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PileDAO extends DAO<Pile> {

    public PileDAO(Connection conn) {
        super(conn);
    }

    public void create(Pile p) throws SQLException {

        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pile_id`, `pile_quantity` FROM `Pile` WHERE `med_id` = "+p.getMedicineNumber()+" AND `pile_expdate` = \'"+p.getDate()+"\' AND `pile_layer`= "+p.getLayer()+" AND `pile_level`= "+p.getPile());

        if(result.first()) {
            int id = result.getInt("pile_id");
            int quant = result.getInt("pile_quantity")+p.getQuantity();
            this.connect.createStatement().executeQuery("UPDATE `Pile` SET `pile_quantity` = "+quant+" WHERE `pile_id` = "+id);
        }
        else {
            Log.d("ELSE", p.toString());
            this.connect.createStatement().executeQuery("INSERT INTO Pile VALUES (NULL, "+p.getMedicineNumber()+", "+p.getQuantity()+", "+p.getPile()+", "+p.getLayer()+", '"+p.getDate()+"', '"+"staff"+"')");
        }
    }

    public ArrayList<QuantityLocation> getQuantLocations(int quantity, String medNumber) throws SQLException {
        ArrayList<QuantityLocation> quantLocationList = new ArrayList<>();
        String dbLoc0 = "";
        ResultSet result0 = this.connect.createStatement().executeQuery("SELECT `med_shelf` FROM `medicine` WHERE `med_id` = "+medNumber);
        while(result0.next()) {
            dbLoc0 = result0.getString("med_shelf");
        }
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pile_quantity`, `pile_layer`, `pile_level` FROM `Pile` WHERE `med_id`="+medNumber+" ORDER BY `pile_expdate` ASC");

        int quant = quantity;
        while(result.next() && quant > 0) {
            int dbQuant = Integer.valueOf(result.getString("pile_quantity"));
            //String dbLoc = result.getString("pil_loc");
            String dbLoc1 = result.getString("pile_layer");
            String dbLoc2 = result.getString("pile_level");
            String dbLoc = dbLoc0+"-"+dbLoc1+"-"+dbLoc2;
            
            // there is enough medicines in one pile
            if(dbQuant >= quant) {
                quantLocationList.add(new QuantityLocation(dbLoc, quant));
                break;
            }
            else { // we need to look for more medicines on other shelves
                quantLocationList.add(new QuantityLocation(dbLoc, dbQuant));
                quant -= (dbQuant);
            }
        }
        return quantLocationList;
    }

    public int getTotalQuantity(String medNumber) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("SELECT SUM(`pile_quantity`) FROM `Pile` WHERE `med_id` = "+medNumber);
        if(result.next()) {
            return result.getInt(1);
        }
        return -1;
    }

    public void executePickUp(Pickup p, int quantity) throws SQLException {
        String[] parts = p.getLocation().split("-");
        String shelf = parts[0];
        String layer = parts[1];
        String level = parts[2];
        int myquantity = quantity;

        while(myquantity != 0) {
            ResultSet result = this.connect.createStatement().executeQuery("SELECT * FROM `pile` WHERE `med_id` = "+p.getSerialNumber()+" AND `pile_level` = "+level+" AND `pile_layer` = "+layer+" ORDER BY `pile_expdate` ASC");
            int pile_id = 0;
            int quant = 0;
            if(result.next()) {
                pile_id = result.getInt("pile_id");
                quant = result.getInt("pile_quantity");
            }
            if(myquantity >= quant) {
                this.connect.createStatement().executeQuery("DELETE FROM `pile` WHERE `pile_id` = "+pile_id);
                myquantity = myquantity - quant;
            }
            else {
                this.connect.createStatement().executeQuery("UPDATE `Pile` SET `pile_quantity` = `pile_quantity`-"+quantity+" WHERE `pile_id` = "+pile_id);
                myquantity = 0;
                break;
            }
            //this.connect.createStatement().executeQuery("DELETE FROM `Pile` WHERE `pile_quantity` <= 0");
        }
    }

    public boolean medicineOutOfStock(String medId) throws SQLException {
        boolean b = true;
        ResultSet result = this.connect.createStatement().executeQuery("SELECT * FROM `pile` WHERE `med_id` = "+medId);

        while(result.next()) {
            b = false;
        }
        return b;
    }
}
