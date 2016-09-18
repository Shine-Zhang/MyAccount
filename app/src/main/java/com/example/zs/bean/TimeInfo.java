package com.example.zs.bean;

/**
 * Created by wuqi on 2016/9/10 0010.
 */
public class TimeInfo {
    int year;
    int month;
    int day;

    public TimeInfo(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return "TimeInfo{" +
                "day=" + day +
                ", year=" + year +
                ", month=" + month +
                '}';
    }
}
