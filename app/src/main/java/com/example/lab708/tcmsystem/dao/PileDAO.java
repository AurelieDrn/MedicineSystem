package com.example.lab708.tcmsystem.dao;

/**
 * Created by Aurelie on 06/07/2017.
 */

import android.util.Log;

import com.example.lab708.tcmsystem.adapter.QuantityLocation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PileDAO extends DAO<Pile> {

    public PileDAO(Connection conn) {
        super(conn);
    }

    public void create(Pile p) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pil_ser`, `pil_quan` FROM `Pile` WHERE `pile_mednum` = \'"+p.getMedicineNumber()+"\' AND `pil_exp` = \'"+p.getDate()+"\' AND `pil_loc`= "+p.getLocation());
        if(result.first()) {
            int id = result.getInt("pil_ser");
            int quant = result.getInt("pil_quan")+p.getQuantity();
            Log.d("TAG", String.valueOf(id)+"  "+String.valueOf(quant));
            this.connect.createStatement().executeQuery("UPDATE `Pile` SET `pile_mednum` = \'"+p.getMedicineNumber()+"\',`pil_quan` = "+quant+",`pil_loc`= '"+p.getLocation()+"',`pil_exp`='"+p.getDate()+"' WHERE `pil_ser`= "+id);
        }
        else {
            this.connect.createStatement().executeQuery("INSERT INTO Pile VALUES (NULL,'"+p.getMedicineNumber()+"',"+p.getQuantity()+",'"+p.getLocation()+"','"+p.getDate()+"','"+"staff"+"')");
        }
    }

    public ArrayList<QuantityLocation> getQuantLocations(int quantity, String medNumber) throws SQLException {
        ArrayList<QuantityLocation> quantLocationList = new ArrayList<>();

        ResultSet result = this.connect.createStatement().executeQuery("SELECT `pil_quan`, `pil_loc` FROM `Pile` WHERE `pile_mednum`=\'"+medNumber+"\' ORDER BY `pil_exp` ASC");

        int quant = quantity;
        while(result.next() && quant > 0) {
            int dbQuant = Integer.valueOf(result.getString("pil_quan"));
            String dbLoc = result.getString("pil_loc");

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

}
