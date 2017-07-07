package com.example.lab708.tcmsystem.adaptater;

import java.util.List;

/**
 * Created by Aurelie on 07/07/2017.
 */

public class Requirement {

    private int number;
    private int emergency;
    private int progress;
    private List<Integer> medicineNumbers;
    private List<String> medicineNames;

    public Requirement(int emergency, List<String> medicineNames, List<Integer> medicineNumbers, int number, int progress) {
        this.emergency = emergency;
        this.medicineNames = medicineNames;
        this.medicineNumbers = medicineNumbers;
        this.number = number;
        this.progress = progress;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public List<String> getMedicineNames() {
        return medicineNames;
    }

    public void setMedicineNames(List<String> medicineNames) {
        this.medicineNames = medicineNames;
    }

    public List<Integer> getMedicineNumbers() {
        return medicineNumbers;
    }

    public void setMedicineNumbers(List<Integer> medicineNumbers) {
        this.medicineNumbers = medicineNumbers;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
