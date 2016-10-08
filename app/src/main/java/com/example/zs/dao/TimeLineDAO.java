package com.example.zs.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.AccountGroupItemBean;
import com.example.zs.dataBase.IncometContentDB;
import com.example.zs.dataBase.PayOutContentDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/9/17 0017.
 * @author Shine-Zhang
 */
public class TimeLineDAO {

    public Context ctx;
    private SQLiteDatabase indb;
    private SQLiteDatabase outdb;
    private ArrayList<Cursor> cursorSet = new ArrayList<Cursor>();
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
        int year = now.get(Calendar.YEAR);
        int xMonth = now.get(Calendar.MONTH)+1;
        int length;
        if(xMonth>month){
            length = getWholeDays(year,month);
        }else {
            length = now.get(Calendar.DAY_OF_MONTH);
        }
        float[] costs = new float[length];
        float[] income = new float[length];
        for(int i=0;i<costs.length;i++){
            costs[i]=0;
            income[i]=0;
        }
         Log.i("cacaca","***************************");
        Cursor costCursor = outdb.rawQuery("select sum(money),day from payouContent where month=?group by day order by day DESC",new String[]{month+""});
        cursorSet.add(costCursor);
        while(costCursor.moveToNext()){
            // AccountGroupItemBean =
            int total = costCursor.getInt(costCursor.getColumnIndex("sum(money)"));
            int day = costCursor.getInt(costCursor.getColumnIndex("day"));
            costs[day-1] = total;
           Log.i("cacaca","length "+costs.length+"总支出： "+ costs[day-1]);
        }

        Cursor incomeCursor = indb.rawQuery("select sum(money),day from incomeContent where month=?group by day order by day DESC",new String[]{month+""});
        cursorSet.add(incomeCursor);
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
        TreeSet<Integer> alldays = new TreeSet<>();

        Cursor costDayCursor = outdb.rawQuery("select day from payouContent where month=?group by day order by day DESC",new String[]{month+""});
        cursorSet.add(costDayCursor);
        Cursor incomeDayCursor = indb.rawQuery("select day from incomeContent where month=?group by day order by day DESC",new String[]{month+""});
        cursorSet.add(incomeDayCursor);
        while(costDayCursor.moveToNext()){
            int day = costDayCursor.getInt(costDayCursor.getColumnIndex("day"));
            alldays.add(day);
            //costDayCursor.close();
        }

        while(incomeDayCursor.moveToNext()){
            int day = incomeDayCursor.getInt(incomeDayCursor.getColumnIndex("day"));
            alldays.add(day);
            //incomeDayCursor.close();
        }

        alldays = (TreeSet<Integer>) alldays.descendingSet();
        // Log.i("nimama","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+dayCursor.getCount());
        ArrayList<AccountChildItemBean> child=null;
        for (Integer x:alldays) {
            child = new ArrayList<AccountChildItemBean>();
            Cursor dayCosts = outdb.rawQuery("select * from payouContent where month=? and day=?",new String[]{month+"",x+""});
            cursorSet.add(dayCosts);
            Cursor dayIncomes = indb.rawQuery("select * from incomeContent where month=? and day=?",new String[]{month+"",x+""});
            cursorSet.add(dayIncomes);
            AccountChildItemBean item = null;

            while(dayCosts.moveToNext()){
                int id = dayCosts.getInt(dayCosts.getColumnIndex("id"));
                int icon = dayCosts.getInt(dayCosts.getColumnIndex("resourceID"));
                int dayOfMonth = dayCosts.getInt(dayCosts.getColumnIndex("day"));
                String itemDescribe = dayCosts.getString(dayCosts.getColumnIndex("category"));
                String howmuch = dayCosts.getString(dayCosts.getColumnIndex("money"));
                String photo = dayCosts.getString(dayCosts.getColumnIndex("photo"));
                String remark = dayCosts.getString(dayCosts.getColumnIndex("remarks"));
                item = new AccountChildItemBean(month,dayOfMonth,icon,photo,itemDescribe,howmuch,false,id,remark);
                child.add(item);
            }

            while(dayIncomes.moveToNext()){
                int id = dayIncomes.getInt(dayIncomes.getColumnIndex("id"));
                int icon = dayIncomes.getInt(dayIncomes.getColumnIndex("resourceID"));
                int dayOfMonth = dayIncomes.getInt(dayIncomes.getColumnIndex("day"));
                String itemDescribe = dayIncomes.getString(dayIncomes.getColumnIndex("category"));
                String howmuch = dayIncomes.getString(dayIncomes.getColumnIndex("money"));
                String photo = dayIncomes.getString(dayCosts.getColumnIndex("photo"));
                String remark = dayIncomes.getString(dayCosts.getColumnIndex("remarks"));
                item = new AccountChildItemBean(month,dayOfMonth,icon,photo,itemDescribe,howmuch,true,id,remark);
                child.add(item);
            }
            children.add(child);
        }

        return children;
    }

    public void releaseCursors(){
        
        for(int i=0;i<cursorSet.size();i++){
            cursorSet.get(i).close();
        }
    }

    public int getWholeDays(int year,int month){
        int wholeDays = -1;
        String monthString = null;
        if(month<10){
            monthString = String.format("%2d", month);
        }else{
            monthString = ""+month;
        }
        String strDate = year+"-"+monthString;
        Calendar calendar;
        Date date1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            calendar = new GregorianCalendar();
            date1 = sdf.parse(strDate);
            calendar.setTime(date1); //放入你的日期
            wholeDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return wholeDays;
    }


}
