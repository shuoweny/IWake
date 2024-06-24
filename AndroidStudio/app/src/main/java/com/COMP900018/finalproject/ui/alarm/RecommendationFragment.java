package com.COMP900018.finalproject.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.adapter.RecommendationAdapter;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.ui.alarm.AlarmSetActivity;

public class RecommendationFragment extends Fragment {

    private View view;
    private RecommendationAdapter recommendationAdapter;
    private RecommendationAdapter.ClickAddListener clickAddListener;

    public void setClickAddListener(RecommendationAdapter.ClickAddListener clickAddListener){
        this.clickAddListener = clickAddListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recomodation, container, false);
        ListView listView = view.findViewById(R.id.recommendationView);

        recommendationAdapter = new RecommendationAdapter(DataIO.getRecommendation(this.getContext()),
                this.getContext());
        recommendationAdapter.setClickAddListener(clickAddListener);
        listView.setAdapter(recommendationAdapter);
        return view;
    }

    public RecommendationAdapter getRecommendationAdapter(){
        return recommendationAdapter;
    }


}
