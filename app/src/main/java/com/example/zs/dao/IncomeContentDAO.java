package com.example.zs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.bean.TimeInfo;
import com.example.zs.dataBase.IncometContentDB;
import com.example.zs.dataBase.PayOutContentDB;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class IncomeContentDAO {
    public Context ctx;
    private SQLiteDatabase db;

    public IncomeContentDAO(Context ctx) {
        this.ctx = ctx;
        IncometContentDB incometContentDB = new IncometContentDB(ctx, "IncomeContent.db", null, 1);
        db = incometContentDB.getReadableDatabase();
    }

    /**查询
     * 以集合形式返回表格中所有数据
     * @return
     */
    public ArrayList<IncomeContentInfo> getAllIncomeContentFromDB(){
        ArrayList<IncomeContentInfo> incomeContentInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from incomeContent", null);
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
            IncomeContentInfo incomeContentInfo = new IncomeContentInfo(anInt,anInt1, string1, anInt2, anInt3, anInt4, string2, string3, string4);
            incomeContentInfos.add(incomeContentInfo);
        }
        return incomeContentInfos;
    }

    /**
     * 修改时，除id外，其它全部更新下
     * @param incomeContentInfo 传入表格当行所有信息
     */
    public void updataIncomeContentDB(int id,IncomeContentInfo incomeContentInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",incomeContentInfo.id);
        contentValues.put("resourceID",incomeContentInfo.resourceID);
        contentValues.put("year",incomeContentInfo.year);
        contentValues.put("month",incomeContentInfo.mouth);
        contentValues.put("day",incomeContentInfo.day);
        contentValues.put("category",incomeContentInfo.category);
        contentValues.put("money",incomeContentInfo.money);
        contentValues.put("remarks",incomeContentInfo.remarks);
        contentValues.put("photo",incomeContentInfo.photo);
        db.update("incomeContent",contentValues,"id=?",new String[]{id+""});

    }

    /**
     * 单个插入到数据库中
     * @param incomeContentInfo
     */
    public void addIncomeContentToDB(IncomeContentInfo incomeContentInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("resourceID",incomeContentInfo.resourceID);
        contentValues.put("year",incomeContentInfo.year);
        contentValues.put("month",incomeContentInfo.mouth);
        contentValues.put("day",incomeContentInfo.day);
        contentValues.put("category",incomeContentInfo.category);
        contentValues.put("money",incomeContentInfo.money);
        contentValues.put("remarks",incomeContentInfo.remarks);
        contentValues.put("photo",incomeContentInfo.photo);
        db.insert("incomeContent",null,contentValues);
    }
    //单个删除
    public void deleteIncomeContentItemFromDB(int id){
        db.delete("incomeContent","id=?",new String[]{""+id});
    }
    public  float getMoneySum(){
        float sum=0;
        Cursor cursor = db.rawQuery("select money from incomeContent;", null);
        while (cursor.moveToNext()){
            String s = cursor.getString(0);
            float i = Integer.parseInt(s);
            sum+=i;
        }
        return sum;
    }


    /**
     * 用于查询月收入总额
     * @param month
     * @return 月收入
     */
    public String getIncomeForMonth(int month){
        Cursor incomeCursor = db.rawQuery("select sum(money),day from incomeContent where month=?",new String[]{month+""});
        incomeCursor.moveToNext();
        String income_month = incomeCursor.getString(0);
        return income_month;
    }
}
