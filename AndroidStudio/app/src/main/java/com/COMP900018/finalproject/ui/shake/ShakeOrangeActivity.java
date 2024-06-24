package com.COMP900018.finalproject.ui.shake;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.sensor.MotionSensor;


public class ShakeOrangeActivity extends AppCompatActivity {
    private MotionSensor motionSensor;
    private TextView shakeNumberTextView;
    private int count = 0;
    private int targetShake = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.shake_orange);
        shakeNumberTextView = findViewById(R.id.shakeNumberTextView);
        shakeNumberTextView.setText(0+"/"+targetShake);
        motionSensor = new MotionSensor(this);
        ImageView imageView = findViewById(R.id.centeredImageView);
        Animation shakeAnimate = AnimationUtils.loadAnimation(this,R.anim.shake_anim);
        imageView.startAnimation(shakeAnimate);
        motionSensor.setShakeListener(shake -> {
            count++;
            shakeNumberTextView.setText(count+"/"+targetShake);
            if (count == targetShake){
                Intent intent = new Intent(ShakeOrangeActivity.this, ShakeGreenActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.popup_in, R.anim.fade_in);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        motionSensor.startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionSensor.stopSensor();
    }
}
