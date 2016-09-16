package com.example.zs.myaccount;

import android.test.AndroidTestCase;

import com.example.zs.dao.PayOutContentDAO;

/**
 * Created by Administrator on 2016/9/16 0016.
 * @author  Shine-Zhang
 */
public class TimeLineDataTest extends AndroidTestCase {

    public void testGroupData(){

        PayOutContentDAO pay = new PayOutContentDAO(getContext());
        pay.getTimeLineData(9);
    }

}
