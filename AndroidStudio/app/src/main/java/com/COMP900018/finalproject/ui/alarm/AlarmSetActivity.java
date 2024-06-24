package com.COMP900018.finalproject.ui.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.COMP900018.finalproject.MainActivity;
import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.adapter.RecommendationAdapter;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.ui.privacy.PrivacyRequest;

public class AlarmSetActivity extends FragmentActivity
        implements AddAlarmFragment.OnConfirmListener,
        AlarmListFragment.ClickAddListener,
        AlarmListFragment.ClickEditListener,
        RecommendationAdapter.ClickAddListener {
    AlarmListFragment alarmListFragment;
    RecommendationFragment recommendationFragment;

    @Override
    protected void onResume() {
        super.onResume();
        PrivacyRequest.requestNotificationPermission(this);
        if(CachedRecord.alarmsRecord!=null){
            CachedRecord.turnToPage(this);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.on_boarding);

        //getSupportActionBar().hide();
        Button allSetBtn = findViewById(R.id.doneBtn);
        allSetBtn.setOnClickListener(view -> {
            CachedRecord.isNew = false;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        alarmListFragment = new AlarmListFragment();
        alarmListFragment.setClickAddListener(this);
        alarmListFragment.setClickEditListener(this);

        recommendationFragment = new RecommendationFragment();
        recommendationFragment.setClickAddListener(this);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_alarms,alarmListFragment)
                .add(R.id.recommendationView,recommendationFragment)
                .commit();
    }
    @Override
    public void onConfirm(AlarmSetBean alarmSetBean, boolean isAdd) {
       alarmListFragment.onConfirm(alarmSetBean,isAdd);
    }

    @Override
    public void clickAdd() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddAlarmFragment fragment = new AddAlarmFragment();
        fragment.setConfirmListener(this);
        fragment.setCloseListener(()->{
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        });
        fragmentManager.beginTransaction()
                .add(R.id.alarmFragment,fragment)
                .commit();
    }

    @Override
    public void clickEdit(AlarmSetBean alarmSetBean) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddAlarmFragment fragment = new AddAlarmFragment();
        fragment.setAlarmSetBean(alarmSetBean);
        fragment.setConfirmListener(this);
        fragment.setCloseListener(()->{
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        });
        fragmentManager.beginTransaction()
                .add(R.id.alarmFragment,fragment)
                .commit();
    }
    public void notifyAllFragment(){
        recommendationFragment.getRecommendationAdapter().notifyDataSetChanged();
    }
    @Override
    public void add(AlarmSetBean alarm) {
        alarmListFragment.onConfirm(alarm,true);
    }
}