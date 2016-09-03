package com.example.zs.pager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zs.myaccount.R;

/**
 * Created by 韦宇 on 2016/9/2.
 * 该类实现愿望页面的业务逻辑。
 * 主要展示用户的愿望，使用的布局文件是 wishpager_main.xml
 * @author 韦宇
 */

public class WishPager extends BasePager {

    private TextView tv_wishpagermain_addwish;
    private TextView tv_wishpagermain_wishfund;
    private ListView lv_wishpagermain_wish;

    @Override
    public View initView() {
        //加载布局文件wishpager_main.xml
        View view = View.inflate(mActivity, R.layout.wishpager_main, null);
        //找到页面布局上需要用到的相关控件
        tv_wishpagermain_addwish = (TextView) view.findViewById(R.id.tv_wishpagermain_addwish);   //添加愿望
        tv_wishpagermain_wishfund = (TextView) view.findViewById(R.id.tv_wishpagermain_wishfund); //愿望资金
        lv_wishpagermain_wish = (ListView) view.findViewById(R.id.lv_wishpagermain_wish);          //显示所有未完成愿望

        //给tv_wishpagermain_addwish添加点击监听

        return view;
    }

    @Override
    public void initData() {

    }
}
