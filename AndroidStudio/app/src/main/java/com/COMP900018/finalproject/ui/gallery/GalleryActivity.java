package com.COMP900018.finalproject.ui.gallery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.COMP900018.finalproject.R;
import com.COMP900018.finalproject.localStorage.DataIO;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfie_gallery);
        getSupportActionBar().hide();
        ImageView arrow = findViewById(R.id.arrow);
        GridLayout gridLayout = findViewById(R.id.selfieGalleryGridLayout);

        arrow.setOnClickListener(v->{
            finish();
        });

        boolean paddingImg = false;
        List<File> imageFiles = DataIO.getAllImageFiles();
        if (imageFiles.size() % 2 != 0) { // 检查列表的大小是否为奇数
            imageFiles.add(createEmptyFile());
            paddingImg = true;
        }

        for (File imageFile : imageFiles) {
            ImageView imageView = new ImageView(this);
            if(imageFile.length() == 0&&!paddingImg){
                paddingImg = false;
                continue;
            }

            // 使用图片加载库（如Glide）加载图片到ImageView中
            Glide.with(this)
                    .load(imageFile)
                    .centerCrop()
                    .into(imageView);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 400;
            params.height = 400;
            params.setMargins(10, 20, 10, 20);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            imageView.setLayoutParams(params);

            gridLayout.addView(imageView);
        }


    }
    private File createEmptyFile(){
        File emptyFile = new File("path/to/directory/empty_file.txt");

        try {
           emptyFile.createNewFile();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return emptyFile;
    }
}
