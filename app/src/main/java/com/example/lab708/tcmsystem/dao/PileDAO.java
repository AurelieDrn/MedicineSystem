package com.example.lab708.tcmsystem.dao;

/**
 * Created by Aurelie on 06/07/2017.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PileDAO extends DAO<Pile> {

    public PileDAO(Connection conn) {
        super(conn);
    }

    public void create(Pile p) throws SQLException {
        ResultSet result = this.connect.createStatement().executeQuery("INSERT INTO Pile VALUES (NULL,'"+p.getMedicineNumber()+"',"+p.getQuantity()+",'"+p.getLocation()+"','"+p.getDate()+"','"+"staff"+"')");
    }

}
