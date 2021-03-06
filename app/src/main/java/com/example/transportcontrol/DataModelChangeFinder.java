package com.example.transportcontrol;

import android.content.Context;

import com.example.transportcontrol.model.DataModel;

import java.util.ArrayList;

public class DataModelChangeFinder {

    public static String getChanges(Context context, DataModel originDataModel, DataModel changedDataModel) {
        ArrayList<String> changes = new ArrayList<>();
        if (!originDataModel.getMotorcade().equals(changedDataModel.getMotorcade())) {
            changes.add(context.getString(R.string.motorcade));
        }
        if (!originDataModel.getVehicleType().equals(changedDataModel.getVehicleType())) {
            changes.add(context.getString(R.string.model));
        }
        if (!originDataModel.getBrand().equals(changedDataModel.getBrand())) {
            changes.add(context.getString(R.string.brand));
        }
        if (!originDataModel.getPlateNumber().equals(changedDataModel.getPlateNumber())) {
            changes.add(context.getString(R.string.plateNumber));
        }
        if (!originDataModel.getInventoryNumber().equals(changedDataModel.getInventoryNumber())) {
            changes.add(context.getString(R.string.inventoryNumber));
        }
        if (!originDataModel.getGarageNumber().equals(changedDataModel.getGarageNumber())) {
            changes.add(context.getString(R.string.garageNumber));
        }
        if (!originDataModel.getDrivers().equals(changedDataModel.getDrivers())) {
            changes.add(context.getString(R.string.drivers));
        }
        if (!originDataModel.getTechnicalInspection().equals(changedDataModel.getTechnicalInspection())) {
            changes.add(context.getString(R.string.technicalInspection));
        }
        if (!originDataModel.getInsurance().equals(changedDataModel.getInsurance())) {
            changes.add(context.getString(R.string.insurance));
        }
        if (!originDataModel.getFirstAidKit().equals(changedDataModel.getFirstAidKit())) {
            changes.add(context.getString(R.string.firstAidKit));
        }
        if (!originDataModel.getExtinguisher().equals(changedDataModel.getExtinguisher())) {
            changes.add(context.getString(R.string.extinguisher));
        }
        if (!originDataModel.getPreviousTechnicalInspection().equals(changedDataModel.getPreviousTechnicalInspection())) {
            changes.add(context.getString(R.string.previousTechnicalInspection));
        }
        if (originDataModel.getWheelNumbers() == null && changedDataModel.getWheelNumbers() != null) {
            changes.add(context.getString(R.string.wheelNumbers));
        }
        else if (originDataModel.getWheelNumbers() != null && !originDataModel.getWheelNumbers().equals(changedDataModel.getWheelNumbers())) {
            changes.add(context.getString(R.string.wheelNumbers));
        }
        if (originDataModel.getComments() == null && changedDataModel.getComments() != null) {
            changes.add(context.getString(R.string.comments));
        }
        else if (originDataModel.getComments() != null && !originDataModel.getComments().equals(changedDataModel.getComments())) {
            changes.add(context.getString(R.string.comments));
        }
        if (originDataModel.getEliminationDate() == null && changedDataModel.getEliminationDate() != null) {
            changes.add(context.getString(R.string.eliminationDate));
        }
        else if (originDataModel.getEliminationDate() != null && !originDataModel.getEliminationDate().equals(changedDataModel.getEliminationDate())) {
            changes.add(context.getString(R.string.eliminationDate));
        }
        if (originDataModel.getCoolantDensity() == null && changedDataModel.getCoolantDensity() != null) {
            changes.add(context.getString(R.string.coolantDensity));
        }
        else if (originDataModel.getCoolantDensity() != null && !originDataModel.getCoolantDensity().equals(changedDataModel.getCoolantDensity())) {
            changes.add(context.getString(R.string.coolantDensity));
        }
        if (originDataModel.getElectricalEquipmentState() == null && changedDataModel.getElectricalEquipmentState() != null) {
            changes.add(context.getString(R.string.electricalEquipmentState));
        }
        else if (originDataModel.getElectricalEquipmentState() != null && !originDataModel.getElectricalEquipmentState().equals(changedDataModel.getElectricalEquipmentState())) {
            changes.add(context.getString(R.string.electricalEquipmentState));
        }
        if (originDataModel.getSufficientPressureInTheFireExtinguisher() == null && changedDataModel.getSufficientPressureInTheFireExtinguisher() != null) {
            changes.add(context.getString(R.string.sufficientPressureInTheFireExtinguisher));
        }
        else if (originDataModel.getSufficientPressureInTheFireExtinguisher() != null && !originDataModel.getSufficientPressureInTheFireExtinguisher().equals(changedDataModel.getSufficientPressureInTheFireExtinguisher())) {
            changes.add(context.getString(R.string.sufficientPressureInTheFireExtinguisher));
        }
        if (originDataModel.getElectrolyteDensity() == null && changedDataModel.getElectrolyteDensity() != null) {
            changes.add(context.getString(R.string.electrolyteDensity));
        }
        else if (originDataModel.getElectrolyteDensity() != null && !originDataModel.getElectrolyteDensity().equals(changedDataModel.getElectrolyteDensity())) {
            changes.add(context.getString(R.string.electrolyteDensity));
        }
        if (originDataModel.getOffers() == null && changedDataModel.getOffers() != null) {
            changes.add("Предложение");
        }
        else if (originDataModel.getOffers() != null && !originDataModel.getOffers().equals(changedDataModel.getOffers())) {
            changes.add("Предложение");
        }
        if (originDataModel.getPhoto() == null && changedDataModel.getPhoto() != null) {
            changes.add("Фото");
        }
        else if (originDataModel.getPhoto() != null && !originDataModel.getPhoto().equals(changedDataModel.getPhoto())) {
            changes.add("Фото");
        }
        return changes.toString();
    }
}
