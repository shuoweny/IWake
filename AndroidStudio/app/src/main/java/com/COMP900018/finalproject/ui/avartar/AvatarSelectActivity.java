package com.COMP900018.finalproject.ui.avartar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.data.DatabaseApi;

public class AvatarSelectActivity extends AppCompatActivity {
    private ImageView[] avatars = new ImageView[12];

    private ImageView selected;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.avatar_selection);
        saveAvatar();
        for (int i = 0; i < avatars.length; i++) {
            final ImageView avatar = avatars[i];
            avatar.setOnClickListener(v -> selectAvatar(avatar));

        }
        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v->{
            finish();
        });
        ImageView arrow = findViewById(R.id.arrow);
        arrow.setOnClickListener(v->{
            finish();
        });
        Button updateButton = findViewById(R.id.confirm);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idString = selected.getResources().getResourceEntryName(selected.getId());
                DatabaseApi.updateAvatar(idString);
                SharedPreferences sharedPreferences = getSharedPreferences("avatar", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userAvatar", idString);
                editor.apply();
                finish();
            }
        });


    }
    private void saveAvatar(){
        avatars[0] = findViewById(R.id.avatar1);
        avatars[1] = findViewById(R.id.avatar2);
        avatars[2] = findViewById(R.id.avatar3);
        avatars[3] = findViewById(R.id.avatar4);
        avatars[4] = findViewById(R.id.avatar5);
        avatars[5] = findViewById(R.id.avatar6);
        avatars[6] = findViewById(R.id.avatar7);
        avatars[7] = findViewById(R.id.avatar8);
        avatars[8] = findViewById(R.id.avatar9);
        avatars[9] = findViewById(R.id.avatar10);
        avatars[10] = findViewById(R.id.avatar11);
        avatars[11] = findViewById(R.id.avatar12);
        avatars[0].setBackgroundResource(R.drawable.avartar_selector);

    }

    private void selectAvatar(ImageView selectedAvatar) {
        selectedAvatar.setBackgroundResource(R.drawable.avartar_selector);
        selected = selectedAvatar;
        for (ImageView avatar : avatars) {
            if (avatar != selectedAvatar) {
                avatar.setBackground(null);
            }
        }
    }
}
