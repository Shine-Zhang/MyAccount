package com.example.zs.pager;

import android.content.Intent;
import android.util.Log;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.zs.myaccount.MyBalanceActivity;
import com.example.zs.myaccount.R;
import com.example.zs.view.OwnerItem;

/**
 * Created by 钟云婷 on 2016/9/2.
 */

public class OwnerPager extends BasePager {

    private static final String TAG = "OwnerPager";
    private OwnerItem oi_ownerpager_login;
    private OwnerItem oi_ownerpager_balance;
    private OwnerItem oi_ownerpager_theme;
    private OwnerItem oi_ownerpager_share;
    private OwnerItem oi_ownerpager_feedback;
    private OwnerItem oi_ownerpager_update;
    private OwnerItem oi_ownerpager_more;

    public OwnerPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        //填充OwnerPager页面
        View OwnerPagerView = View.inflate(mActivity, R.layout.ownerpager_main,null);
        //找出OwnerPagerView中的所有控件
        oi_ownerpager_login = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_login);
        oi_ownerpager_balance = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_balance);
        oi_ownerpager_theme = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_theme);
        oi_ownerpager_share = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_share);
        oi_ownerpager_feedback = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_feedback);
        oi_ownerpager_update = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_update);
        oi_ownerpager_more = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_more);
        Log.i(TAG,"OwnerPager initView!");

        initMyBalance();
        initTheme();
        initShareApp();
        initFeedback();
        initUpdate();
        initMore();

        return OwnerPagerView;
    }

    //初始化more条目，添加点击事件
    private void initMore() {
        oi_ownerpager_more.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //被点击则跳转到more页面
            @Override
            public void onItemClick() {

            }
        });
    }

    //初始化 检测更新 条目，添加点击事件
    private void initUpdate() {
        oi_ownerpager_update.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            @Override
            public void onItemClick() {

            }
        });
    }

    //初始化 意见反馈 条目，添加点击事件
    private void initFeedback() {

        oi_ownerpager_feedback.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            @Override
            public void onItemClick() {

            }
        });
    }

    //初始化 分享App 条目，添加点击事件
    private void initShareApp() {

        oi_ownerpager_share.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            @Override
            public void onItemClick() {

            }
        });
    }

    //初始化 主题皮肤 条目，添加点击事件
    private void initTheme() {

        oi_ownerpager_theme.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            @Override
            public void onItemClick() {

            }
        });
    }

    //初始化 我的余额 条目，添加点击事件
    private void initMyBalance() {
        Log.i(TAG,"initMyBalance coming!");
        oi_ownerpager_balance.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //被点击则跳转到MyBalance页面
            @Override
            public void onItemClick() {
                Log.i(TAG,"initMyBalance.onItemClick");
                mActivity.startActivity(new Intent(mActivity, MyBalanceActivity.class));
            }
        });
    }

    @Override
    public void initData() {

    }
}
