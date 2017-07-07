package com.example.lab708.tcmsystem.dao;

import com.example.lab708.tcmsystem.adaptater.Requirement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Aurelie on 07/07/2017.
 */

public class RequirementDAO extends DAO<Requirement>{

    public RequirementDAO(Connection conn) {
        super(conn);
    }

}
