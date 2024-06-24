package com.COMP900018.finalproject.ui.alarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.Utils;
import com.COMP900018.finalproject.adapter.TaskSetAdapter;
import com.COMP900018.finalproject.alarm.AlarmSoundService;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.model.DayOfWeek;
import com.COMP900018.finalproject.model.Task;

import java.util.LinkedList;
import java.util.List;

public class AddAlarmFragment extends Fragment {
    private OnCloseListener closeListener;
    private AlarmSetBean alarmSetBean;
    private OnConfirmListener confirmListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_alarm, container,false);


        List<Task> tasks = DataIO.getTasks(this.getContext());

        TaskSetAdapter taskSetAdapter;

        ListView listView =  view.findViewById(R.id.taskList);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        Button repeatBtn = view.findViewById(R.id.repeatBtn);
        Button musicBtn = view.findViewById(R.id.musicBtn);
        String[] daysOfWeek = new String[]{"Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday", "Sunday"};
        boolean[] checkItems = new boolean[]{true, false, false, false,
                false, false, false};

        String[] musics = new String[]{"Alarm1","Alarm2","Alarm3","Alarm4","Alarm5","Alarm6"};

        Button confirmBtn = view.findViewById(R.id.alarmBtn);
        if (alarmSetBean != null){
            confirmBtn.setText("Edit");
            taskSetAdapter  = new TaskSetAdapter(tasks,this.getContext(),alarmSetBean.getTasks());
            timePicker.setHour(alarmSetBean.getHours());
            timePicker.setMinute(alarmSetBean.getMinus());
            for(int i = 0;i<daysOfWeek.length;i++){
                for(int j = 0;j<alarmSetBean.getDays().size();j++){
                    if(daysOfWeek[i] == alarmSetBean.getDays().get(j).toString()){
                        checkItems[i] = true;
                    }
                }
            }
            repeatBtn.setText(Utils.getDaysDescription(checkItems));
            musicBtn.setText(alarmSetBean.getMusic());

        }else{
            taskSetAdapter  = new TaskSetAdapter(tasks,this.getContext(),null);
            musicBtn.setText("alarm1");
        }
        listView.setAdapter(taskSetAdapter);
        if(confirmListener != null){
            confirmBtn.setOnClickListener(view1 -> {
                if(taskSetAdapter.getSelectedTasks().size()<2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder
                                    .setIcon(R.drawable.baseline_info_24)
                                    .setTitle("Limit selection")
                                    .setMessage("You should choose two task");
                    builder.setPositiveButton("OK",(dialog,which)->{});
                    AlertDialog alert = builder.create();
                    alert.show();
                    return;
                }
                if(alarmSetBean==null){
                    AlarmSetBean alarm = new AlarmSetBean("Alarm",
                            taskSetAdapter.getSelectedTasks(),
                            timePicker.getHour(),timePicker.getMinute());
                    LinkedList<DayOfWeek> days = new LinkedList<>();
                    for(int i=0;i<daysOfWeek.length;i++){
                        if(checkItems[i]){
                            days.add(DayOfWeek.valueOf(daysOfWeek[i]));
                        }
                    }
                    alarm.setDays(days);
                    alarm.setMusic(musicBtn.getText().toString());
                    confirmListener.onConfirm(alarm,true);
                }else{
                    LinkedList<DayOfWeek> days = new LinkedList<>();
                    for(int i=0;i<daysOfWeek.length;i++){
                        if(checkItems[i]){
                            days.add(DayOfWeek.valueOf(daysOfWeek[i]));
                        }
                    }
                    alarmSetBean.setDays(days);
                    alarmSetBean.setTasks(taskSetAdapter.getSelectedTasks());
                    alarmSetBean.setHours(timePicker.getHour());
                    alarmSetBean.setMinus(timePicker.getMinute());
                    alarmSetBean.setMusic(musicBtn.getText().toString());
                    //...
                    confirmListener.onConfirm(alarmSetBean,false);
                }
                closeListener.onClose();
            });
        }

        ImageButton closeBtn = view.findViewById(R.id.closeBtn);
        if(closeListener!=null){
            closeBtn.setOnClickListener(view1 -> {
                closeListener.onClose();
            });
        }

        musicBtn.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setIcon(R.drawable.baseline_info_24)
                    .setTitle("Choose a music for your alarm");
            builder.setSingleChoiceItems(musics, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Intent serviceIntent = new Intent(getContext(), AlarmSoundService.class);
                    serviceIntent.putExtra("music",musics[which]);
                    getContext().startService(serviceIntent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getContext().stopService(serviceIntent);
                        }
                    },1000);
                    musicBtn.setText(musics[which]);
                }
            });
            builder.setPositiveButton("Save",((dialogInterface, i) -> {}));
            AlertDialog alert = builder.create();
            alert.show();
        });

        repeatBtn.setOnClickListener(view1 -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setMultiChoiceItems(daysOfWeek, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                    checkItems[which] = isChecked;
                }
            });
            builder.setPositiveButton("Save",(dialog,which) ->{
                repeatBtn.setText(Utils.getDaysDescription(checkItems));
            });
//            builder.setNegativeButton("Cancel",(dialog,which) ->{});
            builder.setTitle("Pick the days for clock");
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        return view;
    }
    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }
    public void setConfirmListener(OnConfirmListener confirmListener){
        this.confirmListener = confirmListener;
    }
    public interface OnCloseListener{
        void onClose();
    }
    public void setAlarmSetBean(AlarmSetBean alarmSetBean){
        this.alarmSetBean = alarmSetBean;
    }
    public interface OnConfirmListener{
        void onConfirm(AlarmSetBean alarmSetBean, boolean isAdd);
    }



}
