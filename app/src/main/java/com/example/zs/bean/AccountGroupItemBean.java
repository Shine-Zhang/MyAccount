package com.example.zs.bean;

/**
 * Created by Administrator on 2016/9/4 0004.
 * @author  Shine-Zhang
 *
 * 该类封装了时间轴GroupItem的数据对象
 */
public class AccountGroupItemBean {
    //几号
    int dayOfMonth;
    //一天的总开支
    float totalCosts;
    //一天的总收入
    float totalIncome=0;


    public AccountGroupItemBean(int dayOfMonth, int totalCosts, int totalIncome) {
        this.dayOfMonth = dayOfMonth;
        this.totalCosts = totalCosts;
        this.totalIncome = totalIncome;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public float getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(float totalCosts) {
        this.totalCosts = totalCosts;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }



}
