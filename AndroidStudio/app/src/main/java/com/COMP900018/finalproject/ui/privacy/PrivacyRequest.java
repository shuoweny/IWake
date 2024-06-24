package com.COMP900018.finalproject.ui.privacy;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.alarm.Alarm;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.ui.alarm.AlarmSetActivity;
import com.COMP900018.finalproject.ui.camera.CameraActivity;


public class PrivacyRequest {
    public static void cancelRequest(Activity activity){
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent1.setData(uri);
            activity.startActivity(intent1);
    }

    public static boolean isOnCameraPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
        return true;
    }


    public static void requestOpenCamera(Activity activity){
        final int REQUEST_CAMERA_PERMISSION = 101;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                // 应该显示一个解释
                // 此处显示解释对话框，然后再次尝试请求权限
            } else {
                cancelRequest(activity);
            }
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    public static void requestNotificationPermission(Activity activity) {
        if (!NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
            // 通知权限已被用户禁用，显示解释对话框，并引导用户到设置中更改
            new AlertDialog.Builder(activity)
                    .setTitle("Important Notification")
                    .setMessage("You must open Notification for this app。")
                    .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 引导用户到应用的设置页面
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                            // 这些额外的Intent字段是用于Android O及以上版本
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, activity.getApplicationInfo().uid);
                            // 这是用于Android Lollipop至Nougat
                            intent.putExtra("app_package", activity.getPackageName());
                            intent.putExtra("app_uid", activity.getApplicationInfo().uid);
                            activity.startActivity(intent);
                        }
                    })
                    .show();
        } else {

        }
    }
}
