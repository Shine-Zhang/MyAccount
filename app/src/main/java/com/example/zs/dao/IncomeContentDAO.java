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
    public  int getMoneySum(){
        int sum=0;
        Cursor cursor = db.rawQuery("select money from incomeContent;", null);
        while (cursor.moveToNext()){
            String s = cursor.getString(0);
            int i = Integer.parseInt(s);
            sum+=i;
        }
        return sum;
    }

    /**
     * 此函数为Shine-Zhang添加，用于获取时间轴轴中Group数据
     * @param month
     */
   public float[] getTimeLineGroupData(int month){

       Calendar now = Calendar.getInstance();
       int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
       float[] incomes = new float[dayOfMonth];
       //float[] income = new float[dayOfMonth];
       for(int i=0;i<incomes.length;i++){
           incomes[i]=0;
           // income[i]=0;
       }
       // Log.i("wuta","***************************");
       Cursor costCursor = db.rawQuery("select sum(money),day from incomeContent where month=?group by day order by day DESC",new String[]{month+""});
       while(costCursor.moveToNext()){
           // AccountGroupItemBean =
           int total = costCursor.getInt(costCursor.getColumnIndex("sum(money)"));
           int day = costCursor.getInt(costCursor.getColumnIndex("day"));
           incomes[day-1] = total;
           // Log.i("haha","length "+costs.length+"总支出： "+ costs[day-1]);
       }

       return incomes;
   }


    /**
     * 此函数为Shine-Zhang添加，用于获取时间轴轴中Child数据
     * @param month
     */
    public  ArrayList<ArrayList<AccountChildItemBean>>  getTimeLineIncomeChildData(int month){

        ArrayList<ArrayList<AccountChildItemBean>> children = new ArrayList<ArrayList<AccountChildItemBean>>();
        Cursor dayCursor = db.rawQuery("select day from incomeContent where month=?group by day order by day DESC",new String[]{month+""});
       //  Log.i("nimama","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+dayCursor.getCount());
        ArrayList<AccountChildItemBean> child=null;
        while(dayCursor.moveToNext()){
            child = new ArrayList<AccountChildItemBean>();
            int day = dayCursor.getInt(dayCursor.getColumnIndex("day"));
            Cursor itemsCursor = db.rawQuery("select * from incomeContent where month=? and day=?",new String[]{month+"",day+""});
            AccountChildItemBean item = null;
          //   Log.i("child","***************************************************");
            while(itemsCursor.moveToNext()){
                int id = itemsCursor.getInt(itemsCursor.getColumnIndex("id"));
                int icon = itemsCursor.getInt(itemsCursor.getColumnIndex("resourceID"));
                int dayOfMonth = itemsCursor.getInt(itemsCursor.getColumnIndex("day"));
                String itemDescribe = itemsCursor.getString(itemsCursor.getColumnIndex("category"));
                String howmuch = itemsCursor.getString(itemsCursor.getColumnIndex("money"));
                item = new AccountChildItemBean(month,dayOfMonth,icon,-1,itemDescribe,howmuch,true,id);
                 Log.i("child","hahahahahahah: "+item.toString());
                // Log.i("child","***************************************************");
                child.add(item);
            }
            children.add(child);
        }
        return children;
    }
}
