package com.COMP900018.finalproject.data;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseApi {

    private FirebaseFirestore db;

    public DatabaseApi(AppCompatActivity app){
        FirebaseApp.initializeApp(app);
        this.db = FirebaseFirestore.getInstance();
    }
    public FirebaseFirestore getDb() {
        return db;
    }


    public static void friendsBeat(UserSchema user, FirebaseFirestore db, TextView view){
        System.out.println("jsaioe");
        if (user.getFriends().size() == 0){
            view.setText(String.valueOf(user.getFriendsBeat()));
        }
        for (String uid: user.getFriends()){
            db.collection("users").document(uid).get().
                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (user.getLevel() > Integer.parseInt(documentSnapshot.get("level").toString())) {
                                    user.setFriendsBeat(user.getFriendsBeat() + 1);
                                }
                            }
                            view.setText(String.valueOf(user.getFriendsBeat()));
                        }
                    });
        }

    }

    public static void updateAvatar(String avatarUrl){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).update("avatarUrl", avatarUrl);
    }

    public static void addFriend(String friend_id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                ArrayList<String> friend_list = user.getFriends();
                if (!friend_list.contains(friend_id)){
                ArrayList<String> reaction_list = user.getReaction();
                ArrayList<String> given_reaction_list = user.getGivenReaction();
                friend_list.add(friend_id);
                reaction_list.add("");
                given_reaction_list.add("");
                db.collection("users").document(uid).update("friends", friend_list,
                        "givenReaction", given_reaction_list,
                        "reaction", reaction_list,
                "pendingFriend", FieldValue.arrayRemove(friend_id));
                }

            }
        });

        db.collection("users").document(friend_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                ArrayList<String> friend_list = user.getFriends();
                ArrayList<String> reaction_list = user.getReaction();
                if (!friend_list.contains(uid)){
                ArrayList<String> given_reaction_list = user.getGivenReaction();
                friend_list.add(uid);
                reaction_list.add("");
                given_reaction_list.add("");
                db.collection("users").document(friend_id).update("friends", friend_list,
                        "givenReaction", given_reaction_list,
                        "reaction", reaction_list);
                }

            }
        });
    }

    public static void rejectFriend(String friend_id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).update("pendingFriend", FieldValue.arrayRemove(friend_id));
    }

    public  static void sentRequest(String target_id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String current_uid = auth.getCurrentUser().getUid();
        db.collection("users").document(target_id).update("pendingFriend", FieldValue.arrayUnion(current_uid));
    }


    private static void updateGivenToFriend(UserSchema user, Friend friend, FirebaseFirestore db){
        db.collection("users").document(friend.getuId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    UserSchema friendUser = document.toObject(UserSchema.class);
                    int index = friendUser.getFriends().indexOf(user.getUid());
                    ArrayList<String> newReceive = friendUser.getReaction();
                    newReceive.set(index, friend.getGivenReaction());
                    db.collection("users").document(friendUser.getUid()).update("reaction", newReceive, "newReaction", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });;
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static  void updateReaction(Friend friend, FirebaseFirestore db){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uId = auth.getCurrentUser().getUid();

        db.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    UserSchema user = document.toObject(UserSchema.class);
                    int index = user.getFriends().indexOf(friend.getuId());
                    ArrayList<String> newgiven = user.getGivenReaction();
                    newgiven.set(index, friend.getGivenReaction());

                    db.collection("users").document(user.getUid()).update("givenReaction", newgiven)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });

                    updateGivenToFriend(user, friend, db);

                }
            }
        });



    }

    public static void updatePoints(int point, FirebaseFirestore db){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uID = auth.getCurrentUser().getUid();

        db.collection("users").document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    UserSchema user = document.toObject(UserSchema.class);
                    int currentLevel = user.getLevel();
                    int currentPoints = user.getPoints();
                    int newPoints = (currentPoints + point) % 200;
                    int newLevel = currentLevel + (int) ((currentPoints + point) / 200);

                    db.collection("users").document(user.getUid()).update("level", newLevel, "points", newPoints, "wakeStatus", "Awake")
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });

                }
            }
        });
    }


    public static void updateUserName(String lastName, String firstName,  FirebaseFirestore db){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uID = auth.getCurrentUser().getUid();

        db.collection("users").document(uID).update("lastName", lastName, "firstName", firstName)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public static void deleteAccount(FirebaseAuth auth, FirebaseFirestore db){
        String uID = auth.getCurrentUser().getUid();

        db.collection("users").document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    UserSchema user = documentSnapshot.toObject(UserSchema.class);
                    ArrayList<String> friends = user.getFriends();
                    if (friends.size() != 0){
                        db.collection("users").whereIn(FieldPath.documentId(),friends).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    // Process each retrieved document here
                                                    UserSchema userFriend = document.toObject(UserSchema.class);
                                                    int index = userFriend.getFriends().indexOf(uID);
                                                    ArrayList<String> newReaction = userFriend.getReaction();
                                                    ArrayList<String> newGiven = userFriend.getGivenReaction();
                                                    newReaction.remove(index);
                                                    newGiven.remove(index);
                                                    db.collection("user").document(userFriend.getUid()).
                                                    update("friends", FieldValue.arrayRemove(uID),
                                                            "givenReaction", newGiven
                                                    , "reaction", newReaction);

                                                }

                                            }
                                            db.collection("users").document(uID).delete();
                                        }
                                    });
                    }else {
                        db.collection("users").document(uID).delete();
                    }

                    if (auth.getCurrentUser() != null){
                        auth.getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            // Handle the failure and log the error
                                            Exception e = task.getException();
                                            Log.w(TAG, "Delete failed with error: " + e.getMessage());
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });
                    }
                    }
                }
            });


    }

    public static void rankHistory(FirebaseFirestore db, TextView first, TextView second,
                                   TextView third, TextView myRank, ImageView rankUp,
                                   SharedPreferences sharedPreferences, ImageView circleFirst,
                                   ImageView circleSecond, ImageView circleThird){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        TextView[] views = {first, second, third};
        ImageView[] rankAvatars = {circleFirst,circleSecond,circleThird};

        db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                ArrayList<String> friends = user.getFriends();
                if (friends.size() != 0){
                    db.collection("users").whereIn(FieldPath.documentId(),friends).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    HashMap<String, Integer> rank = new HashMap<>();
                                    HashMap<String, String> avatars = new HashMap<>();
                                    rank.put(user.getFirstName() + ',' + uid, user.getPoints() + user.getLevel() * 1000);
                                    avatars.put(user.getUid(), user.getAvatarUrl());
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Process each retrieved document here
                                            UserSchema userFriend = document.toObject(UserSchema.class);
                                            rank.put(userFriend.getFirstName() + "," + userFriend.getUid(), userFriend.getPoints() + userFriend.getLevel() * 1000);
                                            avatars.put(userFriend.getUid(), userFriend.getAvatarUrl());
                                        }


                                        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(rank.entrySet());
                                        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
                                        System.out.println(entryList);
                                        int rankUser = findIndexByKeyHistory(entryList, user.getFirstName() + ',' + uid);
                                        if (rankUser == -1){
                                            myRank.setText("--");
                                            rankUp.setVisibility(View.INVISIBLE);
                                        }else{
                                            myRank.setText(String.valueOf(rankUser+1));
                                            int oldRank = sharedPreferences.getInt("myRankHistory", Integer.MAX_VALUE);
                                            if (oldRank != -1 && oldRank > rankUser){
                                                rankUp.setVisibility(View.VISIBLE);
                                            }else{
                                                rankUp.setVisibility(View.INVISIBLE);
                                            }
                                            editor.putInt("myRankHistory", rankUser);
                                        }



                                        List<Map.Entry<String, Integer>> topThreeEntries = entryList.subList(0, Math.min(3, entryList.size()));

                                        int friend_size = topThreeEntries.size();
                                        for (int i = 0;i < 3; i++){
                                            if (i < friend_size){
                                                Map.Entry<String, Integer> entry = topThreeEntries.get(i);
                                                String name = entry.getKey().split(",")[0];
                                                String id = entry.getKey().split(",")[1];
                                                rankAvatars[i].setImageResource(AvatarTrans.getInstance().urlToId(avatars.get(id)));
                                                rankAvatars[i].setVisibility(View.VISIBLE);
                                                views[i].setText(name);
                                            }else{
                                                views[i].setText("None");
                                                rankAvatars[i].setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    }
                                }
                            });
                }

            }
        });
    }

    public static void rankToday(FirebaseFirestore db, TextView first, TextView second,
                                 TextView third, TextView myRank, ImageView rankUp,
                                 SharedPreferences sharedPreferences,ImageView circleFirst,
                                 ImageView circleSecond, ImageView circleThird){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        TextView[] views = {first, second, third};
        ImageView[] rankAvatars = {circleFirst,circleSecond,circleThird};
        db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                ArrayList<String> friends = user.getFriends();
                if (friends.size() != 0){
                    db.collection("users").whereIn(FieldPath.documentId(),friends).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    HashMap<String, Date> rank = new HashMap<>();
                                    HashMap<String, String> avatars = new HashMap<>();
                                    if (isToDay(user.getWakeTime())){
                                        rank.put(user.getFirstName() + ',' + uid, user.getWakeTime());
                                        avatars.put(user.getUid(), user.getAvatarUrl());
                                    }
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Process each retrieved document here
                                            UserSchema userFriend = document.toObject(UserSchema.class);
                                            if (isToDay(userFriend.getWakeTime())){
                                                rank.put(userFriend.getFirstName() + "," + userFriend.getUid(), userFriend.getWakeTime());
                                                avatars.put(userFriend.getUid(), userFriend.getAvatarUrl());
                                            }
                                        }


                                        List<Map.Entry<String, Date>> entryList = new ArrayList<>(rank.entrySet());
                                        entryList.sort(Comparator.comparing(Map.Entry::getValue));

                                        int rankUser = findIndexByKeyToday(entryList, user.getFirstName() + ',' + uid);
                                        if (rankUser == -1){
                                            myRank.setText("--");
                                            rankUp.setVisibility(View.INVISIBLE);
                                        }else{
                                            myRank.setText(String.valueOf(rankUser+1));
                                            int oldRank = sharedPreferences.getInt("myRankToday", Integer.MAX_VALUE);
                                            if (oldRank != -1 && oldRank > rankUser){
                                                rankUp.setVisibility(View.VISIBLE);
                                            }else{
                                                rankUp.setVisibility(View.INVISIBLE);
                                            }
                                            editor.putInt("myRankToday", rankUser);
                                            editor.apply();
                                        }



                                        List<Map.Entry<String, Date>> topThreeEntries = entryList.subList(0, Math.min(3, entryList.size()));
                                        int friend_size = topThreeEntries.size();
                                        for (int i = 0;i < 3; i++){
                                            if (i < friend_size){
                                                Map.Entry<String, Date> entry = topThreeEntries.get(i);
                                                String name = entry.getKey().split(",")[0];
                                                String id = entry.getKey().split(",")[1];
                                                rankAvatars[i].setImageResource(AvatarTrans.getInstance().urlToId(avatars.get(id)));
                                                rankAvatars[i].setVisibility(View.VISIBLE);
                                                views[i].setText(name);
                                            }else{
                                                views[i].setText("None");
                                                rankAvatars[i].setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    }
                                }
                            });
                }

            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isToDay(Date target){
        LocalDate targetDate = target.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate nowDate = LocalDate.now(ZoneId.systemDefault());
        return targetDate.equals(nowDate);

    }

    public static int findIndexByKeyHistory(List<Map.Entry<String, Integer>> list, String key) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(key)) {
                return i; // Found the key, return its index
            }
        }
        return -1; // Key not found in the list
    }

    public static int findIndexByKeyToday(List<Map.Entry<String, Date>> list, String key) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(key)) {
                return i; // Found the key, return its index
            }
        }
        return -1; // Key not found in the list
    }

}
