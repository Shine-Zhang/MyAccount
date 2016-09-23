package com.example.zs.bean;

/**
 * Created by wuqi on 2016/9/6 0006.
 */
public class UserAddCategoryInfo {
    int id;
    int resourceID;
    String categoryName;

    public UserAddCategoryInfo( int id,int resourceIDString,String categoryName) {
        this.categoryName = categoryName;
        this.resourceID = resourceIDString;
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getResourceID() {
        return resourceID;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserAddCategoryInfo{" +
                "categoryName='" + categoryName + '\'' +
                ", id=" + id +
                ", resourceID=" + resourceID +
                '}';
    }
}
