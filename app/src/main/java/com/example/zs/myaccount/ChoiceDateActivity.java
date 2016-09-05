package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.view.GregorianLunarCalendarView;

/**
 * 日期选择：选择时间
 */
public class ChoiceDateActivity extends AppCompatActivity {

    private static final String TAG = "ChoiceDateActivity";
    private GregorianLunarCalendarView calendar_start;
    private GregorianLunarCalendarView calendar_end;
    private int year_start;
    private int month_start;
    private int day_start;
    private int year_end;
    private int month_end;
    private int day_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_date);
        getSupportActionBar().hide();

        calendar_start = (GregorianLunarCalendarView) this.findViewById(R.id.calendar_start);
        calendar_end = (GregorianLunarCalendarView) this.findViewById(R.id.calendar_end);

        final Calendar calendar = Calendar.getInstance();
        year_start = calendar.get(Calendar.YEAR);
        month_start = calendar.get(Calendar.MONTH)+1;
        day_start = calendar.get(Calendar.DAY_OF_MONTH);
        calendar_start.init();

        year_end = calendar.get(Calendar.YEAR);
        month_end = calendar.get(Calendar.MONTH)+1;
        day_end = calendar.get(Calendar.DAY_OF_MONTH);
        calendar_end.init(calendar);


    }

    /**
     * 返回到前一个页面，并且销毁当前页面
     */
    public void back(View view){
        finish();
    }

    /**
     * 带上当前页面选择的日期数据回到前一个页面，并且销毁当前页面
     * @param view
     */
    public void complete(View view){
        GregorianLunarCalendarView.CalendarData calendarData_start = calendar_start.getCalendarData();
        Calendar calendar_start = calendarData_start.getCalendar();
        GregorianLunarCalendarView.CalendarData calendarData_end = calendar_end.getCalendarData();
        Calendar calendar_end = calendarData_end.getCalendar();
        String choiceDate =calendar_start.get(Calendar.YEAR) + "年" +
                calendar_start.get(Calendar.MONTH) + "月" +
                calendar_start.get(Calendar.DAY_OF_MONTH)+ "日"+
                "-"+ calendar_end.get(Calendar.YEAR) + "年" +
                calendar_end.get(Calendar.MONTH)+ "月" +
                calendar_end.get(Calendar.DAY_OF_MONTH)+ "日";
        Toast.makeText(getApplicationContext(), choiceDate, Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.putExtra("choiceDate", choiceDate);
        // 设置结果，并进行传送
        this.setResult(200, intent);
        finish();
    }
}
