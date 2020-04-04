package com.example.transportcontrol;

import java.util.ArrayList;

public class DataModel {

    private int id;
    private String motorcade, vehicleType, brand, plateNumber, inventoryNumber, garageNumber;
    private ArrayList<String> drivers;
    private String speedometerReading, technicalInspection, insurance, firstAidKit, extinguisher,
            previousTechnicalInspection;
    private ArrayList<String> wheelNumbers;
    private String comments, eliminationDate, coolantDensity, electricalEquipmentState;
    private String sufficientPressureInTheFireExtinguisher, electrolyteDensity, offers;

    public DataModel() {

    }
}
