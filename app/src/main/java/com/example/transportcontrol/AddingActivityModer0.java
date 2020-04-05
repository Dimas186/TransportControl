package com.example.transportcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.transportcontrol.model.DataModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddingActivityModer0 extends AppCompatActivity {

    EditText motorcade, vehicleType, brand, plateNumber, inventoryNumber, garageNumber, drivers, technicalInspection;
    EditText insurance, firstAidKit, extinguisher, previousTechnicalInspection, comments, coolantDensity;
    EditText electricalEquipmentState, sufficientPressureInTheFireExtinguisher, electrolyteDensity;
    DataModel dataModel = new DataModel();
    ArrayList<String> driversList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public static boolean isAdded() {
        return isAdded;
    }

    public static void setIsAdded(boolean isAdded) {
        AddingActivityModer0.isAdded = isAdded;
    }

    private static boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_moder0);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("items");
        initViews();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addDriver:
                driversList.add(drivers.getText().toString());
                break;
            case R.id.btnAdd:
                setDataModel();
                myRef.push().setValue(dataModel);
                isAdded = true;
                break;
        }
    }

    private void initViews() {
        motorcade = findViewById(R.id.motorcade);
        vehicleType = findViewById(R.id.vehicleType);
        brand = findViewById(R.id.brand);
        plateNumber = findViewById(R.id.plateNumber);
        inventoryNumber = findViewById(R.id.inventoryNumber);
        garageNumber = findViewById(R.id.garageNumber);
        drivers = findViewById(R.id.drivers);
        technicalInspection = findViewById(R.id.technicalInspection);
        insurance = findViewById(R.id.insurance);
        firstAidKit = findViewById(R.id.firstAidKit);
        extinguisher = findViewById(R.id.extinguisher);
        previousTechnicalInspection = findViewById(R.id.previousTechnicalInspection);
        comments = findViewById(R.id.comments);
        coolantDensity = findViewById(R.id.coolantDensity);
        electricalEquipmentState = findViewById(R.id.electricalEquipmentState);
        sufficientPressureInTheFireExtinguisher = findViewById(R.id.sufficientPressureInTheFireExtinguisher);
        electrolyteDensity = findViewById(R.id.electrolyteDensity);
    }

    private void setDataModel() {
        dataModel.setMotorcade(motorcade.getText().toString());
        dataModel.setVehicleType(vehicleType.getText().toString());
        dataModel.setBrand(brand.getText().toString());
        dataModel.setPlateNumber(plateNumber.getText().toString());
        dataModel.setInventoryNumber(inventoryNumber.getText().toString());
        dataModel.setGarageNumber(garageNumber.getText().toString());
        dataModel.setDrivers(driversList);
        dataModel.setTechnicalInspection(technicalInspection.getText().toString());
        dataModel.setInsurance(insurance.getText().toString());
        dataModel.setFirstAidKit(firstAidKit.getText().toString());
        dataModel.setExtinguisher(extinguisher.getText().toString());
        dataModel.setPreviousTechnicalInspection(previousTechnicalInspection.getText().toString());
        dataModel.setComments(comments.getText().toString());
        dataModel.setCoolantDensity(coolantDensity.getText().toString());
        dataModel.setElectricalEquipmentState(electricalEquipmentState.getText().toString());
        dataModel.setSufficientPressureInTheFireExtinguisher(sufficientPressureInTheFireExtinguisher.getText().toString());
        dataModel.setElectrolyteDensity(electrolyteDensity.getText().toString());
    }
}
