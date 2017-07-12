package com.example.lab708.tcmsystem.classe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aurelie on 12/07/2017.
 */

public class Pickup implements Parcelable, Comparable {

    private String serialNumber;
    private String medicineName;
    private String location;
    private int quantityStock;
    private int quantityToPick;

    public Pickup(String location, String medicineName, int quantityStock, int quantityToPick, String serialNumber) {
        this.location = location;
        this.medicineName = medicineName;
        this.quantityStock = quantityStock;
        this.quantityToPick = quantityToPick;
        this.serialNumber = serialNumber;
    }

    public Pickup() {
        this.serialNumber = "";
        this.medicineName = "";
        this.location = "";
        this.quantityStock = 0;
        this.quantityToPick = 0;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public int getQuantityToPick() {
        return quantityToPick;
    }

    public void setQuantityToPick(int quantityToPick) {
        this.quantityToPick = quantityToPick;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serialNumber);
        dest.writeString(this.medicineName);
        dest.writeString(this.location);
        dest.writeInt(this.quantityStock);
        dest.writeInt(this.quantityToPick);
    }

    protected Pickup(Parcel in) {
        this.serialNumber = in.readString();
        this.medicineName = in.readString();
        this.location = in.readString();
        this.quantityStock = in.readInt();
        this.quantityToPick = in.readInt();
    }

    public static final Parcelable.Creator<Pickup> CREATOR = new Parcelable.Creator<Pickup>() {
        @Override
        public Pickup createFromParcel(Parcel source) {
            return new Pickup(source);
        }

        @Override
        public Pickup[] newArray(int size) {
            return new Pickup[size];
        }
    };

    @Override
    public String toString() {
        return "Pickup{" +
                "location='" + location + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", medicineName='" + medicineName + '\'' +
                ", quantityStock=" + quantityStock +
                ", quantityToPick=" + quantityToPick +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Pickup pickup = (Pickup) o;
        if(Integer.valueOf(this.location) < Integer.valueOf((pickup.getLocation()))) {
            return -1;
        }
        else if(Integer.valueOf(this.location) == Integer.valueOf((pickup.getLocation()))) {
            return 0;
        }
        else return 1;
    }
}
