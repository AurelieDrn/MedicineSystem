package com.example.lab708.tcmsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aurelie on 11/07/2017.
 */

public class RequirementDetail implements Parcelable {

    private String name;
    private String serialNumber;
    private int quantityInStock;
    private List<QuantityLocation> quantityLocationList;
    private int quantityToPick;

    public RequirementDetail() {
        this.name = "";
        this.quantityLocationList = new ArrayList<>();
        this.serialNumber = "";
        this.quantityInStock = 0;
        this.quantityToPick = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuantityLocation> getQuantityLocationList() {
        return quantityLocationList;
    }

    public void setQuantityLocationList(List<QuantityLocation> quantityLocationList) {
        this.quantityLocationList = quantityLocationList;
    }

    public boolean add(QuantityLocation ql) {
        return this.quantityLocationList.add(ql);
    }

    public static Creator<RequirementDetail> getCREATOR() {
        return CREATOR;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getQuantityToPick() {
        return quantityToPick;
    }

    public void setQuantityToPick(int quantityToPick) {
        this.quantityToPick = quantityToPick;
    }

    @Override
    public String toString() {
        return "RequirementDetail{" +
                "name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", quantityLocationList=" + quantityLocationList +
                ", quantityToPick=" + quantityToPick +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.serialNumber);
        dest.writeInt(this.quantityInStock);
        dest.writeTypedList(this.quantityLocationList);
        dest.writeInt(this.quantityToPick);
    }

    protected RequirementDetail(Parcel in) {
        this.name = in.readString();
        this.serialNumber = in.readString();
        this.quantityInStock = in.readInt();
        this.quantityLocationList = in.createTypedArrayList(QuantityLocation.CREATOR);
        this.quantityToPick = in.readInt();
    }

    public static final Creator<RequirementDetail> CREATOR = new Creator<RequirementDetail>() {
        @Override
        public RequirementDetail createFromParcel(Parcel source) {
            return new RequirementDetail(source);
        }

        @Override
        public RequirementDetail[] newArray(int size) {
            return new RequirementDetail[size];
        }
    };
}
