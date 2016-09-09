package com.example.zs.bean;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class PayouContentInfo {
    public int id;
    public int resourceID;
    public int year;
    public  int mouth;
    public int day;
    public  String category;
    public  String money;
    public   String remarks;
    public  String photo;

    public PayouContentInfo(int id, int resourceID, String category, int year, int mouth, int day, String money, String remarks, String photo ) {
        this.id = id;
        this.category = category;
        this.day = day;
        this.mouth = mouth;
        this.money = money;
        this.photo = photo;
        this.remarks = remarks;
        this.resourceID = resourceID;
        this.year = year;
    }

    @Override
    public String toString() {
        return "PayouContentInfo{" +
                "category='" + category + '\'' +
                ", resourceID=" + resourceID +
                ", year=" + year +
                ", mouth=" + mouth +
                ", day=" + day +
                ", money='" + money + '\'' +
                ", remarks='" + remarks + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
