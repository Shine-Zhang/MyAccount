package com.example.zs.myaccount;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.zs.bean.MyAllCatoryInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.dao.AllCategoryDAO;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.dao.PayoutCategoryDAO;

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
    public void testCategory1(){
        PayoutCategoryDAO payoutCategoryDAO = new PayoutCategoryDAO(getContext());
        payoutCategoryDAO.addPayoutCategoryToDB(23,"hhah");
    }
    public void testpayout(){
        PayOutContentDAO payOutContentDAO = new PayOutContentDAO(getContext());
        PayoutContentInfo payouContentInfo = new PayoutContentInfo(1,123, "购物", 2016, 9, 7, "250", "打折买的", "--");
        payOutContentDAO.addPayoutContentToDB(payouContentInfo);
    }
}