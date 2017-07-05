package com.example.lab708.tcmsystem.dao;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class Staff {

    private String account;
    private String password;

    public Staff(String acc, String pass) {
        this.account = acc;
        this.password = pass;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
