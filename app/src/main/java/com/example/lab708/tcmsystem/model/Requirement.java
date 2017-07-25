package com.example.lab708.tcmsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aurelie on 07/07/2017.
 */

public class Requirement {

    private String number;
    private int emergency;
    private int progress;
    private List<String> medicineNumbers;
    private List<Medicine> medicines;

    public Requirement(int emergency, List<String> medicineNumbers, List<Medicine> medicines, String number, int progress) {
        this.emergency = emergency;
        this.medicineNumbers = medicineNumbers;
        this.medicines = medicines;
        this.number = number;
        this.progress = progress;
    }

    public Requirement() {
        this.number = new String();
        this.emergency = 0;
        this.progress = 0;
        this.medicineNumbers = new ArrayList<>();
        this.medicines = new ArrayList<>();
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public List<String> getMedicineNumbers() {
        return medicineNumbers;
    }

    public void setMedicineNumbers(List<String> medicineNumbers) {
        this.medicineNumbers = medicineNumbers;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void addMedicine(Medicine m) {
        this.medicines.add(m);
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "emergency=" + emergency +
                ", number='" + number + '\'' +
                ", progress=" + progress +
                ", medicineNumbers=" + medicineNumbers +
                ", medicines=" + medicines +
                '}';
    }
}
