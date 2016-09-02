package com.example.zs.pager;

/**
 * Created by Administrator on 2016/8/23 0023.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * 该类对应的是是Xml中所定义的布局文件ViewPager，该类所实现的是其业务逻辑
 * 同时，该类是新闻客户端主页面两大模块(从视觉上分),的整个上半部分的一个抽取
 */
public class BasePager {

    //该Activity主要用来当作全局的山下文使用
    protected Activity mActivity;

    //该视图的根布局控件
    public View mrootView;

    public BasePager(){

    }

    public BasePager(Activity activity){
        this.mActivity = activity;
        mrootView = initView();
    }

    /**
     * 该函数实现的是初始化布局，通过填充Xml文件，来返回相应的View
     */
     private View initView(){
         //初始化布局,利用子类
        // View view = mActivity.getLayoutInflater().inflate(R.layout.base_pager,null);

	    //初始化布局中的控件

         //这里暂时return null 否则会报错，大家正式开始写的时候返回的就应该是各自页面的布局文件所对应的那个view
        return null;
     }

    /**
     * 该函数实现的是初始化数据
     */
    public void initData(){

    }


}
