package com.example.myapplication.modules.space.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserRW {
    public static String getString(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String value=sp.getString(key,"");
        return value;
    }
    public static boolean getBoolean(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Boolean value=sp.getBoolean(key,false);
        return value;
    }

    public  static boolean writeString(Context context, String key,String value){
        SharedPreferences sp=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString(key,value);
        boolean commit=edit.commit();
        return commit;

    }
    public  static boolean writeBoolean(Context context, String key,boolean value){
        SharedPreferences sp=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.putBoolean(key,value);
        boolean commit=edit.commit();
        return commit;

    }
}

