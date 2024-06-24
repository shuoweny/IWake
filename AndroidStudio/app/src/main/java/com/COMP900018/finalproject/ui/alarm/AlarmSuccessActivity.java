package com.COMP900018.finalproject.ui.alarm;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AlarmSuccessActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_success);
        getSupportActionBar().hide();
        ImageView progress = findViewById(R.id.level);
        Button okBtn = findViewById(R.id.okBtn);
        TextView lightTV = findViewById(R.id.light_text4);
        String msg = "You have earned ${score} pts for today!\nAlmost finishing you goals... "
                .replace("${score}",CachedRecord.mark+"");
        lightTV.setText(msg);

        int newWidthInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                CachedRecord.mark*1.0f / CachedRecord.total * 380,
                getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams layoutParams = progress.getLayoutParams();
        layoutParams.width = newWidthInPixels;
        progress.setLayoutParams(layoutParams);

        okBtn.setOnClickListener(view -> {
            DatabaseApi.updatePoints(CachedRecord.mark,db);
            CachedRecord.addWakeupTime(new Date());
            CachedRecord.clear();
            CachedRecord.turnToPage(this);
        });


    }
}
