package com.example.lab708.tcmsystem;

/**
 * Created by Aurelie on 17/07/2017.
 */

public final class AppConstants {

    private AppConstants() {
    }

    // IP of RPI
    /*
    public static final String IP = "10.200.205.60";
    public static final String IP2 = "10.200.205.67";
    public static final String IP3 = "10.200.205.68";
    */

    public static final String IP = "192.168.0.6";
    public static final String IP2 = "192.168.0.14";
    public static final String IP3 = "192.168.0.5";

    // IP of the computer
    // public static final String IP_PC = "10.207.157.54";
    public static final String IP_PC = "192.168.0.4";

    // PORT number to connect to the RPI
    public static final int PORT = 8080;

    // port number of the computer for the emergency function
    public static final int PORT_PC = 9090;
}
