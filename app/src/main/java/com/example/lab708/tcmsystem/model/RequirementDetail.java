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

    public RequirementDetail(String name, List<QuantityLocation> quantityLocationList, String sn, int qs) {
        this.name = name;
        this.quantityLocationList = quantityLocationList;
        this.serialNumber = sn;
        this.quantityInStock = qs;
    }

    public RequirementDetail() {
        this.name = "";
        this.quantityLocationList = new ArrayList<>();
        this.serialNumber = "";
        this.quantityInStock = 0;
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

    @Override
    public String toString() {
        return "RequirementDetail{" +
                "name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", quantityLocationList=" + quantityLocationList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.quantityLocationList);
    }

    protected RequirementDetail(Parcel in) {
        this.name = in.readString();
        this.quantityLocationList = new ArrayList<QuantityLocation>();
        in.readList(this.quantityLocationList, QuantityLocation.class.getClassLoader());
    }

    public static final Parcelable.Creator<RequirementDetail> CREATOR = new Parcelable.Creator<RequirementDetail>() {
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
