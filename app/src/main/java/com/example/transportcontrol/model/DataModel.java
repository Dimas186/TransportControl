package com.example.transportcontrol.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DataModel implements Cloneable{

    private String id;
    private String motorcade, vehicleType, brand, plateNumber, inventoryNumber, garageNumber;
    private ArrayList<String> drivers;
    private String technicalInspection, insurance, firstAidKit, extinguisher,
            previousTechnicalInspection;
    private ArrayList<String> wheelNumbers;
    private String comments, eliminationDate, coolantDensity, electricalEquipmentState;
    private String sufficientPressureInTheFireExtinguisher, electrolyteDensity, offers;
    private String photo;

    public DataModel() {

    }

    @NonNull
    @Override
    public DataModel clone() throws CloneNotSupportedException {
        return (DataModel) super.clone();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMotorcade() {
        return motorcade;
    }

    public void setMotorcade(String motorcade) {
        this.motorcade = motorcade;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getGarageNumber() {
        return garageNumber;
    }

    public void setGarageNumber(String garageNumber) {
        this.garageNumber = garageNumber;
    }

    public ArrayList<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<String> drivers) {
        this.drivers = drivers;
    }

    public String getTechnicalInspection() {
        return technicalInspection;
    }

    public void setTechnicalInspection(String technicalInspection) {
        this.technicalInspection = technicalInspection;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getFirstAidKit() {
        return firstAidKit;
    }

    public void setFirstAidKit(String firstAidKit) {
        this.firstAidKit = firstAidKit;
    }

    public String getExtinguisher() {
        return extinguisher;
    }

    public void setExtinguisher(String extinguisher) {
        this.extinguisher = extinguisher;
    }

    public String getPreviousTechnicalInspection() {
        return previousTechnicalInspection;
    }

    public void setPreviousTechnicalInspection(String previousTechnicalInspection) {
        this.previousTechnicalInspection = previousTechnicalInspection;
    }

    public ArrayList<String> getWheelNumbers() {
        return wheelNumbers;
    }

    public void setWheelNumbers(ArrayList<String> wheelNumbers) {
        this.wheelNumbers = wheelNumbers;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEliminationDate() {
        return eliminationDate;
    }

    public void setEliminationDate(String eliminationDate) {
        this.eliminationDate = eliminationDate;
    }

    public String getCoolantDensity() {
        return coolantDensity;
    }

    public void setCoolantDensity(String coolantDensity) {
        this.coolantDensity = coolantDensity;
    }

    public String getElectricalEquipmentState() {
        return electricalEquipmentState;
    }

    public void setElectricalEquipmentState(String electricalEquipmentState) {
        this.electricalEquipmentState = electricalEquipmentState;
    }

    public String getSufficientPressureInTheFireExtinguisher() {
        return sufficientPressureInTheFireExtinguisher;
    }

    public void setSufficientPressureInTheFireExtinguisher(String sufficientPressureInTheFireExtinguisher) {
        this.sufficientPressureInTheFireExtinguisher = sufficientPressureInTheFireExtinguisher;
    }

    public String getElectrolyteDensity() {
        return electrolyteDensity;
    }

    public void setElectrolyteDensity(String electrolyteDensity) {
        this.electrolyteDensity = electrolyteDensity;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }
}
