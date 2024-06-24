package com.COMP900018.finalproject.ui.shake;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.model.TaskType;
import com.COMP900018.finalproject.ui.alarm.CachedRecord;

public class ShakeGreenActivity extends AppCompatActivity {
    private TextView shakeNumberTextView;
    private int targetShake = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.shake_green);
        shakeNumberTextView = findViewById(R.id.shakeNumberTextView);
        shakeNumberTextView.setText(targetShake+"/"+targetShake);
        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(view -> {
            CachedRecord.doneTheWork(TaskType.MOTIONTASK);
            CachedRecord.turnToPage(this);
            finish();
//            Intent intent = new Intent(ShakeGreenActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
        });
    }
}
