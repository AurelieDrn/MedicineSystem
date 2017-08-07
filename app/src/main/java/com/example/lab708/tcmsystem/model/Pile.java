package com.example.lab708.tcmsystem.model;

/**
 * Created by Aurelie on 06/07/2017.
 */

public class Pile {

    private int ser;
    private String medicineNumber;
    private int quantity;
    private String layer;
    private String pile;
    private String date;
    private String staffAccount;

    public Pile(String layer, String pile, String medicineNumber, int quantity, String date) {
        this.layer = layer;
        this.pile = pile;
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

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getMedicineNumber() {
        return medicineNumber;
    }

    public void setMedicineNumber(String medicineNumber) {
        this.medicineNumber = medicineNumber;
    }

    public String getPile() {
        return pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
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

    @Override
    public String toString() {
        return "Pile{" +
                "date='" + date + '\'' +
                ", ser=" + ser +
                ", medicineNumber='" + medicineNumber + '\'' +
                ", quantity=" + quantity +
                ", layer='" + layer + '\'' +
                ", pile='" + pile + '\'' +
                ", staffAccount='" + staffAccount + '\'' +
                '}';
    }
}
