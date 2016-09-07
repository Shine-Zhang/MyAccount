package com.example.zs.bean;

/**
 * Created by wuqi on 2016/9/6 0006.
 * baen存放从数据库表AllCategoryInfoDB中获取的单个Category信息
 */
public class MyAllCatoryInfo {
    int id;
    int resourceID;

    public MyAllCatoryInfo(int id, int resourceId) {
        this.id = id;
        this.resourceID = resourceId;
    }

    public int getId() {
        return id;
    }


    public int getResourceID() {
        return resourceID;
    }


    @Override
    public String toString() {
        return "MyAllCatoryInfo{" +
                "i=" + id +
                ", resourceId='" + resourceID + '\'' +
                '}';
    }
}
