package com.COMP900018.finalproject.ui.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.adapter.FriendAdapter;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.data.Friend;
import com.COMP900018.finalproject.data.UserSchema;
import com.COMP900018.finalproject.ui.addFriend.RequestActivity;
import com.COMP900018.finalproject.ui.checkReaction.ReactionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class friendsFragment extends Fragment {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseUser user = auth.getCurrentUser();
        String uID = user.getUid();
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);


        EditText searchContent = rootView.findViewById(R.id.searchEditText);
        RecyclerView recyclerView = rootView.findViewById(R.id.friendsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (searchContent.getText().toString().length() == 0){
            fillRecyclerView(recyclerView, uID);
        }


        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if the EditText has input and trigger your action
                if (s.length() > 0) {
                    performSearch(recyclerView, uID, s.toString());
                } else {
                    performSearch(recyclerView, uID, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this case
            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rank", Context.MODE_PRIVATE);



        Button dailyRank = rootView.findViewById(R.id.daily);
        Button historyView = rootView.findViewById(R.id.button3);
        TextView first = rootView.findViewById(R.id.labelFirst);
        TextView second = rootView.findViewById(R.id.labelSecond);
        TextView third = rootView.findViewById(R.id.labelThird);
        ImageView firstAvatar = rootView.findViewById(R.id.circleFirst);
        ImageView secondAvatar = rootView.findViewById(R.id.circleSecond);
        ImageView thirdAvatar = rootView.findViewById(R.id.circleThird);
        TextView myRank = rootView.findViewById(R.id.rankNumber);
        ImageView rankUp = rootView.findViewById(R.id.rankUpIcon);

        dailyRank.setSelected(true);
        DatabaseApi.rankToday(db, first, second, third, myRank, rankUp, sharedPreferences,
                firstAvatar, secondAvatar, thirdAvatar);

        dailyRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseApi.rankToday(db, first, second, third, myRank, rankUp, sharedPreferences,
                        firstAvatar, secondAvatar, thirdAvatar);
                dailyRank.setSelected(true);
                historyView.setSelected(false);
            }
        });

        historyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseApi.rankHistory(db, first, second, third, myRank, rankUp, sharedPreferences,
                        firstAvatar, secondAvatar, thirdAvatar);
                historyView.setSelected(true);
                dailyRank.setSelected(false);

            }
        });



        ImageView addFriend = rootView.findViewById(R.id.addFriend);
        ImageView newReaction = rootView.findViewById(R.id.newReaction);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                startActivity(intent);
            }
        });

        db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                if (!user.isNewReaction()){
                    newReaction.setImageResource(R.drawable.notification_off);
                }
                if (user.getFriends().size() != 0){
                    addFriend.setImageResource(R.drawable.request_on);
                }
            }
        });

        newReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReactionActivity.class);
                startActivity(intent);
                db.collection("users").document(uID).update("newReaction", false);
            }
        });


        return rootView;
    }


    public void fillRecyclerView(RecyclerView recyclerView,String uID){
        db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                if (user.getFriends().size() == 0){
                    List<Friend> friendList = new ArrayList<>();
                    FriendAdapter adapter = new FriendAdapter(friendList);
                    recyclerView.setAdapter(adapter);
                }else{
                    db.collection("users").whereIn(FieldPath.documentId(),user.getFriends())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<Friend> friendList = new ArrayList<>();
                                    if (task.isSuccessful()) {
                                        QuerySnapshot result = task.getResult();
                                        if (result != null && !result.isEmpty()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // Process each retrieved document here
                                                String documentId = document.getId();
                                                int index = user.getFriends().indexOf(documentId);
                                                String reaction = user.getReaction().get(index);
                                                String givenReaction = user.getGivenReaction().get(index);
                                                Friend friend = new Friend(documentId, document.get("firstName").toString(), "",
                                                        document.get("wakeStatus").toString(), reaction,
                                                        givenReaction, document.get("avatarUrl").toString());
                                                friendList.add(friend);
                                            }
                                        }
                                    }
                                    FriendAdapter adapter = new FriendAdapter(friendList);
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                }
            }
        });
    }

    public void performSearch(RecyclerView recyclerView,String uID, String searchTerm){
        EditText searchEdit = rootView.findViewById(R.id.searchEditText);
//        String searchTerm = searchEdit.getText().toString();
        if (searchTerm.length() == 0){
            fillRecyclerView(recyclerView, uID);
        }else{
            db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserSchema user = documentSnapshot.toObject(UserSchema.class);
                    if (user.getFriends().size() == 0){
                        List<Friend> friendList = new ArrayList<>();
                        FriendAdapter adapter = new FriendAdapter(friendList);
                        recyclerView.setAdapter(adapter);
                    }else{
                        db.collection("users").whereIn(FieldPath.documentId(),user.getFriends())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List<Friend> friendList = new ArrayList<>();
                                        if (task.isSuccessful()) {
                                            QuerySnapshot result = task.getResult();
                                            if (result != null && !result.isEmpty()){
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    String firstName = document.get("firstName").toString();
                                                    if (firstName.toLowerCase().contains(searchTerm.toLowerCase())){
                                                        String documentId = document.getId();
                                                        int index = user.getFriends().indexOf(documentId);
                                                        String reaction = user.getReaction().get(index);
                                                        String givenReaction = user.getGivenReaction().get(index);
                                                        Friend friend = new Friend(documentId, firstName, "",
                                                                document.get("wakeStatus").toString(), reaction,
                                                                givenReaction, document.get("avatarUrl").toString());
                                                        friendList.add(friend);
                                                    }

                                                }
                                            }
                                        }
                                        FriendAdapter adapter = new FriendAdapter(friendList);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                    }
                }
            });
        }
    }

    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = rootView.findViewById(R.id.friendsRecyclerView);
        fillRecyclerView(recyclerView, auth.getCurrentUser().getUid());
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                ImageView add_friend = rootView.findViewById(R.id.addFriend);
                ImageView newReaction = rootView.findViewById(R.id.newReaction);
                if (user.getPendingFriend().size() == 0){
                    add_friend.setImageResource(R.drawable.add_friend);
                }else {
                    add_friend.setImageResource(R.drawable.request_on);
                }
                if (user.isNewReaction() == false){
                    newReaction.setImageResource(R.drawable.notification_off);
                }else{
                    newReaction.setImageResource(R.drawable.get_reaction);
                }
            }
        });

    }

}
