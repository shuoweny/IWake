package com.COMP900018.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.COMP900018.finalproject.Auth.AuthAPI;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.databinding.HomeBinding;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.ui.alarm.AlarmSetActivity;
import com.COMP900018.finalproject.ui.alarm.CachedRecord;
import com.COMP900018.finalproject.ui.login.LoginActivity;
import com.COMP900018.finalproject.ui.privacy.PrivacyRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private HomeBinding binding;
    private DatabaseApi db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AuthAPI mauthAPI = new AuthAPI();
        super.onCreate(savedInstanceState);
        this.db = new DatabaseApi(this);
        getSupportActionBar().hide();

        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(CachedRecord.alarmsRecord!=null){
            CachedRecord.turnToPage(this);
            return;
        }
        System.out.println(CachedRecord.isNew);
        if(CachedRecord.isNew){
            Intent intent = new Intent(this, AlarmSetActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.friendsFragment, R.id.profileFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Alarm.setAlarm(this,1000);

    }
    @Override
    protected void onResume() {
        super.onResume();
        PrivacyRequest.requestNotificationPermission(this);
        if(CachedRecord.alarmsRecord!=null){
            CachedRecord.turnToPage(this);
        }
    }

}