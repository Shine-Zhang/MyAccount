package com.example.zs.pager;

import android.content.Intent;
import android.util.Log;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.zs.myaccount.AboutUsActivity;
import com.example.zs.myaccount.FeedbackActivity;
import com.example.zs.myaccount.InitializeActivity;
import com.example.zs.myaccount.LoginActivity;
import com.example.zs.myaccount.MyBalanceActivity;
import com.example.zs.myaccount.QuestionActivity;
import com.example.zs.myaccount.R;
import com.example.zs.myaccount.ShareAppActivity;
import com.example.zs.view.OwnerItem;

/**
 * Created by 钟云婷 on 2016/9/2.
 */
//该类实现“我的”页面
public class OwnerPager extends BasePager {

    private static final String TAG = "OwnerPager";
    private OwnerItem oi_ownerpager_login;
    private OwnerItem oi_ownerpager_balance;
    private OwnerItem oi_ownerpager_share;
    private OwnerItem oi_ownerpager_feedback;
    private OwnerItem oi_ownerpager_update;
    private OwnerItem oi_ownerpager_initialize;
    private OwnerItem oi_ownerpager_commonQuestion;
    private OwnerItem oi_ownerpager_aboutUs;

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
        oi_ownerpager_share = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_share);
        oi_ownerpager_feedback = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_feedback);
        oi_ownerpager_update = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_update);
        oi_ownerpager_initialize = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_initialize);
        oi_ownerpager_commonQuestion = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_commonQuestion);
        oi_ownerpager_aboutUs = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_aboutUs);

        initLogin();        //登录
        initMyBalance();    //我的余额
        initShareApp();     //分享App
        initClearALl();     //初始化，最初状态
        initFeedback();     //意见反馈
        initUpdate();       //检测更新
        initQuestion();     //常见问题
        initAboutUs();      //关于我们

        return OwnerPagerView;
    }

    //初始化 登录 条目，添加点击事件
    private void initLogin() {
        Log.i(TAG,"initLogin coming!");
        oi_ownerpager_login.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
    }

    //初始化 我的余额 条目，添加点击事件
    private void initMyBalance() {
        Log.i(TAG,"initMyBalance coming!");
        oi_ownerpager_balance.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, MyBalanceActivity.class));
            }
        });
    }

    //初始化 “初始化”条目，添加点击事件
    private void initClearALl() {
        Log.i(TAG,"initClearALl coming!");
        oi_ownerpager_initialize.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, InitializeActivity.class));
            }
        });
    }

    //初始化 常见问题 条目，添加点击事件
    private void initQuestion() {
        Log.i(TAG,"initQuestion coming!");
        oi_ownerpager_commonQuestion.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, QuestionActivity.class));
            }
        });
    }

    //初始化 关于我们 条目，添加点击事件
    private void initAboutUs() {
        Log.i(TAG,"initAboutUs coming!");
        oi_ownerpager_aboutUs.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, AboutUsActivity.class));
            }
        });
    }

    //初始化 检测更新 条目，添加点击事件
    private void initUpdate() {
        Log.i(TAG,"initUpdate coming!");
        oi_ownerpager_update.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                Log.i(TAG,"更新App");
            }
        });
    }

    //初始化 意见反馈 条目，添加点击事件
    private void initFeedback() {
        Log.i(TAG,"initFeedback coming!");
        oi_ownerpager_feedback.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, FeedbackActivity.class));
            }
        });
    }

    //初始化 分享App 条目，添加点击事件
    private void initShareApp() {
        Log.i(TAG,"initShareApp coming!");
        oi_ownerpager_share.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, ShareAppActivity.class));
            }
        });
    }

    @Override
    public void initData() {

    }
}
