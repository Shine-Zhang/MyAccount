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

    /**
     * 保存字符串到SharedPreferences
     * @param key
     * @param value
     */
    public static void saveStringToSp(String key,String value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    /**
     * 从SharedPreferences获取字符串
     * @param name
     * @return
     */
    public static String getStringFromSp(String name){
        return   sp.getString(name,"");
    }

    /**
     * 保存Boolean值到SharedPreferences
     * @param key
     * @param value
     */
    public static void saveBooleanToSp(String key,Boolean value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    /**
     * 从SharedPreferences获取Boolean值
     * @param name
     * @return
     */
    public static Boolean getBooleanFromSp(String name){
        return   sp.getBoolean(name,false);
    }
}
