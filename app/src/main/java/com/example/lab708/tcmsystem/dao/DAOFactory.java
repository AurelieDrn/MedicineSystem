package com.example.lab708.tcmsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class DAOFactory {

    protected static Connection conn = null;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mariadb://120.105.161.89/TCMSystem", "TCMSystem", "TCMSystem");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DAO getStaffDAO() {
        return new StaffDAO(conn);
    }

    public static DAO getMedicineDAO() {
        return new MedicineDAO(conn);
    }
}
