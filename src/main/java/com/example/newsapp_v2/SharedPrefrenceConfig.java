package com.example.newsapp_v2;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefrenceConfig(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences("com.example.newsapp_v2",Context.MODE_PRIVATE);
    }
    public void writeCategory(String name){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("url",name);
        editor.commit();
    }
    public String getCategory(){
        String name=sharedPreferences.getString("url","");
        return name;
    }
}
