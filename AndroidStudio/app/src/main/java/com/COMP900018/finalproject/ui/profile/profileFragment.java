package com.COMP900018.finalproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.data.AvatarTrans;
import com.COMP900018.finalproject.data.DatabaseApi;
import com.COMP900018.finalproject.data.UserSchema;
import com.COMP900018.finalproject.databinding.FragmentProfileBinding;
import com.COMP900018.finalproject.model.AlarmSetBean;
import com.COMP900018.finalproject.ui.alarm.AddAlarmFragment;
import com.COMP900018.finalproject.ui.alarm.AlarmListFragment;
import com.COMP900018.finalproject.ui.avartar.AvatarSelectActivity;
import com.COMP900018.finalproject.ui.gallery.GalleryActivity;
import com.COMP900018.finalproject.ui.login.LoginActivity;
import com.COMP900018.finalproject.ui.privacy.PrivacyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profileFragment extends Fragment {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentProfileBinding binding;
    private AlarmListFragment alarmListFragment;
    private  View root;
    private EditText delete_email;
    private EditText delete_password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        FirebaseUser user = auth.getCurrentUser();
        String uID = user.getUid();
        TextView username = root.findViewById(R.id.userName);
        TextView email = root.findViewById(R.id.email);
        TextView level = root.findViewById(R.id.levelNumber);
        ImageView level_bar = root.findViewById(R.id.level);
        ImageView level_bar_background = root.findViewById(R.id.imageView);
        int background_width = level_bar_background.getLayoutParams().width;
        TextView level_points = root.findViewById(R.id.levelPoints);
        TextView log_out = root.findViewById(R.id.logOut);
        TextView delete_account = root.findViewById(R.id.deleteAccount);
        ImageView editAvatar = root.findViewById(R.id.editAvatar);
        TextView privacySetting = root.findViewById(R.id.privacy_setting);
        ConstraintLayout gallery = root.findViewById(R.id.galleryContainer);
        ImageView avatar = root.findViewById(R.id.avatar);
        TextView user_title  = root.findViewById(R.id.title);

        delete_email = root.findViewById(R.id.username_confirm);
        delete_password = root.findViewById(R.id.password);
        Button confirm_button = root.findViewById(R.id.delete_button);
        Button cancel_button = root.findViewById(R.id.cancel_delete_button);
        LinearLayout confirm_delete = root.findViewById(R.id.confirm_delete);


        alarmListFragment = new AlarmListFragment();
        alarmListFragment.setClickAddListener(this::clickAdd);
        alarmListFragment.setClickEditListener(this::clickEdit);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_alarms,alarmListFragment)
                .commit();



        db.collection("users").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserSchema user = documentSnapshot.toObject(UserSchema.class);
                username.setText(user.getFirstName() +" "+user.getLastName());
                email.setText(user.getEmail());
                level.setText(String.valueOf(user.getLevel()));
                avatar.setImageResource(AvatarTrans.getInstance().urlToId(user.getAvatarUrl()));
                ViewGroup.LayoutParams level_bar_layout = level_bar.getLayoutParams();
                int newWidth = (int) ((user.getPoints()/200f) * background_width);
                if (newWidth == 0){
                    level_bar.setVisibility(View.INVISIBLE);
                }else{
                    level_bar.setVisibility(View.VISIBLE);
                    level_bar_layout.width = newWidth;
                    level_bar.setLayoutParams(level_bar_layout);
                }
                int user_level = user.getLevel();
                if (user_level <= 5){
                    user_title.setText("Rising Star");
                } else if (user_level >5 && user_level <= 15) {
                    user_title.setText("Early Bird");
                } else if (user_level > 15 && user_level <= 29) {
                    user_title.setText("Morning Bee");
                } else {
                    user_title.setText("Time Master");
                }

                level_points.setText(String.valueOf(user.getPoints()));
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null){
                    auth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_delete.setVisibility(View.VISIBLE);
            }
        });


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCredential credential = EmailAuthProvider.
                        getCredential(delete_email.getText().toString().trim(),
                                delete_password.getText().toString().trim());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DatabaseApi.deleteAccount(auth, db);
                            auth.signOut();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            Toast.makeText(getActivity(), "Email or password Wrong.", Toast.LENGTH_SHORT).show();
                            delete_email.setText("");
                            delete_password.setText("");
                        }

                    }
                });
            }
        });

        cancel_button.setOnClickListener(view -> confirm_delete.setVisibility(View.GONE));

        editAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AvatarSelectActivity.class);
            startActivity(intent);
        });

        privacySetting.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PrivacyActivity.class);
            startActivity(intent);
        });
        gallery.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GalleryActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView avatar = root.findViewById(R.id.avatar);
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        avatar.setImageResource(AvatarTrans.getInstance().urlToId(documentSnapshot.getString("avatarUrl")));
                    }
                }
        );

    }


    public void clickAdd() {
        FragmentManager fragmentManager = getChildFragmentManager();
        AddAlarmFragment fragment = new AddAlarmFragment();
        fragment.setConfirmListener(this::onConfirm);
        fragment.setCloseListener(()->{
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        });
        fragmentManager.beginTransaction()
                .add(R.id.alarmFragment,fragment)
                .commit();
    }
    public void onConfirm(AlarmSetBean alarmSetBean, boolean isAdd) {
        alarmListFragment.onConfirm(alarmSetBean,isAdd);
    }



    public void clickEdit(AlarmSetBean alarmSetBean) {
        FragmentManager fragmentManager = getChildFragmentManager();
        AddAlarmFragment fragment = new AddAlarmFragment();
        fragment.setAlarmSetBean(alarmSetBean);
        fragment.setConfirmListener(this::onConfirm);
        fragment.setCloseListener(()->{
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        });
        fragmentManager.beginTransaction()
                .add(R.id.alarmFragment,fragment)
                .commit();
    }
}