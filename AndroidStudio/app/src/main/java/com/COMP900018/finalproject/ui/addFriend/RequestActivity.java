package com.COMP900018.finalproject.ui.addFriend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.adapter.RequestAdapter;
import com.COMP900018.finalproject.adapter.SearchAdapter;
import com.COMP900018.finalproject.data.Friend;
import com.COMP900018.finalproject.data.UserSchema;
import com.COMP900018.finalproject.databinding.ActivityRequestBinding;
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


public class RequestActivity extends AppCompatActivity {


    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityRequestBinding binding;

    private RecyclerView requestRecycler;

    private RecyclerView searchRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = auth.getCurrentUser();
        String uID = user.getUid();

        LinearLayout back_title = findViewById(R.id.title);

        back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        requestRecycler = findViewById(R.id.request_recyclerView);
        requestRecycler.setLayoutManager(new LinearLayoutManager(this));
        requestRecycler.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requestRecycler.getContext(),
                new LinearLayoutManager(this).getOrientation());
        requestRecycler.addItemDecoration(dividerItemDecoration);

        db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                if (user.getPendingFriend().size() == 0){
                    List<Friend> friendList = new ArrayList<>();
                    RequestAdapter adapter = new RequestAdapter(friendList);
                    requestRecycler.setAdapter(adapter);
                }else{
                    db.collection("users").whereIn(FieldPath.documentId(),user.getPendingFriend())
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
                                                System.out.println(document.get("firstName"));
                                                Friend friend = new Friend(documentId, document.get("firstName").toString(), document.get("email").toString(),
                                                        "", "", "",
                                                        document.get("avatarUrl").toString());
                                                friendList.add(friend);
                                            }
                                        }
                                    }
                                    RequestAdapter adapter = new RequestAdapter(friendList);
                                    requestRecycler.setAdapter(adapter);
                                }
                            });
                }

            }
        });

        searchRecycler = findViewById(R.id.search_recyclerView);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(searchRecycler.getContext(),
                new LinearLayoutManager(this).getOrientation());
        searchRecycler.addItemDecoration(dividerItemDecoration2);

        EditText searchEdit = findViewById(R.id.searchEditText);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if the EditText has input and trigger your action
                if (s.length() > 0) {
                    performSearch(searchRecycler, uID, s.toString());
                } else {
                    performSearch(searchRecycler, uID, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this case
            }
        });


    }

    private void performSearch(RecyclerView searchRecycler, String uID, String searchTerm){
        if (searchTerm.length() == 0){
            searchRecycler.setVisibility(View.GONE);
        }else{
            System.out.println(searchTerm);
            searchRecycler.setVisibility(View.VISIBLE);
            db.collection("users").document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        UserSchema user = task.getResult().toObject(UserSchema.class);
                        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<Friend> userList = new ArrayList<>();
                                if (task.isSuccessful()) {
                                    QuerySnapshot result = task.getResult();
                                    if (result != null && !result.isEmpty()){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Process each retrieved document here
                                            String documentId = document.getId();
                                            if (!documentId.equals(uID)
                                                    && document.get("email").toString().toLowerCase().contains(searchTerm.toLowerCase())
                                                    && !user.getFriends().contains(documentId)){
                                                Friend friend = new Friend(documentId, document.get("firstName").toString(),  document.get("email").toString(),
                                                        "", "", "",
                                                        document.get("avatarUrl").toString());
                                                userList.add(friend);
                                            }
                                        }
                                    }
                                }
                                SearchAdapter adapter = new SearchAdapter(userList);
                                searchRecycler.setAdapter(adapter);
                            }
                        });
                    }

                }
            });
        }
    }

}