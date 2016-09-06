package com.example.zs.bean;

/**
 * Created by wuqi on 2016/9/6 0006.
 */
public class UserAddCategoryInfo {
    int resourceID;
    String categoryName;

    public UserAddCategoryInfo( int resourceIDString,String categoryName) {
        this.categoryName = categoryName;
        this.resourceID = resourceID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getResourceID() {
        return resourceID;
    }

    @Override
    public String toString() {
        return "UserAddCategoryInfo{" +
                "categoryName='" + categoryName + '\'' +
                ", resourceID=" + resourceID +
                '}';
    }
}
