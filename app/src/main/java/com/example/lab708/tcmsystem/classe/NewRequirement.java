package com.example.lab708.tcmsystem.classe;

import java.io.Serializable;

/**
 * Created by Aurelie on 13/07/2017.
 */

public class NewRequirement implements Serializable {

    private String medicineNumber;
    private String medicineName;
    private int quantity;

    public NewRequirement(String medicineName, String medicineNumber, int quantity) {
        this.medicineName = medicineName;
        this.medicineNumber = medicineNumber;
        this.quantity = quantity;
    }

    public NewRequirement() {
        this.medicineNumber = "";
        this.medicineName = "";
        this.quantity = 0;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
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

    @Override
    public String toString() {
        return "NewRequirement{" +
                "medicineName='" + medicineName + '\'' +
                ", medicineNumber='" + medicineNumber + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
