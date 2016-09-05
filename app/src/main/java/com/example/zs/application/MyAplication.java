package com.example.zs.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by wuqi on 2016/9/4 0004.
 * 用于创建应用时的初始化一些供全局使用的参数
 *
 */
public class MyAplication extends Application{

    public static SharedPreferences sp;

    /**
     * 应用创建时调用oncreate（）
     */
    @Override
    public void onCreate() {
        sp = getSharedPreferences("MyAccount", MODE_PRIVATE);
        super.onCreate();
    }
    public static void saveStateToSp(String key,String value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }
    public static String getSateFromSp(String name){
        return   sp.getString(name,"");
    }
    public static void saveBooleanToSp(String key,Boolean value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }
    public static Boolean getBooleanFromSp(String name){
        return   sp.getBoolean(name,false);
    }
}
