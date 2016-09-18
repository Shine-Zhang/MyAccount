package com.example.zs.myaccount;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.zs.bean.WishInfo;
import com.example.zs.dao.CompleteWishDAO;
import com.example.zs.dao.OnGoingWishDao;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class WishDBTest extends AndroidTestCase{

    private static final String TAG = "WishDBTest";

    public void testAddOnGoingWish(){
        OnGoingWishDao dao = new OnGoingWishDao(getContext());
        for(int i =0;i<5;i++){
            WishInfo wishInfo = new WishInfo(2016,9,8,"我的愿望"+i,"备注"+i,"100.00","0");
            dao.addOnGoingWishInfo(wishInfo);
        }

    }

    public void testDeleteOnGoingWish(){
        OnGoingWishDao dao = new OnGoingWishDao(getContext());
        dao.deleteOnGoingWishInfo(1);
    }

    public void testupdateOnGoingWish(){
        OnGoingWishDao dao = new OnGoingWishDao(getContext());
        WishInfo wishInfo = new WishInfo(2016,9,3,"我的愿望发呆","备注发呆","100.00","0");
        //dao.updateOnGoingWishInfo(wishInfo,3);
    }

    public void testGetOnGoingWishNumber(){
        OnGoingWishDao dao = new OnGoingWishDao(getContext());
        int allOnGoingWishNumber = dao.getAllOnGoingWishNumber();
        assertTrue(allOnGoingWishNumber==4);
    }

    public void testgetAllOnGoingWishInfo(){
        OnGoingWishDao dao = new OnGoingWishDao(getContext());
        List<WishInfo> allOnGoingWishInfo = dao.getAllOnGoingWishInfo();
        for (WishInfo info: allOnGoingWishInfo) {
            Log.i(TAG,info.toString());
        }
    }

    public void testAddCompleteWish(){
        CompleteWishDAO dao = new CompleteWishDAO(getContext());
        for(int i =0;i<5;i++){
            WishInfo wishInfo = new WishInfo(2016,9,8,"我的愿望"+i,"备注"+i,"100.00","0");
            dao.addCompleteWishInfo(wishInfo);
        }
    }

    public void testGeCompleteWishNumber(){
        CompleteWishDAO dao = new CompleteWishDAO(getContext());
        int allCompleteWishNumber = dao.getAllCompleteWishNumber();
        Log.i(TAG,"allCompleteWishNumber="+allCompleteWishNumber);
        assertTrue(allCompleteWishNumber==5);
    }

    public void testgetAllCompleteWishInfo(){
        CompleteWishDAO dao = new CompleteWishDAO(getContext());
        List<WishInfo> allCompleteWishInfo = dao.getAllCompleteWishInfo();
        for (WishInfo info: allCompleteWishInfo) {
            Log.i(TAG,info.toString());
        }
    }

}
