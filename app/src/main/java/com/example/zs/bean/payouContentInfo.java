package com.example.zs.bean;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class PayouContentInfo {
    public int resourceID;
    public int year;
    public  int mouth;
    public int day;
    public  String category;
    public  int money;
    public   String remarks;
    public  String photo;

    public PayouContentInfo(int resourceID,String category,  int year, int mouth,int day, int money, String remarks,String photo ) {
        this.category = category;
        this.day = day;
        this.mouth = mouth;
        this.money = money;
        this.photo = photo;
        this.remarks = remarks;
        this.resourceID = resourceID;
        this.year = year;
    }
}
