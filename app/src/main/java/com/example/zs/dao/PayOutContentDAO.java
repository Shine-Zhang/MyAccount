package com.example.zs.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.AccountGroupItemBean;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.dataBase.PayOutContentDB;
import com.example.zs.pager.AccountPager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class PayOutContentDAO {
    public Context ctx;
    private SQLiteDatabase db;

    public PayOutContentDAO(Context ctx) {
        this.ctx = ctx;
        PayOutContentDB payOutContentDB = new PayOutContentDB(ctx, "PayOutContent.db", null, 1);
        db = payOutContentDB.getReadableDatabase();
    }

    /**查询
     * 以集合形式返回表格中所有数据
     * @return
     */
    public ArrayList<PayoutContentInfo> getAllPayoutContentFromDB(){
        ArrayList<PayoutContentInfo> payouContentInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from payouContent", null);
        while (cursor.moveToNext()){
            int anInt = cursor.getInt(0);
            int anInt1 = cursor.getInt(1);
            int anInt2 = cursor.getInt(2);
            int anInt3 = cursor.getInt(3);
            int anInt4 = cursor.getInt(4);
            String string1 = cursor.getString(5);
            String string2 = cursor.getString(6);
            String string3 = cursor.getString(7);
            String string4 = cursor.getString(8);
            PayoutContentInfo payouContentInfo = new PayoutContentInfo(anInt,anInt1, string1, anInt2, anInt3, anInt4, string2, string3, string4);
            payouContentInfos.add(payouContentInfo);
        }
        return payouContentInfos;
    }




    /**
     * 修改时，除id外，其它全部更新下
     * @param payouContentInfo 传入表格当行所有信息
     */
    public void updataPayoutContentDB(int id,PayoutContentInfo payouContentInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("resourceID",payouContentInfo.resourceID);
        contentValues.put("year",payouContentInfo.year);
        contentValues.put("month",payouContentInfo.mouth);
        contentValues.put("day",payouContentInfo.day);
        contentValues.put("category",payouContentInfo.category);
        contentValues.put("money",payouContentInfo.money);
        contentValues.put("remarks",payouContentInfo.remarks);
        contentValues.put("photo",payouContentInfo.photo);
        db.update("payouContent",contentValues,"id=?",new String[]{id+""});

    }

    /**
     * 单个插入到数据库中
     * @param payouContentInfo
     */
    public void addPayoutContentToDB(PayoutContentInfo payouContentInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("resourceID",payouContentInfo.resourceID);
        contentValues.put("year",payouContentInfo.year);
        contentValues.put("month",payouContentInfo.mouth);
        contentValues.put("day",payouContentInfo.day);
        contentValues.put("category",payouContentInfo.category);
        contentValues.put("money",payouContentInfo.money);
        contentValues.put("remarks",payouContentInfo.remarks);
        contentValues.put("photo",payouContentInfo.photo);
        db.insert("payouContent",null,contentValues);
    }
    //单个删除
    public void deletePayoutContentItemFromDB(int id){
        db.delete("payouContent","id=?",new String[]{""+id});
    }
    public  float getMoneySum(){
        float sum=0;
        Cursor cursor = db.rawQuery("select money from payouContent ;", null);
        while (cursor.moveToNext()){
            String s = cursor.getString(0);
            float i = Float.valueOf(s);
            Log.i("--","000");
            sum+=i;
        }
        return sum;
    }
    /**
     * 跳转来的页面携带类别，通过类别找到对应的resourceID
     * @param name
     * @return
     */
    public int getResourceIDFromName(String name){
        Cursor cursor = db.rawQuery("select resourceID from payouContent where category=?", new String[]{name});
        boolean b = cursor.moveToNext();
        int resourceID = cursor.getInt(cursor.getColumnIndex("resourceID"));
        return resourceID;
    }



    /**
     * 用于查询月支出总额
     * @param month
     * @return 月支出
     */
    public String getExpenseForMonth(int month){
        Cursor expenseCursor = db.rawQuery("select sum(money),day from payouContent where month=?",new String[]{month+""});
        expenseCursor.moveToNext();
        String expense_month = expenseCursor.getString(0);
        return expense_month;
    }
    public ArrayList<PayoutContentInfo> getDatePayoutContentFromDB(int year,int month){
        ArrayList<PayoutContentInfo> payouContentInfos = new ArrayList<>();
        Cursor cursor = db.query("payouContent",null, "year=? and month=?", new String[]{year + "", month + ""}, null, null, null);
        //Cursor cursor = db.rawQuery("select * from payouContent ", null);
        while (cursor.moveToNext()){
            int anInt = cursor.getInt(0);
            int anInt1 = cursor.getInt(1);
            int anInt2 = cursor.getInt(2);
            int anInt3 = cursor.getInt(3);
            int anInt4 = cursor.getInt(4);
            String string1 = cursor.getString(5);
            String string2 = cursor.getString(6);
            String string3 = cursor.getString(7);
            String string4 = cursor.getString(8);
            PayoutContentInfo payouContentInfo = new PayoutContentInfo(anInt,anInt1, string1, anInt2, anInt3, anInt4, string2, string3, string4);
            payouContentInfos.add(payouContentInfo);
        }
        return payouContentInfos;
    }

}
