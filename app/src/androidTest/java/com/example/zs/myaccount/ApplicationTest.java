package com.example.zs.myaccount;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.zs.bean.MyAllCatoryInfo;
import com.example.zs.dao.AllCategoryDAO;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    //测试AllCategoryInfoDAO
    public void testCategory(){
        AllCategoryDAO allCategoryDAO = new AllCategoryDAO(getContext());
        ArrayList<MyAllCatoryInfo> cateoryList = allCategoryDAO.getCateoryList();
        Log.i("--",cateoryList.toString());
    }
}