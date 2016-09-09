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
    public static SharedPreferences UserInfosp;
    public static SharedPreferences CurUsersp;

    /**
     * 应用创建时调用oncreate（）
     */
    @Override
    public void onCreate() {
        sp = getSharedPreferences("MyAccount", MODE_PRIVATE);
        UserInfosp = getSharedPreferences("config",MODE_PRIVATE);
        CurUsersp = getSharedPreferences("currentUsername",MODE_PRIVATE);
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

    public static void saveIntToSp(String key,int value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key,value);
        edit.commit();
    }
    public static int getIntFromSp(String name){
        return   sp.getInt(name,0);
    }

    /**
     * 保存用户名和密码到SharedPreferences,用户名为键，密码为值
     * @param username  用户名
     * @param password  密码
     */
    public static void saveUserInfoToSp(String username,String password){
        SharedPreferences.Editor edit = UserInfosp.edit();
        edit.putString(username,password);
        edit.commit();
    }

    /**
     * 根据用户名从SharedPreferences获取密码的字符串
     * @param username
     * @return
     */
    public static String getUserInfoFromSp(String username){
        return   UserInfosp.getString(username,"");
    }

    /**
     * 保存当前用户名到sp
     * @param username  用户名
     * @param password  密码
     */
    public static void saveCurUsernaemToSp(String username,String password){
        SharedPreferences.Editor edit = CurUsersp.edit();
        edit.putString(username,password);
        edit.commit();
    }

    /**
     * 获取当前用户名
     * @param username
     * @return
     */
    public static String getCurUsernameFromSp(String username){
        return   CurUsersp.getString(username,"");
    }

}
