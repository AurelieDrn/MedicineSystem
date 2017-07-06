package com.example.lab708.tcmsystem;

import android.widget.NumberPicker;

/**
 * Created by Aurelie on 06/07/2017.
 */

public class MyNumberPicker {

    private NumberPicker location1;
    private NumberPicker location2;
    private NumberPicker location3;

    public MyNumberPicker(NumberPicker location1, NumberPicker location2, NumberPicker location3) {
        this.location1 = location1;
        this.location2 = location2;
        this.location3 = location3;
    }

    public NumberPicker getLocation1() {
        return location1;
    }

    public void setLocation1(NumberPicker location1) {
        this.location1 = location1;
    }

    public NumberPicker getLocation2() {
        return location2;
    }

    public void setLocation2(NumberPicker location2) {
        this.location2 = location2;
    }

    public NumberPicker getLocation3() {
        return location3;
    }

    public void setLocation3(NumberPicker location3) {
        this.location3 = location3;
    }
}
