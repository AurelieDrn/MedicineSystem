package com.example.lab708.tcmsystem.adaptater;

/**
 * Created by Aurelie on 11/07/2017.
 */

public class QuantityLocation {

    private int quantity;
    private String location;

    public QuantityLocation(String location, int quantity) {
        this.location = location;
        this.quantity = quantity;
    }

    public QuantityLocation() {
        this.location = "";
        this.quantity = 0;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "QuantityLocation{" +
                "location='" + location + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
