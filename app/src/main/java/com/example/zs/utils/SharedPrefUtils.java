package com.example.zs.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/30.
 */
public class SharedPrefUtils {
    //将key和 value组成键值对保存到sp文件中
    public static void saveStringToCache(String key, String value, Context context){

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    //从sp文件中，根据key值取出value
    public static String getStringFromCache(String key, Context context){

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String string = sp.getString(key,"");
        return string;
    }

}
