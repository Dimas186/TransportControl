package com.example.transportcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddingActivityModer0 extends AppCompatActivity {

    EditText motorcade, vehicleType, brand, plateNumber, inventoryNumber, garageNumber, drivers, technicalInspection;
    EditText insurance, firstAidKit, extinguisher, previousTechnicalInspection, comments, coolantDensity;
    EditText electricalEquipmentState, sufficientPressureInTheFireExtinguisher, electrolyteDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_moder0);
    }

    public void onClick(View v) {

    }
}
