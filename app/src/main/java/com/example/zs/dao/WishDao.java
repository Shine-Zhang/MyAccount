package com.example.zs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zs.bean.WishInfo;
import com.example.zs.dataBase.WishDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class WishDao {

    Context context;
    private final SQLiteDatabase readableDatabase;

    public WishDao(Context context) {
        this.context = context;
        WishDB wishDBHelper = new WishDB(context, "wish.db",null, 1);
        readableDatabase = wishDBHelper.getReadableDatabase();
    }

    //往数据库添加数据
    public long addWishInfo(WishInfo wishInfo){
        ContentValues values = new ContentValues();
        values.put("wishTitle",wishInfo.wishTitle);
        values.put("wishDescription",wishInfo.wishDescription);
        values.put("wishYear",wishInfo.wishYear);
        values.put("wishMonth",wishInfo.wishMonth);
        values.put("wishDay",wishInfo.wishDay);
        values.put("wishFund",wishInfo.wishFund);
        values.put("wishphotoUri",wishInfo.wishphotoUri);
        return readableDatabase.insert("wish",null,values);
    }

    //删除
    public int deleteWishInfo(String wishid){
        return readableDatabase.delete("wish","wishid = ?",new String[]{wishid});
    }

    /*//修改愿望信息
    public int updateWishInfo(String number,int mode){
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        return readableDatabase.update("blacknum",values,"number = ?",new String[]{number});
    }

    //查询所有黑名单
    public List<WishInfo> getAllBlackNumber(){
        List<BlackNumberInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from blacknum;", null);
        while(cursor.moveToNext()){
            String num = cursor.getString(0);
            int mode = cursor.getInt(1);

            BlackNumberInfo  info  = new BlackNumberInfo();
            info.number = num;
            info.mode = mode;
            list.add(info);
        }

        return list;
    }*/


}
