package com.example.zs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zs.bean.PayouContentInfo;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class PayOutContentDAO {
    public Context ctx;
    private SQLiteDatabase db;

    public PayOutContentDAO(Context ctx) {
        this.ctx = ctx;
    }
    public void addPayoutContentToDB(PayouContentInfo payouContentInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("resourceID",payouContentInfo.resourceID);
        contentValues.put("category",payouContentInfo.year);
        contentValues.put("mouth",payouContentInfo.mouth);
        contentValues.put("day",payouContentInfo.day);
        contentValues.put("money",payouContentInfo.money);
        contentValues.put("remarks",payouContentInfo.remarks);
        contentValues.put("photo",payouContentInfo.photo);
        db.insert("payouContent",null,contentValues);
    }
}
