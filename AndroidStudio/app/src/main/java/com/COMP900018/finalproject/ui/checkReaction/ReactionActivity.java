package com.COMP900018.finalproject.ui.checkReaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.adapter.ReactionAdapter;
import com.COMP900018.finalproject.data.Friend;
import com.COMP900018.finalproject.data.UserSchema;
import com.COMP900018.finalproject.databinding.ActivityReactionBinding;
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


public class ReactionActivity extends AppCompatActivity {

    private ActivityReactionBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityReactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = auth.getCurrentUser();
        String uID = user.getUid();

        LinearLayout back_title = findViewById(R.id.title);

        back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent = new Intent(ReactionActivity.this, MainActivity.class);
//                intent.putExtra("fragment", "friends");
//                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.friendsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                if (user.getFriends().size() == 0){
                    List<Friend> friendList = new ArrayList<>();
                    ReactionAdapter adapter = new ReactionAdapter(friendList);
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
                                                System.out.println(document.get("firstName"));
                                                if (! reaction.equals("")){
                                                    Friend friend = new Friend(documentId, document.get("firstName").toString(), "",
                                                            document.get("wakeStatus").toString(), reaction, givenReaction,
                                                            document.get("avatarUrl").toString());
                                                    friendList.add(friend);
                                                }
                                            }
                                        }
                                    }
                                    ReactionAdapter adapter = new ReactionAdapter(friendList);
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                }
            }
        });
    }


}