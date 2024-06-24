package com.COMP900018.finalproject.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.ui.alarm.CachedRecord;
import com.COMP900018.finalproject.ui.alarm.RecommendationFragment;
import com.COMP900018.finalproject.data.AvatarTrans;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.data.UserSchema;
import com.COMP900018.finalproject.databinding.FragmentHomeBinding;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class homeFragment extends Fragment {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentHomeBinding binding;

    private View root;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uID = user.getUid();
        TextView points = root.findViewById(R.id.pointsEarned);
        TextView friendsBeat = root.findViewById(R.id.friendsNumber);
        TextView helloMessage = root.findViewById(R.id.welcome_message);
        TextView date = root.findViewById(R.id.date_display);
        TextView averageTime = root.findViewById(R.id.wakeupTime);

        ImageView avatar = root.findViewById(R.id.avatar);
        TextView alarmNumber = root.findViewById(R.id.alarmNumber);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        date.setText(simpleDateFormat.format(calendar.getTime()).toString());

        RecommendationFragment recommendationFragment = new RecommendationFragment();
        recommendationFragment.setClickAddListener((alarm)->{
            ArrayList<AlarmSetBean> alarms = DataIO.getAlarms(this.getContext());
            alarms.add(alarm);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(alarms);
            DataIO.saveJSON(this.getContext(),"alarms.json",json);
        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.recommendationListView,recommendationFragment)
                .commit();
        int alarmsCount = DataIO.getAlarms(this.getContext()).size();
        alarmNumber.setText(String.valueOf(alarmsCount));
        averageTime.setText(CachedRecord.getAverageTimes());



        db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                points.setText(String.valueOf(user.getPoints()));
                helloMessage.setText("Hi, " + user.getFirstName());
                avatar.setImageResource(AvatarTrans.getInstance().urlToId(user.getAvatarUrl()));
                DatabaseApi.friendsBeat(user, db, friendsBeat);

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView avatar = root.findViewById(R.id.avatar);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("avatar", Context.MODE_PRIVATE);
        String avatarUrl = sharedPreferences.getString("userAvatar", "avatar1");
        avatar.setImageResource(AvatarTrans.getInstance().urlToId(avatarUrl));
    }


}