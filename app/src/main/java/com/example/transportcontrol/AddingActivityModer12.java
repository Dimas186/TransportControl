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
import com.example.transportcontrol.model.DataModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class AddingActivityModer12 extends AppCompatActivity {

    EditText motorcade, vehicleType, brand, plateNumber, inventoryNumber, garageNumber, drivers, technicalInspection;
    EditText insurance, firstAidKit, extinguisher, previousTechnicalInspection, wheelNumbers, eliminationDate;
    ImageView ivPhoto;
    DataModel dataModel = new DataModel();
    ArrayList<String> driversList = new ArrayList<>();
    ArrayList<String> wheelNumbersList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;

    public static boolean isAdded() {
        return isAdded;
    }

    public static void setIsAdded(boolean isAdded) {
        AddingActivityModer12.isAdded = isAdded;
    }

    private static boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_moder12);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("items");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initViews();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addDriver:
                driversList.add(drivers.getText().toString());
                break;
            case R.id.scan:
                showImageImportDialog();
                break;
            case R.id.addWheelNumbers:
                wheelNumbersList.add(wheelNumbers.getText().toString());
                break;
            case R.id.addPhoto:
                Intent intent = new Intent(this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 9917);
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
        plateNumber = findViewById(R.id.plateNumber);
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
        dataModel.setWheelNumbers(wheelNumbersList);
        dataModel.setEliminationDate(eliminationDate.getText().toString());
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
            }
            else if (requestCode == 9917) {
                String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                uploadFileInFireBaseStorage(Uri.fromFile(new File(filePath)));
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myitem = items.valueAt(i);
                        sb.append(myitem.getValue());
                        sb.append("\n");
                    }
                    plateNumber.setText(handleText(sb.toString()));
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadFileInFireBaseStorage (Uri uri){
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
                    dataModel.setPhoto(downloadUri.toString());
                    ivPhoto.setVisibility(View.VISIBLE);
                    Glide.with(AddingActivityModer12.this) //Takes the context
                            .asBitmap()  //Tells glide that it is a bitmap
                            .load(downloadUri)
                            .apply(new RequestOptions()
                                    .override(900, 600)
                                    .centerCrop()
                                    .placeholder(R.drawable.progress_animation))
                            .into(ivPhoto);
                } else {
                    // Handle failures
                    // ...
                }
                progressDialog.cancel();
            }
        });
    }

    private String handleText(String text) {
        text = text.toUpperCase();
        //the 0, 4 and 5 position must have letters, but it can't be 0
        if (text.charAt(0) == '0') {
            text = text.substring(0,0)+'O'+text.substring(1);
        }
        if (text.charAt(4) == '0') {
            text = text.substring(0,4)+'O'+text.substring(5);
        }
        if (text.charAt(5) == '0') {
            text = text.substring(0,5)+'O'+text.substring(6);
        }
        //positions 1, 2, and 3 must have numbers
        if (text.charAt(1) == 'O') {
            text = text.substring(0,1)+'0'+text.substring(2);
        }
        if (text.charAt(2) == 'O') {
            text = text.substring(0,2)+'0'+text.substring(3);
        }
        if (text.charAt(3) == 'O') {
            text = text.substring(0,3)+'0'+text.substring(4);
        }
        return text.replace("RUS", "").replace(" ", "");
    }
}
