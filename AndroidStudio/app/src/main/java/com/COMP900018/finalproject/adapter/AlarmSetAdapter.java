package com.COMP900018.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.Utils;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.model.DayOfWeek;

import java.util.LinkedList;
import java.util.List;

public class AlarmSetAdapter extends BaseAdapter {
    private List<AlarmSetBean> alarms;
    private Context mContext;
    private OnItemDeleteListener deleteListener;
    private OnItemEditListener itemEditListener;
    public AlarmSetAdapter(List<AlarmSetBean> alarms, Context mContext) {
        this.alarms = alarms;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int i) {
        return alarms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_info_item,parent,false);
        }
        TextView nameTv = convertView.findViewById(R.id.nameTextView);
        TextView timeTv = convertView.findViewById(R.id.timeTextView);
        ImageButton delBtn = convertView.findViewById(R.id.delButton);
        Button editBtn = convertView.findViewById(R.id.editBtn);
        if(deleteListener != null){
            delBtn.setOnClickListener(view -> deleteListener.onItemDeleted(position));
        }
        if (itemEditListener != null){
            editBtn.setOnClickListener(view -> itemEditListener.onEditClick(position));
        }
        AlarmSetBean alarm = alarms.get(position);
        boolean[] checkedItems = new boolean[]{false,false,false,false,false,false,false};
        DayOfWeek[] days = DayOfWeek.values();
        for(int i=0;i<days.length;i++){
            LinkedList<DayOfWeek> selectedDays = alarm.getDays() == null?new LinkedList<>():alarm.getDays();
            for(DayOfWeek dow :selectedDays){
                if(dow.equals(days[i])){
                    checkedItems[i] = true;
                }
            }
        }
        TextView daysTV = convertView.findViewById(R.id.daysTV);
        daysTV.setText(Utils.getDaysDescription(checkedItems));

        int[] taskTvs = {R.id.taskName1, R.id.taskName2, R.id.taskName3};

        for(int i=0;i<taskTvs.length;i++){
            TextView taskTv = convertView.findViewById(taskTvs[i]);
            taskTv.setVisibility(View.GONE);
        }
        for(int i=0;i<alarm.getTasks().size();i++){
            TextView taskTv = convertView.findViewById(taskTvs[i]);
            taskTv.setText(alarm.getTasks().get(i).getType().toString());
            taskTv.setVisibility(View.VISIBLE);
        }

        nameTv.setText(alarm.getName()+(position+1));
        String time = String.format("%02d:%02d",alarm.getHours(),alarm.getMinus());
        timeTv.setText(time);


        return convertView;
    }
    public void setDeleteListener(OnItemDeleteListener deleteListener){
        this.deleteListener = deleteListener;
    }
    public void setItemEditListener(OnItemEditListener editListener){
        this.itemEditListener = editListener;
    }


    public interface OnItemDeleteListener{
        void onItemDeleted(int position);
    }
    public interface OnItemEditListener{
        void onEditClick(int position);
    }
}
