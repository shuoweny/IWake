package com.COMP900018.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.model.Recommendation;

import java.util.ArrayList;
import java.util.List;

public class RecommendationAdapter extends BaseAdapter {
    private List<Recommendation> recommendations;
    private Context mContext;
    private ClickAddListener clickAddListener;

    public void setClickAddListener(ClickAddListener clickAddListener){
        this.clickAddListener = clickAddListener;
    }


    public RecommendationAdapter(List<Recommendation> recommendations, Context mContext){
        this.recommendations = recommendations;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return recommendations.size();
    }

    @Override
    public Object getItem(int i) {
        return recommendations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.recommendation_item,parent,false);
        }

        TextView title = convertView.findViewById(R.id.rTitle);
        TextView description = convertView.findViewById(R.id.rDescription);
        Button button = convertView.findViewById(R.id.rAddBtn);

        Recommendation recommendation = recommendations.get(position);
        if(existInAlarms(recommendation)){
            convertView.findViewById(R.id.buttonLayout).setAlpha(0.4f);
            button.setText("Added");
            button.setOnClickListener(null);
           // button.setBackgroundResource(R.color.light_gray);
        }else{
            button.setText("Add To Plan");
            convertView.findViewById(R.id.buttonLayout).setAlpha(1);
            button.setOnClickListener(view -> {
                if (clickAddListener!=null){
                    clickAddListener.add(recommendation.getAlarmSetBean());
                    notifyDataSetChanged();
                }
            });
        }


        title.setText(recommendation.getTitle());
        description.setText(recommendation.getDescription());

        return convertView;
    }

    private boolean existInAlarms(Recommendation recommendation){
        ArrayList<AlarmSetBean> alarms = DataIO.getAlarms(this.mContext);
        int id = recommendation.getAlarmSetBean().getId();
        for(AlarmSetBean alarm: alarms){
            if(alarm.getId() == id){
                return true;
            }
        }
        return false;
    }

    public interface ClickAddListener{
        void add(AlarmSetBean alarm);
    }


}
