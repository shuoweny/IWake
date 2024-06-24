package com.COMP900018.finalproject.ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.model.TaskType;
import com.COMP900018.finalproject.ui.alarm.CachedRecord;
import com.COMP900018.finalproject.ui.privacy.PrivacyRequest;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    private ImageView picture;
    private File currentImageFile = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.camera);
        Button takeBtn = findViewById(R.id.startTask);
        takeBtn.setVisibility(View.VISIBLE);
        takeBtn.setOnClickListener(view -> {startTakePhoto();});
        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(view -> {
            CachedRecord.doneTheWork(TaskType.CAMERATASK);
            CachedRecord.turnToPage(this);
            finish();
        });
        picture = findViewById(R.id.picture);
    }
    private void startTakePhoto(){
        // 检查相机权限
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Request Camera privacy")
                    .setMessage("You should open the Camera privacy");
            builder.setNegativeButton("No", ((dialogInterface, i) -> {}));
            builder. setPositiveButton("Yes", ((dialogInterface, i) -> {
                PrivacyRequest.requestOpenCamera(this);
            }));
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getURI());
            startActivityForResult(takePictureIntent, Activity.DEFAULT_KEYS_DIALER);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Activity.DEFAULT_KEYS_DIALER && resultCode == RESULT_OK) {
            // 处理拍照成功后的操作
            picture.setImageURI(Uri.fromFile(currentImageFile));
            //findViewById(R.id.saveBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.nextBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.startTask).setVisibility(View.GONE);
        }
    }

    public Uri getURI(){
        File dir = getExternalFilesDir("pictures");
        if(dir.exists()){
            dir.mkdirs();
        }
        currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
        if(!currentImageFile.exists()){
            try {
                currentImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri imageUri = null;
        if (Build.VERSION.SDK_INT>=24)
        {
            imageUri = FileProvider.getUriForFile(CameraActivity.this,getPackageName() + ".fileprovider",currentImageFile);
        }
        else
        {
            imageUri = Uri.fromFile(currentImageFile);
        }

        return imageUri;
    }


}
