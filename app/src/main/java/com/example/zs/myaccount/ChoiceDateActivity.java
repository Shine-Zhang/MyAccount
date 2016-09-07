package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zs.application.MyAplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.view.GregorianLunarCalendarView;

/**
 * 日期选择：选择时间
 */
public class ChoiceDateActivity extends AppCompatActivity {

    private static final String TAG = "ChoiceDateActivity";
    private GregorianLunarCalendarView gc_choicedateactivity_start;
    private GregorianLunarCalendarView gc_choicedateactivity_end;
    private String tv_id;
    private static int start_year;
    private static int start_month;
    private static int start_day;
    private static int end_year;
    private static int end_month;
    private static int end_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_date);
        getSupportActionBar().hide();

        gc_choicedateactivity_start = (GregorianLunarCalendarView) this.findViewById(R.id.gc_choicedateactivity_start);
        gc_choicedateactivity_end = (GregorianLunarCalendarView) this.findViewById(R.id.gc_choicedateactivity_end);

        gc_choicedateactivity_start.init();
        gc_choicedateactivity_end.init();

    }

    /**
     * 返回到前一个页面，并且销毁当前页面
     */
    public void back(View view){
        //将选择的日期数据传递回前一个页面
        Intent cur_intent = new Intent();
        cur_intent.putExtra("choiceDate", "");
        // 设置结果，并进行传送
        this.setResult(200, cur_intent);
        finish();
    }

    /**
     * 带上当前页面选择的日期数据回到前一个页面，并且销毁当前页面
     * @param view
     */
    public void complete(View view){

        //获取在开始时间的日期选择器中选择的日期
        GregorianLunarCalendarView.CalendarData calendarData_start = gc_choicedateactivity_start.getCalendarData();
        Calendar calendar_start = calendarData_start.getCalendar();
        //获取在结束时间的日期选择器中选择的日期
        GregorianLunarCalendarView.CalendarData calendarData_end = gc_choicedateactivity_end.getCalendarData();
        Calendar calendar_end = calendarData_end.getCalendar();

        start_year = calendar_start.get(Calendar.YEAR);
        start_month = calendar_start.get(Calendar.MONTH)+1;
        start_day = calendar_start.get(Calendar.DAY_OF_MONTH);
        end_year = calendar_end.get(Calendar.YEAR);
        end_month = (calendar_end.get(Calendar.MONTH)+1);
        end_day = calendar_end.get(Calendar.DAY_OF_MONTH);

        //为防止出现开始时间大于结束时间的情况，需做如下判断
        if (start_year>end_year) {
            end_year = start_year;
        }
        if (start_month>end_month) {
            end_month = start_month;
        }
        if (start_day>end_day) {
            end_day = start_day;
        }

        //将开始日期和结束日期拼接成一个字符串
        String choiceDate = start_year + "年" + start_month + "月" + start_day + "日"+ "-"+
                end_year + "年" + end_month + "月" + end_day + "日";
        Log.i(TAG,choiceDate);

        //将选择的日期数据保存到sp文件中，用于回显
        MyAplication.saveStringToSp("choiceDate",choiceDate);

        //将选择的日期数据传递回前一个页面
        Intent cur_intent = new Intent();
        cur_intent.putExtra("choiceDate", choiceDate);
        // 设置结果，并进行传送
        this.setResult(200, cur_intent);

        finish();
    }

    public static int getEnd_day() {
        return end_day;
    }

    public static int getEnd_month() {
        return end_month;
    }

    public static int getEnd_year() {
        return end_year;
    }

    public static int getStart_day() {
        return start_day;
    }

    public static int getStart_month() {
        return start_month;
    }

    public static int getStart_year() {
        return start_year;
    }
}
