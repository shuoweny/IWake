package com.COMP900018.finalproject.ui.alarm;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.adapter.AlarmSetAdapter;
import com.COMP900018.finalproject.alarm.Alarm;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.ui.alarm.AlarmSetActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class AlarmListFragment extends Fragment {
    private AlarmSetAdapter alarmSetAdapter;
    private List<AlarmSetBean> alarms = new ArrayList<>();
    private View view;
    private ClickAddListener clickAddListener;
    private ClickEditListener clickEditListener;
    public void setClickEditListener(ClickEditListener clickEditListener){
        this.clickEditListener = clickEditListener;
    }
    public void setClickAddListener(ClickAddListener clickAddListener){
        this.clickAddListener = clickAddListener;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_items, container, false);


        alarms = DataIO.getAlarms(this.getContext());
        if (alarms.size() == 0){
            TextView emptyTv = view.findViewById(R.id.emptyTv);
            emptyTv.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.VISIBLE);
            view.findViewById(R.id.alarmContainer).setBackgroundResource(R.drawable.empty_alarm_with_color);
        }
        Button addAlarmBtn = view.findViewById(R.id.addAlarmBtn);
        addAlarmBtn.setOnClickListener(view -> {
            if(clickAddListener!=null){
                clickAddListener.clickAdd();
            }
        });

        alarmSetAdapter = new AlarmSetAdapter(alarms,this.getContext());
        alarmSetAdapter.setDeleteListener(position -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

            builder.setTitle("Confirmation")
                    .setMessage("Are you sure to delete?");
            builder.setNegativeButton("No", ((dialogInterface, i) -> {}));


            builder. setPositiveButton("Yes", ((dialogInterface, i) -> {
                Alarm.cancelAlarm(this.getContext(), alarms.get(position).getId());
                alarms.remove(position);
                TextView emptyTv = view.findViewById(R.id.emptyTv);
                if(alarms.size()==0){
                    emptyTv.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.alarmContainer).setBackgroundResource(R.drawable.empty_alarm_with_color);
                }else{
                    emptyTv.setVisibility(View.GONE);
                    ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FCF4D7"));
                    view.findViewById(R.id.alarmContainer).setBackground(colorDrawable);
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(alarms);
                DataIO.saveJSON(this.getContext(),"alarms.json",json);
                alarmSetAdapter.notifyDataSetChanged();
                if(getActivity() instanceof AlarmSetActivity){
                    ((AlarmSetActivity) getActivity()).notifyAllFragment();
                }
            }));

            AlertDialog alert = builder.create();
            alert.show();



        });

        alarmSetAdapter.setItemEditListener(position -> {
            if(clickEditListener!=null){
                AlarmSetBean alarmSetBean = alarms.get(position);
                clickEditListener.clickEdit(alarmSetBean);
            }
        });
        ListView listView = view.findViewById(R.id.alarmView);

        listView.setAdapter(alarmSetAdapter);


        return view;
    }

    public interface ClickAddListener{
        void clickAdd();
    }
    public interface ClickEditListener{
        void clickEdit(AlarmSetBean alarmSetBean );
    }
    public void onConfirm(AlarmSetBean alarmSetBean, boolean isAdd) {
        if (isAdd){
            alarms.add(alarmSetBean);
        }
        Alarm.cancelAlarm(this.getContext(),alarmSetBean.getId());
        Alarm.scheduleNextAlarm(this.getContext(),alarmSetBean);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(alarms);
        DataIO.saveJSON(this.getContext(),"alarms.json",json);
        TextView emptyTv = view.findViewById(R.id.emptyTv);
        if(alarms.size()==0){
            emptyTv.setVisibility(View.VISIBLE);
            view.findViewById(R.id.alarmContainer).setBackgroundResource(R.drawable.empty_alarm_with_color);
        }else{
            emptyTv.setVisibility(View.GONE);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FCF4D7"));
            view.findViewById(R.id.alarmContainer).setBackground(colorDrawable);
        }
        alarmSetAdapter.notifyDataSetChanged();
    }


}
