package com.example.transportcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.transportcontrol.model.DataModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddingActivityModer3 extends AppCompatActivity {

    EditText motorcade, vehicleType, brand, inventoryNumber, garageNumber, drivers, technicalInspection;
    EditText insurance, firstAidKit, extinguisher, previousTechnicalInspection, wheelNumbers, eliminationDate;
    DataModel dataModel = new DataModel();
    ArrayList<String> driversList = new ArrayList<>();
    ArrayList<String> wheelNumbersList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_moder3);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("items");
        initViews();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addDriver:
                driversList.add(drivers.getText().toString());
                break;
            case R.id.addWheelNumbers:
                wheelNumbersList.add(wheelNumbers.getText().toString());
                break;
            case R.id.btnAdd:
                setDataModel();
                myRef.push().setValue(dataModel);
                break;
        }
    }

    private void initViews() {
        motorcade = findViewById(R.id.motorcade);
        vehicleType = findViewById(R.id.vehicleType);
        brand = findViewById(R.id.brand);
        inventoryNumber = findViewById(R.id.inventoryNumber);
        garageNumber = findViewById(R.id.garageNumber);
        drivers = findViewById(R.id.drivers);
        technicalInspection = findViewById(R.id.technicalInspection);
        insurance = findViewById(R.id.insurance);
        firstAidKit = findViewById(R.id.firstAidKit);
        extinguisher = findViewById(R.id.extinguisher);
        previousTechnicalInspection = findViewById(R.id.previousTechnicalInspection);
        wheelNumbers = findViewById(R.id.wheelNumbers);
        eliminationDate = findViewById(R.id.eliminationDate);
    }

    private void setDataModel() {
        dataModel.setMotorcade(motorcade.getText().toString());
        dataModel.setVehicleType(vehicleType.getText().toString());
        dataModel.setBrand(brand.getText().toString());
        dataModel.setInventoryNumber(inventoryNumber.getText().toString());
        dataModel.setGarageNumber(garageNumber.getText().toString());
        dataModel.setDrivers(driversList);
        dataModel.setTechnicalInspection(technicalInspection.getText().toString());
        dataModel.setInsurance(insurance.getText().toString());
        dataModel.setFirstAidKit(firstAidKit.getText().toString());
        dataModel.setExtinguisher(extinguisher.getText().toString());
        dataModel.setPreviousTechnicalInspection(previousTechnicalInspection.getText().toString());
        dataModel.setWheelNumbers(wheelNumbersList);
        dataModel.setEliminationDate(eliminationDate.getText().toString());
    }
}
