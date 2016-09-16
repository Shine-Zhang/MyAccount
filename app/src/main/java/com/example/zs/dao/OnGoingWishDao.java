package com.example.zs.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.zs.bean.WishInfo;
import com.example.zs.dataBase.WishDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WEIYU on 2016/9/8.
 * 对未完成愿望的数据表的操作
 * @author 韦宇
 */
public class OnGoingWishDao {

    Context context;
    private final SQLiteDatabase readableDatabase;

    public OnGoingWishDao(Context context) {
        this.context = context;
        WishDB wishDBHelper = new WishDB(context, "wish.db",null, 1);
        readableDatabase = wishDBHelper.getReadableDatabase();
    }

    //往数据库添加数据
    public long addOnGoingWishInfo(WishInfo wishInfo){

        //通知内容观察者数据库变化了
        ContentResolver contentResolver = context.getContentResolver();
        //因为是我们自己的数据发生变化了,所以我们要自定义一个uri进行操作
        Uri uri = Uri.parse("content://com.example.zs.dao.ongoingwish.changed");
        //通知内容观察者数据发生变化了
        contentResolver.notifyChange(uri, null);

        ContentValues values = new ContentValues();
        values.put("wishTitle",wishInfo.wishTitle);
        values.put("wishDescription",wishInfo.wishDescription);
        values.put("wishYear",wishInfo.wishYear);
        values.put("wishMonth",wishInfo.wishMonth);
        values.put("wishDay",wishInfo.wishDay);
        values.put("wishFund",wishInfo.wishFund);
        values.put("wishphotoUri",wishInfo.wishphotoUri);
        return readableDatabase.insert("ongoingwish",null,values);
    }

    //从数据库中删除选中的愿望信息
    public int deleteOnGoingWishInfo(int wishid){
        //通知内容观察者数据库变化了
        ContentResolver contentResolver = context.getContentResolver();
        //因为是我们自己的数据发生变化了,所以我们要自定义一个uri进行操作
        Uri uri = Uri.parse("content://com.example.zs.dao.ongoingwish.changed");
        //通知内容观察者数据发生变化了
        contentResolver.notifyChange(uri, null);

        return readableDatabase.delete("ongoingwish","wishid = ?",new String[]{wishid+""});
    }

    //查询所有未完成愿望的信息
    public List<WishInfo> getAllOnGoingWishInfo(){
        List<WishInfo> list = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from ongoingwish;", null);
        while(cursor.moveToNext()){
            int wishid = cursor.getInt(0);
            int wishYear = cursor.getInt(1);
            int wishMonth = cursor.getInt(2);
            int wishDay = cursor.getInt(3);
            String wishTitle = cursor.getString(4);
            String wishDescription = cursor.getString(5);
            String wishFund = cursor.getString(6);
            String wishphotoUri = cursor.getString(7);

            WishInfo wishInfo = new WishInfo(wishid,wishYear,wishMonth,wishDay,wishTitle,wishDescription,wishFund,wishphotoUri);
            list.add(wishInfo);
        }

        return list;
    }

    //获取所有未完成愿望的数目
    public int getAllOnGoingWishNumber(){
        Cursor cursor = readableDatabase.rawQuery("select count(*) from ongoingwish;", null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }


}
