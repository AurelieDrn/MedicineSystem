package com.example.lab708.tcmsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aurelie on 11/07/2017.
 */

public class QuantityLocation implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quantity);
        dest.writeString(this.location);
    }

    protected QuantityLocation(Parcel in) {
        this.quantity = in.readInt();
        this.location = in.readString();
    }

    public static final Creator<QuantityLocation> CREATOR = new Creator<QuantityLocation>() {
        @Override
        public QuantityLocation createFromParcel(Parcel source) {
            return new QuantityLocation(source);
        }

        @Override
        public QuantityLocation[] newArray(int size) {
            return new QuantityLocation[size];
        }
    };
}
