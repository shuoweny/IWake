package com.COMP900018.finalproject.ui.alarm;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.google.gson.Gson;

public class AlarmSetOffActivity extends AppCompatActivity {
    private AlarmSetBean alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alarm_setoff);
        getSupportActionBar().hide();
        String alarmConfigJson = getIntent().getStringExtra("alarm_config");

        if (alarmConfigJson!=null){
            Gson gson1 = new Gson();
            alarm = gson1.fromJson(alarmConfigJson, AlarmSetBean.class);
            Button snooze = findViewById(R.id.snooze_btn);
            snooze.setOnClickListener(view -> {
                if(CachedRecord.mark==0){
                    CachedRecord.initialiseRecord(alarm);
                }
                CachedRecord.delay(this,alarm);
                finish();
            });
            Button startBtn = findViewById(R.id.awake_btn);
            startBtn.setOnClickListener(view -> {
                CachedRecord.commit = true;
                if(CachedRecord.alarmsRecord == null){
                    CachedRecord.initialiseRecord(alarm);
                }
                CachedRecord.turnToPage(this);
            });
        }
    }


}
