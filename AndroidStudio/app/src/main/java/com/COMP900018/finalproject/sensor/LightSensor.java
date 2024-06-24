package com.COMP900018.finalproject.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class LightSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private OnLightChanged lightChangedListener;
    private float sensorRead;
//    private float threshold = 9000;
//    private boolean completion;

    //init
    public LightSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor= sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }
    public void setOnLightChangedListener(OnLightChanged lightChangedListener){
        this.lightChangedListener = lightChangedListener;
    }

    public void startSensor(){
        //change if more accurate readings needed
        if (lightSensor != null) {
            System.out.println("I am worked");
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("LightSensor", "Light sensor not available on this device.");
        }
    }

    public void stopSensor(){
        sensorManager.unregisterListener(this,lightSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println(sensorEvent.values[0]);
        sensorRead = sensorEvent.values[0];
        if(lightChangedListener != null){
            lightChangedListener.change(sensorRead);
        }


//
//        if(sensorRead >= threshold){
//            completion = true;
//        }
//        else {
//
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public float getSensorRead() {
        Log.d("Sensor", "Read: " + sensorRead);
        return sensorRead;

    }

    public interface OnLightChanged{
        void change(float value);
    }


}
