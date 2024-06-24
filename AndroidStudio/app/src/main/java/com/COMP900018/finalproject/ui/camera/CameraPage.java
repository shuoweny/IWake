package com.COMP900018.finalproject.ui.camera;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;

public class CameraPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.camera_task);
        Button button = findViewById(R.id.turnBtn);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
