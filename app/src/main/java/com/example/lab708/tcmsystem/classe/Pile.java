package com.example.lab708.tcmsystem.classe;

/**
 * Created by Aurelie on 06/07/2017.
 */

public class Pile {

    private int ser;
    private String medicineNumber;
    private int quantity;
    private String location;
    private String date;
    private String staffAccount;

    public Pile(String location, String medicineNumber, int quantity, String date) {
        this.location = location;
        this.medicineNumber = medicineNumber;
        this.quantity = quantity;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMedicineNumber() {
        return medicineNumber;
    }

    public void setMedicineNumber(String medicineNumber) {
        this.medicineNumber = medicineNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSer() {
        return ser;
    }

    public void setSer(int ser) {
        this.ser = ser;
    }

    public String getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(String staffAccount) {
        this.staffAccount = staffAccount;
    }
}
