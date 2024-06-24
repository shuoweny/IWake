package com.COMP900018.finalproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskSetAdapter extends BaseAdapter {
    private List<Task> tasks;
    private Context mContext;
    private ArrayList<Task> selectedTask = new ArrayList<>();

    public TaskSetAdapter(List<Task> tasks, Context mContext, ArrayList<Task> tempTask){
        this.tasks = tasks;
        this.mContext = mContext;
        if (tempTask == null || tasks == null) return;
        for(Task task: tasks){
            for(Task t: tempTask){
                if(task.getType().equals(t.getType())){
                    selectedTask.add(task);
                }
            }
        }

    }


    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ArrayList<Task> getSelectedTasks(){
        return selectedTask;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.task_info_item,parent,false);
        }
        CheckBox radioBtn = convertView.findViewById(R.id.radioButton1);

        CardView cardView = convertView.findViewById(R.id.task_1);
        TextView taskNameTV = convertView.findViewById(R.id.task_name1);
        TextView descriptionTv = convertView.findViewById(R.id.description1);
        TextView pointTv = convertView.findViewById(R.id.point1);
        Task task = tasks.get(position);
        taskNameTV.setText(task.getType().toString());
        descriptionTv.setText(task.getDescription());
        pointTv.setText("+"+task.getPoint()+" PTS");

        for(Task t: selectedTask){
            if (t.getType().equals(task.getType())){
                radioBtn.setChecked(!radioBtn.isChecked());
                String color = radioBtn.isChecked()? "#E8F5C1": "#FFFFFF";
                cardView.setCardBackgroundColor(Color.parseColor(color));
            }
        }
        radioBtn.setOnClickListener(view -> {
            String color = radioBtn.isChecked()? "#E8F5C1": "#FFFFFF";
            cardView.setCardBackgroundColor(Color.parseColor(color));
            if (radioBtn.isChecked()) {
                selectedTask.add(task);
            }else{
                selectedTask.remove(task);
            }
        });
        convertView.setOnClickListener(view -> {
            radioBtn.setChecked(!radioBtn.isChecked());
            String color = radioBtn.isChecked()? "#E8F5C1": "#FFFFFF";
            cardView.setCardBackgroundColor(Color.parseColor(color));
            if (radioBtn.isChecked()) {
                selectedTask.add(task);
            }else{
                selectedTask.remove(task);
            }
        });
        return convertView;
    }

}
