package com.COMP900018.finalproject.data;

import com.COMP900018.finalproject.R;

import java.util.HashMap;

public class AvatarTrans {
    private static AvatarTrans instance;
    private static HashMap<String, Integer> urlToDrawableMap;

    private AvatarTrans(){
        urlToDrawableMap = new HashMap<>();
        urlToDrawableMap.put("avatar1", R.drawable.avatar);
        urlToDrawableMap.put("avatar2", R.drawable.avatar2);
        urlToDrawableMap.put("avatar3", R.drawable.avatar3);
        urlToDrawableMap.put("avatar4", R.drawable.avatar4);
        urlToDrawableMap.put("avatar5", R.drawable.avatar5);
        urlToDrawableMap.put("avatar6", R.drawable.avatar6);
        urlToDrawableMap.put("avatar7", R.drawable.avatar7);
        urlToDrawableMap.put("avatar8", R.drawable.avatar8);
        urlToDrawableMap.put("avatar9", R.drawable.avatar9);
        urlToDrawableMap.put("avatar10", R.drawable.avatar10);
        urlToDrawableMap.put("avatar11", R.drawable.avatar11);
        urlToDrawableMap.put("avatar12", R.drawable.avatar12);
    }

    public static AvatarTrans getInstance(){
        if(instance == null){
            instance = new AvatarTrans();
        }
        return instance;
    }
    public int urlToId(String url){
        if (urlToDrawableMap.containsKey(url)){
        return urlToDrawableMap.get(url);
        }
        return -1;
    }
}
