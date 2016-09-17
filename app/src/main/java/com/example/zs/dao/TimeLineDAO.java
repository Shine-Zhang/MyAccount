package com.example.zs.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.AccountGroupItemBean;
import com.example.zs.dataBase.IncometContentDB;
import com.example.zs.dataBase.PayOutContentDB;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/17 0017.
 * @author Shine-Zhang
 */
public class TimeLineDAO {

    public Context ctx;
    private SQLiteDatabase indb;
    private SQLiteDatabase outdb;
    public TimeLineDAO(Context ctx){
        this.ctx = ctx;
        IncometContentDB incometContentDB = new IncometContentDB(ctx, "IncomeContent.db", null, 1);
        PayOutContentDB  payOutContentDB = new PayOutContentDB(ctx, "PayOutContent.db", null, 1);
        indb = incometContentDB.getReadableDatabase();
        outdb =payOutContentDB.getReadableDatabase();
    }


    public ArrayList<AccountGroupItemBean> getTimeLineGroupData(int month){
        ArrayList<AccountGroupItemBean> group = new ArrayList<AccountGroupItemBean>();
        Calendar now = Calendar.getInstance();
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        float[] costs = new float[dayOfMonth];
        float[] income = new float[dayOfMonth];
        for(int i=0;i<costs.length;i++){
            costs[i]=0;
            income[i]=0;
        }
         Log.i("cacaca","***************************");
        Cursor costCursor = outdb.rawQuery("select sum(money),day from payouContent where month=?group by day order by day DESC",new String[]{month+""});
        while(costCursor.moveToNext()){
            // AccountGroupItemBean =
            int total = costCursor.getInt(costCursor.getColumnIndex("sum(money)"));
            int day = costCursor.getInt(costCursor.getColumnIndex("day"));
            costs[day-1] = total;
           Log.i("cacaca","length "+costs.length+"总支出： "+ costs[day-1]);
        }

        Cursor incomeCursor = indb.rawQuery("select sum(money),day from incomeContent where month=?group by day order by day DESC",new String[]{month+""});
        while(incomeCursor.moveToNext()){

            int total = incomeCursor.getInt(incomeCursor.getColumnIndex("sum(money)"));
            int day = incomeCursor.getInt(incomeCursor.getColumnIndex("day"));
            income[day-1] = total;
             Log.i("cacaca","length "+income.length+"总支出： "+ income[day-1]);
        }
        for(int i = costs.length-1; i>=0 ; i--){
            if(costs[i]!=0||income[i]!=0){
                group.add(new AccountGroupItemBean(i+1,costs[i],income[i]));
            }
        }
        Log.i("cacaca","00000length "+group.size());
        return group;
    }


    public ArrayList<ArrayList<AccountChildItemBean>> getTimeLinePayOutChildData(int month){

        ArrayList<ArrayList<AccountChildItemBean>> children = new ArrayList<ArrayList<AccountChildItemBean>>();
        Cursor dayCursor = outdb.rawQuery("select day from payouContent where month=?group by day order by day DESC",new String[]{month+""});
        // Log.i("nimama","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+dayCursor.getCount());
        ArrayList<AccountChildItemBean> child=null;
        while(dayCursor.moveToNext()){
            child = new ArrayList<AccountChildItemBean>();
            int day = dayCursor.getInt(dayCursor.getColumnIndex("day"));
            Cursor itemsCursor = outdb.rawQuery("select * from payouContent where month=? and day=?",new String[]{month+"",day+""});
            AccountChildItemBean item = null;
            // Log.i("child","***************************************************");
            while(itemsCursor.moveToNext()){
                int id = itemsCursor.getInt(itemsCursor.getColumnIndex("id"));
                int icon = itemsCursor.getInt(itemsCursor.getColumnIndex("resourceID"));
                int dayOfMonth = itemsCursor.getInt(itemsCursor.getColumnIndex("day"));
                String itemDescribe = itemsCursor.getString(itemsCursor.getColumnIndex("category"));
                String howmuch = itemsCursor.getString(itemsCursor.getColumnIndex("money"));
                item = new AccountChildItemBean(month,dayOfMonth,icon,-1,itemDescribe,howmuch,false,id);
                // Log.i("child","hahahahahahah: "+item.toString());
                // Log.i("child","***************************************************");
                child.add(item);
                child.addAll(getIncomeOfDay(month,day));
            }
            children.add(child);
        }

        return children;
    }


    public ArrayList<AccountChildItemBean> getIncomeOfDay(int month,int day){


            ArrayList<AccountChildItemBean> child = new ArrayList<AccountChildItemBean>();
            Cursor itemsCursor = indb.rawQuery("select * from incomeContent where month=? and day=?", new String[]{month + "", day + ""});
             AccountChildItemBean item = null;
            // Log.i("child","***************************************************");
            while (itemsCursor.moveToNext()) {
                int id = itemsCursor.getInt(itemsCursor.getColumnIndex("id"));
                int icon = itemsCursor.getInt(itemsCursor.getColumnIndex("resourceID"));
                int dayOfMonth = itemsCursor.getInt(itemsCursor.getColumnIndex("day"));
                String itemDescribe = itemsCursor.getString(itemsCursor.getColumnIndex("category"));
                String howmuch = itemsCursor.getString(itemsCursor.getColumnIndex("money"));
                item = new AccountChildItemBean(month, dayOfMonth, icon, -1, itemDescribe, howmuch, true, id);
                // Log.i("child","hahahahahahah: "+item.toString());
                // Log.i("child","***************************************************");
                child.add(item);
        }

        return child;
    }
}
