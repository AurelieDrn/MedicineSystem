package com.example.lab708.tcmsystem.adaptater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aurelie on 11/07/2017.
 */

public class RequirementDetail {

    private String name;
    private List<QuantityLocation> quantityLocationList;

    public RequirementDetail(String name, List<QuantityLocation> quantityLocationList) {
        this.name = name;
        this.quantityLocationList = quantityLocationList;
    }

    public RequirementDetail() {
        this.name = "";
        this.quantityLocationList = new ArrayList<>();
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

    @Override
    public String toString() {
        return "RequirementDetail{" +
                "name='" + name + '\'' +
                ", quantityLocationList=" + quantityLocationList +
                '}';
    }
}
