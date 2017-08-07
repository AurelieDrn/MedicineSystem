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
            this.connect.createStatement().executeQuery("INSERT INTO Pile VALUES (NULL, "+p.getMedicineNumber()+", "+p.getQuantity()+", NULL, "+p.getPile()+", "+p.getLayer()+", '"+p.getDate()+"', '"+"staff"+"')");
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
        this.connect.createStatement().executeQuery("UPDATE `Pile` SET `pile_quantity` = `pile_quantity`-"+quantity+" WHERE `med_id` = '"+p.getSerialNumber()+"' AND `pil_loc` = '"+p.getLocation()+"'");
        this.connect.createStatement().executeQuery("DELETE FROM `Pile` WHERE `pile_quantity` <= 0");
    }
}
