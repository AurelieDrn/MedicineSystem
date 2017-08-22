package com.example.lab708.tcmsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.lab708.tcmsystem.AppConstants.IP_PC;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class DAOFactory {

    protected static Connection conn = null;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            String databaseAddress = "jdbc:mariadb://"+IP_PC+"/TCMSystem";
            conn = DriverManager.getConnection(databaseAddress, "TCMSystem", "TCMSystem");
            //conn = DriverManager.getConnection("jdbc:mariadb://192.168.0.173:3306/tcmsystem", "TCMSystem", "TCMSystem");
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

    public static StaffDAO getStaffDAO() {
        return new StaffDAO(conn);
    }

    public static MedicineDAO getMedicineDAO() {
        return new MedicineDAO(conn);
    }

    public static PileDAO getPileDAO() {
        return new PileDAO(conn);
    }

    public static RequirementDAO getRequirementDAO() {
        return new RequirementDAO(conn);
    }
}
