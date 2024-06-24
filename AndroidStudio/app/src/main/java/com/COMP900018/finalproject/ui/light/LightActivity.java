package com.COMP900018.finalproject.ui.light;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.model.TaskType;
import com.COMP900018.finalproject.sensor.LightSensor;
import com.COMP900018.finalproject.ui.alarm.CachedRecord;
import com.COMP900018.finalproject.ui.privacy.PrivacyActivity;

public class LightActivity extends AppCompatActivity implements LightSensor.OnLightChanged {

    private LightSensor lightSensor;
    private GradientDrawable gradientDrawable;
    private TextView lightMain;
    private TextView lightSec;
    private Animation animation;
    private Button doneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.light_task);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean lightPermission = sharedPreferences.getBoolean("lightPermission", false);


        lightSensor = new LightSensor(this);
        lightSensor.setOnLightChangedListener(this);

        //connect sensor
        lightSensor.startSensor();

        float sensorRead = lightSensor.getSensorRead();
        Log.d("Sensor", "Sensor connected"+sensorRead);
        doneButton = findViewById(R.id.doneButton);
        doneButton.setVisibility(View.GONE);
        doneButton.setOnClickListener(view -> {
            CachedRecord.doneTheWork(TaskType.LIGHTTASK);
            CachedRecord.turnToPage(this);
            finish();
        });
//        } else {
        //Display Alert and direct to Privacy page
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Light Permission Required");
//        builder.setMessage("In order to use the light sensor, you need to grant permission.");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Context context = getApplicationContext();
//                Intent privacyIntent = new Intent(context, PrivacyActivity.class);
//                startActivity(privacyIntent);
//            }
//        });
//        builder.setCancelable(false);
//        builder.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        lightSensor.stopSensor();
    }

    @Override
    public void change(float value) {
        float minValue = 0; // 传感器的最小值
        float maxValue = 500; // 传感器的最大值
        float minBrightness = 0.9F; // 期望的最低亮度
        int maxBrightness = 0; // 期望的最高亮度
        int minRadius = 25;
        int maxRadius = 100;
        int firstThreshold = 50;
        int secondThreshold = 75;
        float firstThresholdValue = maxValue * firstThreshold / 100;
        float secondThresholdValue = maxValue * secondThreshold / 100;


        float brightness = brightness(value, minValue, maxValue, minBrightness, maxBrightness);
        int radius = (int) ((value - minValue) / (maxValue - minValue) * (maxRadius - minRadius) + minRadius);

        ImageView imageView = findViewById(R.id.image_view);
        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
        drawable.setGradientRadius(radius); // Set the radius of the radial gradient
        imageView.setAlpha(brightness);

        if (value >= firstThresholdValue) {
            lightMain = findViewById(R.id.light_text1);
            lightMain.setText("");

            lightSec = findViewById(R.id.light_text2);
            lightSec.setText("More Light");
        }

        if (value>= secondThresholdValue){
            lightSec = findViewById(R.id.light_text2);
            lightSec.setText("Task complete!");
            lightSec.setTextColor(Color.parseColor("#463734"));
            doneButton = findViewById(R.id.doneButton);
            doneButton.setVisibility(View.VISIBLE);
        }
    }

    public static float brightness(float value, float minValue, float maxValue, float minBrightness, float maxBrightness) {
        // Calculate the normalized sensor value.
        float normalizedValue = (value - minValue) / (maxValue - minValue);
        // Calculate the brightness value.
        float brightness = normalizedValue * (maxBrightness - minBrightness) + minBrightness;
        // Clamp the brightness value to the range [0, 1].
        brightness = Math.max(0, Math.min(1, brightness));

        return brightness;
    }
}