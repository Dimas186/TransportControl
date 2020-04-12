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
            changes.add(context.getString(R.string.vehicleType));
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
        return changes.toString();
    }
}
