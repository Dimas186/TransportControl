package com.example.transportcontrol.handler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import static com.example.transportcontrol.handler.PlateNumberHandler.handleText;

public class OCRHandler {

    public static StringBuilder handle(Intent data, Activity activity) {
        StringBuilder sb = new StringBuilder();
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        Uri resultUri = result.getUri();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), resultUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextRecognizer recognizer = new TextRecognizer.Builder(activity).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = recognizer.detect(frame);
            sb = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                TextBlock myitem = items.valueAt(i);
                sb.append(myitem.getValue());
                sb.append("\n");
            }

        }
        return sb;
    }
}
