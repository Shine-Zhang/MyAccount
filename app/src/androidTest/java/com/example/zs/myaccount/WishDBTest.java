package com.example.zs.myaccount;

import android.test.AndroidTestCase;

import com.example.zs.bean.WishInfo;
import com.example.zs.dao.OnGoingWishDao;

/**
 * Created by Administrator on 2016/9/8.
 */
public class WishDBTest extends AndroidTestCase{

    public void testAddOnGoingWish(){
        OnGoingWishDao dao = new OnGoingWishDao(getContext());
        WishInfo wishInfo = new WishInfo(2016,9,8,"我的愿望1","备注1","100.00","0");
        dao.addOnGoingWishInfo(wishInfo);
    }
}
