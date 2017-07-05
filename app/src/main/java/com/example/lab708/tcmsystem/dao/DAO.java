package com.example.lab708.tcmsystem.dao;

/**
 * Created by Aurelie on 05/07/2017.
 */

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO<T> {

    protected Connection connect = null;

    public DAO(Connection conn) {
        this.connect = conn;
    }

    public abstract void connect();
    public abstract boolean create(T obj);
    public abstract boolean delete(T obj);
    public abstract boolean update(T obj);
    public abstract boolean find(T obj);
}
