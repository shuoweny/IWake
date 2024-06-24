package com.COMP900018.finalproject.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MotionSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener shakeListener;
    private SwingListener swingListener;
    private static final float SHAKE_THRESHOLD = 15.0f;
    private static final float HEIGHT_THRESHOLD = 5.0f;
    private static final float SPEED_THRESHOLD = 20.0f;
    private static final float SHAKE_THRESHOLD_GRAVITY = 1.7F;
    private static final int SHAKE_SLOP_TIME_MS = 100;
    private float maxY = Float.MIN_VALUE;
    private float minX = Float.MAX_VALUE;
    private float lastX, lastY, lastZ;
    private int shakeCount = 0;
    private long mShakeTimestamp;
    private int swingCount = 0;

    private long lastSwingTime = 0;

    public interface ShakeListener {
        void onShake(int count);
    }

    public interface SwingListener {
        void onSwing(int count);
    }

    public SwingListener getSwingListener() {
        return swingListener;
    }

    public void setSwingListener(SwingListener swingListener) {
        this.swingListener = swingListener;
    }

    public ShakeListener getShakeListener() {
        return shakeListener;
    }

    public void setShakeListener(ShakeListener shakeListener) {
        this.shakeListener = shakeListener;
    }

    public MotionSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void startSensor(){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopSensor(){
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        float x = sensorEvent.values[0];
//        float y = sensorEvent.values[1];
//        float z = sensorEvent.values[2];
//
//        float diff = Math.abs(x - lastX) + Math.abs(y - lastY) + Math.abs(z - lastZ);
//        if (diff > SHAKE_THRESHOLD) {
//            shakeCount++;
//            notifyShakeDetected();
//        }
        float x = sensorEvent.values[0] / SensorManager.GRAVITY_EARTH;
        float y = sensorEvent.values[1] / SensorManager.GRAVITY_EARTH;
        float z = sensorEvent.values[2] / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(x * x + y * y + z * z);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            mShakeTimestamp = now;
            shakeCount++;
            notifyShakeDetected();
        }


//        lastX = x;
//        lastY = y;
//        lastZ = z;
//        if (y > maxY) {
//            maxY = y;
//        }
//
//        if (x < minX) {
//            minX = x;
//        }
//
//        float heightDifference = Math.abs(maxY - minX);
//        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
//        long currentTime = System.currentTimeMillis();
//
//        if (heightDifference > HEIGHT_THRESHOLD && acceleration > SPEED_THRESHOLD) {
//            swingCount++;
//            notifySwingDetected();
//            lastSwingTime = currentTime;
//
//            maxY = Float.MIN_VALUE;
//            minX = Float.MAX_VALUE;
//        }
    }

    private void notifyShakeDetected() {
        if (shakeListener != null) {
            shakeListener.onShake(shakeCount);
        }
    }

    private void notifySwingDetected() {
        if (swingListener != null) {
            swingListener.onSwing(swingCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}