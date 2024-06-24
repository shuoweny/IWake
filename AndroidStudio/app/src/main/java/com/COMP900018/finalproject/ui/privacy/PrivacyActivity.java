package com.COMP900018.finalproject.ui.privacy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.alarm.Alarm;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.ui.alarm.AlarmSetActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.SharedPreferences;
import  android.content.Context;
import android.widget.Switch;
import android.widget.TextView;

public class PrivacyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.privacy_setting);
        ImageView arrow = findViewById(R.id.arrow);

        CheckBox cameraBox= findViewById(R.id.camera_checkBox);
        cameraBox.setChecked(PrivacyRequest.isOnCameraPermission(this));

        arrow.setOnClickListener(v->{
            finish();
        });


        cameraBox.setOnClickListener((view) -> {
            if (!cameraBox.isChecked()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure to turn off the camera");
                builder.setNegativeButton("No", ((dialogInterface, i) -> {
                    cameraBox.setChecked(PrivacyRequest.isOnCameraPermission(this));
                }));
                builder. setPositiveButton("Yes", ((dialogInterface, i) -> {
                    PrivacyRequest.cancelRequest(this);

                }));
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure to turn On the camera");
                builder.setNegativeButton("No", ((dialogInterface, i) -> {
                    cameraBox.setChecked(PrivacyRequest.isOnCameraPermission(this));
                }));
                builder. setPositiveButton("Yes", ((dialogInterface, i) -> {
                    PrivacyRequest.requestOpenCamera(this);

                }));
                AlertDialog alert = builder.create();
                alert.show();


            }

            cameraBox.setChecked(PrivacyRequest.isOnCameraPermission(this));

        });





    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckBox cameraBox= findViewById(R.id.camera_checkBox);
        cameraBox.setChecked(PrivacyRequest.isOnCameraPermission(this));
    }

    protected void onResume() {
        super.onResume();
        CheckBox cameraBox= findViewById(R.id.camera_checkBox);
        cameraBox.setChecked(PrivacyRequest.isOnCameraPermission(this));
    }

}
