package com.example.transportcontrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.transportcontrol.handler.OCRHandler;
import com.example.transportcontrol.model.DataModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static com.example.transportcontrol.handler.PlateNumberHandler.handleText;

public class AddOrChangeActivityModer0 extends AppCompatActivity {

    EditText motorcade, vehicleType, brand, plateNumber, inventoryNumber, garageNumber, drivers, technicalInspection;
    EditText insurance, firstAidKit, extinguisher, previousTechnicalInspection, comments, coolantDensity;
    EditText electricalEquipmentState, sufficientPressureInTheFireExtinguisher, electrolyteDensity;
    ImageView ivPhoto;
    static DataModel dataModel = new DataModel();
    DataModel changedDataModel = new DataModel();
    ArrayList<String> driversList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference logsRef;
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;
    private static boolean isAddedOrChanged = false;
    private static boolean forChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_change_moder0);

        try {
            changedDataModel = dataModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (dataModel.getDrivers() != null) {
            driversList.addAll(dataModel.getDrivers());
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("items");
        logsRef = database.getReference("logs");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initViews();
        setEditTexts();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addDriver:
                driversList.add(drivers.getText().toString());
                drivers.setText("");
                break;
            case R.id.scan:
                showImageImportDialog();
                break;
            case R.id.addPhoto:
                Intent intent = new Intent(this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 9917);
                break;
            case R.id.btnAddOrChange:
                updateDataModel();
                if (forChanges) {
                    String changes = DataModelChangeFinder.getChanges(this, dataModel, changedDataModel);
                    if (!changes.isEmpty()) {
                        myRef.child(dataModel.getId()).setValue(changedDataModel);
                    }
                    addLog(changes);
                } else {
                    myRef.push().setValue(changedDataModel);
                    addLog("");
                }
                isAddedOrChanged = true;
                forChanges = false;
                dataModel = new DataModel();
                finish();
                break;
        }
    }

    private void addLog(String changes) {
        String userName = getString(R.string.user_name_and_last_name,
                MainActivity.getCurrentUser().getName(), MainActivity.getCurrentUser().getLastName());
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        if (forChanges) {
            if (!changes.isEmpty()) {
                logsRef.push()
                        .setValue(getString(R.string.changedBy, userName, changes, plateNumber.getText().toString(), time));
            }
        } else {
            logsRef.push().setValue(getString(R.string.addedBy, userName, plateNumber.getText().toString(), time));
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
        ivPhoto = findViewById(R.id.ivPhoto);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void updateDataModel() {
        changedDataModel.setMotorcade(motorcade.getText().toString());
        changedDataModel.setVehicleType(vehicleType.getText().toString());
        changedDataModel.setBrand(brand.getText().toString());
        changedDataModel.setPlateNumber(plateNumber.getText().toString());
        changedDataModel.setInventoryNumber(inventoryNumber.getText().toString());
        changedDataModel.setGarageNumber(garageNumber.getText().toString());
        changedDataModel.setDrivers(driversList);
        changedDataModel.setTechnicalInspection(technicalInspection.getText().toString());
        changedDataModel.setInsurance(insurance.getText().toString());
        changedDataModel.setFirstAidKit(firstAidKit.getText().toString());
        changedDataModel.setExtinguisher(extinguisher.getText().toString());
        changedDataModel.setPreviousTechnicalInspection(previousTechnicalInspection.getText().toString());
        changedDataModel.setComments(comments.getText().toString());
        changedDataModel.setCoolantDensity(coolantDensity.getText().toString());
        changedDataModel.setElectricalEquipmentState(electricalEquipmentState.getText().toString());
        changedDataModel.setSufficientPressureInTheFireExtinguisher(sufficientPressureInTheFireExtinguisher.getText().toString());
        changedDataModel.setElectrolyteDensity(electrolyteDensity.getText().toString());
    }

    private void setEditTexts() {
        motorcade.setText(dataModel.getMotorcade());
        vehicleType.setText(dataModel.getVehicleType());
        brand.setText(dataModel.getBrand());
        plateNumber.setText(dataModel.getPlateNumber());
        inventoryNumber.setText(dataModel.getInventoryNumber());
        garageNumber.setText(dataModel.getGarageNumber());
        technicalInspection.setText(dataModel.getTechnicalInspection());
        insurance.setText(dataModel.getInsurance());
        firstAidKit.setText(dataModel.getFirstAidKit());
        extinguisher.setText(dataModel.getExtinguisher());
        previousTechnicalInspection.setText(dataModel.getPreviousTechnicalInspection());
        comments.setText(dataModel.getComments());
        coolantDensity.setText(dataModel.getCoolantDensity());
        electricalEquipmentState.setText(dataModel.getElectricalEquipmentState());
        sufficientPressureInTheFireExtinguisher.setText(dataModel.getSufficientPressureInTheFireExtinguisher());
        electrolyteDensity.setText(dataModel.getElectrolyteDensity());
    }

    private void showImageImportDialog() {
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent, 1213);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1213) {
                String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                CropImage.activity(Uri.fromFile(new File(filePath)))
                        .setGuidelines(CropImageView.Guidelines.ON).start(this);
            } else if (requestCode == 9917) {
                String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                uploadFileInFireBaseStorage(Uri.fromFile(new File(filePath)));
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                plateNumber.setText(handleText(OCRHandler.handle(data, AddOrChangeActivityModer0.this).toString()));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadFileInFireBaseStorage(Uri uri) {
        initProgressDialog();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());//получаем время
        final String imageFileName = "photo_" + timeStamp;
        final StorageReference ref = mStorageRef.child("images/" + imageFileName);
        UploadTask uploadTask = ref.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    System.out.println(downloadUri);
                    changedDataModel.setPhoto(downloadUri.toString());
                    ivPhoto.setVisibility(View.VISIBLE);
                    Glide.with(AddOrChangeActivityModer0.this) //Takes the context
                            .asBitmap()  //Tells glide that it is a bitmap
                            .load(downloadUri)
                            .apply(new RequestOptions()
                                    .override(900, 600)
                                    .centerCrop()
                                    .placeholder(R.drawable.progress_animation))
                            .into(ivPhoto);
                }
                progressDialog.cancel();
            }
        });
    }

    //getters and setters
    public static boolean isAddedOrChanged() {
        return isAddedOrChanged;
    }

    public static void setIsAddedOrChanged(boolean isAdded) {
        AddOrChangeActivityModer0.isAddedOrChanged = isAdded;
    }

    public static void setForChanges(boolean forChanges) {
        AddOrChangeActivityModer0.forChanges = forChanges;
    }

    public static void setDataModel(DataModel dataModel) {
        AddOrChangeActivityModer0.dataModel = dataModel;
    }
}
