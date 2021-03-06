package com.example.lab708.tcmsystem.model;

/**
 * Created by Aurelie on 05/07/2017.
 */

public class Medicine {

    private String serialNumber;
    private String name;
    private String ingredients;
    private String brand;
    private String firmNumber;
    private String experienceQuantity;
    private int shelfNumber;

    public Medicine(String brand, String experienceQuantity, String firmNumber, String ingredients, String name, String serialNumber, int shelfNumber) {
        this.brand = brand;
        this.experienceQuantity = experienceQuantity;
        this.firmNumber = firmNumber;
        this.ingredients = ingredients;
        this.name = name;
        this.serialNumber = serialNumber;
        this.shelfNumber = shelfNumber;
    }

    public Medicine() {
        this.serialNumber = new String();
        this.name = new String();
        this.ingredients = new String();
        this.brand = new String();
        this.firmNumber = new String();
        this.experienceQuantity = new String();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getExperienceQuantity() {
        return experienceQuantity;
    }

    public void setExperienceQuantity(String experienceQuantity) {
        this.experienceQuantity = experienceQuantity;
    }

    public String getFirmNumber() {
        return firmNumber;
    }

    public void setFirmNumber(String firmNumber) {
        this.firmNumber = firmNumber;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "brand='" + brand + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", firmNumber='" + firmNumber + '\'' +
                ", experienceQuantity='" + experienceQuantity + '\'' +
                ", shelfNumber=" + shelfNumber +
                '}';
    }
}
