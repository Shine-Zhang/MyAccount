package com.example.zs.myaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zs.application.MyAplication;

import java.util.Calendar;

public class RportFormDatePickerActivity extends AppCompatActivity {
    //选择开始时间
    public DatePicker dp_reportformdate_start;
    //结束时间
    public DatePicker dp_reportformdate_end;
    public int currentYear;
    public int currentMonth;
    public int currentDay;
    public RadioGroup rg_reportformdata;
    public int currentWeek;
    public SharedPreferences reportformData;
    public SharedPreferences.Editor edit;
    //触摸调整日期选择器时记录的时间.初始化时都复制为0.
    public int touch_startday = 0;
    public int touch_startmonth = 0;
    public int touch_startyear = 0;

    public int touch_endday = 0 ;
    public int touch_endmonth = 0;
    public int touch_endyear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rport_form_date_picker);
        getSupportActionBar().hide();

        dp_reportformdate_start = (DatePicker) findViewById(R.id.dp_reportformdate_start);
        dp_reportformdate_end = (DatePicker) findViewById(R.id.dp_reportformdate_end);
        rg_reportformdata = (RadioGroup) findViewById(R.id.rg_reportformdata);
        //获取当前时间
        Calendar instance = Calendar.getInstance();
        currentYear = instance.get(Calendar.YEAR);
        currentMonth = instance.get(Calendar.MONTH) ;
        currentDay = instance.get(Calendar.DAY_OF_MONTH);
        currentWeek = instance.get(Calendar.DAY_OF_WEEK) - 1;
        //用来存放选择的日期

        reportformData = getSharedPreferences("reportformData", MODE_PRIVATE);
        edit = reportformData.edit();

        //开始时间
        dp_reportformdate_start.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                touch_startday = dayOfMonth;
                touch_startmonth = monthOfYear + 1;
                touch_startyear = year;
            }
        });

        //结束时间
        dp_reportformdate_end.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                touch_endday = dayOfMonth;
                touch_endmonth = monthOfYear + 1;
                touch_endyear = year;
            }
        });
        Log.i("currentDate", currentYear + "--" + currentMonth + "--" + currentDay);
        //为RadioGroup设置监听
        rg_reportformdata.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_reportformdata_year:
                        dp_reportformdate_start.updateDate(currentYear,0,1);
                        dp_reportformdate_end.updateDate(currentYear,11,31);
                        //保存当前设定好的时间阶段,当没有手动触摸的去调整时间时，这样存储
                        if(touch_startday == 0 && touch_startmonth ==0 && touch_startyear ==0&&
                                touch_endday == 0 && touch_endmonth == 0 && touch_endyear == 0){

                            edit.putInt("reportformTime_fromyear",currentYear);
                            edit.putInt("reportformTime_frommonth",0);
                            edit.putInt("reportformTime_fromday",1);
                            edit.putInt("reportformTime_toyear",currentYear);
                            edit.putInt("reportformTime_tomonth",12);
                            edit.putInt("reportformTime_today",31);
                            edit.commit();
                        }else {//当手动的进行触摸的调整时这样存储
                            edit.putInt("reportformTime_fromyear",touch_startyear);
                            edit.putInt("reportformTime_frommonth",touch_startmonth);
                            edit.putInt("reportformTime_fromday",touch_startday);
                            edit.putInt("reportformTime_toyear",touch_endyear);
                            edit.putInt("reportformTime_tomonth",touch_endmonth);
                            edit.putInt("reportformTime_today",touch_endday);
                            edit.commit();
                        }

                        break;
                    case R.id.rb_reportformdata_month:
                        dp_reportformdate_start.updateDate(currentYear,currentMonth,1);
                        //保存当前设定好的时间阶段
                        if(touch_startday == 0 && touch_startmonth ==0 && touch_startyear ==0&&
                                touch_endday == 0 && touch_endmonth == 0 && touch_endyear == 0){
                            edit.putInt("reportformTime_fromyear",currentYear);
                            edit.putInt("reportformTime_frommonth",currentMonth);
                            edit.putInt("reportformTime_fromday",1);
                            edit.commit();
                            //先判断一下是不是大月，即31天
                            if(currentMonth == 1 || currentMonth == 3|| currentMonth == 5|| currentMonth == 7|| currentMonth == 8
                                    || currentMonth ==10|| currentMonth == 12){
                                dp_reportformdate_end.updateDate(currentYear,currentMonth,31);
                                edit.putInt("reportformTime_toyear",currentYear);
                                edit.putInt("reportformTime_tomonth",currentMonth + 1);
                                edit.putInt("reportformTime_today",31);
                                edit.commit();
                            }else if(currentMonth != 2 ){
                                dp_reportformdate_end.updateDate(currentYear,currentMonth,30);
                                edit.putInt("reportformTime_toyear",currentYear);
                                edit.putInt("reportformTime_tomonth",currentMonth + 1);
                                edit.putInt("reportformTime_today",30);
                                edit.commit();
                            }else if((currentYear % 4 ==0)&&(currentYear % 100 != 0)&&currentMonth == 2){//判断是否为闰年，对2月做相应处理
                                dp_reportformdate_end.updateDate(currentYear,2,29);
                                edit.putInt("reportformTime_toyear",currentYear);
                                edit.putInt("reportformTime_tomonth",2);
                                edit.putInt("reportformTime_today",29);
                                edit.commit();
                            }else{
                                dp_reportformdate_end.updateDate(currentYear,2,28);
                                edit.putInt("reportformTime_toyear",currentYear);
                                edit.putInt("reportformTime_tomonth",2);
                                edit.putInt("reportformTime_today",28);
                                edit.commit();
                            }
                        }else {
                            dp_reportformdate_end.updateDate(currentYear,2,28);
                            edit.putInt("reportformTime_fromyear",touch_startyear);
                            edit.putInt("reportformTime_frommonth",touch_startmonth);
                            edit.putInt("reportformTime_fromday",touch_startday);
                            edit.putInt("reportformTime_toyear",touch_endyear);
                            edit.putInt("reportformTime_tomonth",touch_endmonth);
                            edit.putInt("reportformTime_today",touch_endday);
                            edit.commit();
                        }

                        break;
                    case R.id.rb_reportformdate_week:
                        dp_reportformdate_start.updateDate(currentYear,currentMonth,currentDay - currentWeek + 1);
                        dp_reportformdate_end.updateDate(currentYear,currentMonth,currentDay - currentWeek + 7);
                        //保存当前设定好的时间阶段
                        if(currentMonth == 1 || currentMonth == 3|| currentMonth == 5|| currentMonth == 7|| currentMonth == 8
                                || currentMonth ==10|| currentMonth == 12){
                            edit.putInt("reportformTime_fromyear",currentYear);
                            edit.putInt("reportformTime_frommonth",currentMonth + 1);
                            edit.putInt("reportformTime_fromday",currentDay - currentWeek + 1);
                            edit.putInt("reportformTime_toyear",currentYear);
                            edit.putInt("reportformTime_tomonth",currentMonth + 1);
                            edit.putInt("reportformTime_today",currentDay - currentWeek + 7);
                            edit.commit();
                        }else{
                            edit.putInt("reportformTime_fromyear",touch_startyear);
                            edit.putInt("reportformTime_frommonth",touch_startmonth);
                            edit.putInt("reportformTime_fromday",touch_startday);
                            edit.putInt("reportformTime_toyear",touch_endyear);
                            edit.putInt("reportformTime_tomonth",touch_endmonth);
                            edit.putInt("reportformTime_today",touch_endday);
                            edit.commit();
                        }
                        break;
                }
            }
        });
    }

    //返回
    public void reportFormDataBack(View v){
        //startActivity(new Intent(RportFormDatePickerActivity.this,MainActivity.class));
        finish();
    }

    //完成
    public void reportFormDataComplete(View view){
        //点击完成后，取出保存的时间阶段设置，以便返货报表页后显示时间
        int fromyear = 0;
        int frommonth = 0 ;
        int fromday = 0;
        int toyear = 0;
        int tomonth = 0;
        int today = 0;

        if(reportformData != null){
            //开始时间
            fromyear =  reportformData.getInt("reportformTime_fromyear", currentYear);
            frommonth = reportformData.getInt("reportformTime_frommonth", currentMonth);
            fromday = reportformData.getInt("reportformTime_fromday", currentDay);
            //结束时间
            toyear = reportformData.getInt("reportformTime_toyear",currentYear);
            tomonth = reportformData.getInt("reportformTime_tomonth",currentMonth);
            today = reportformData.getInt("reportformTime_today",currentDay);
        }
        //将时间间段数据带回到报表页
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("date", fromyear + "." + frommonth + "." + fromday + " " + "~"
                + toyear + "." + tomonth + "." + today);
        startActivity(intent);
        finish();
    }
}
