package com.example.zs.pager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zs.myaccount.R;

import java.util.Calendar;

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

    /**
     * 该方法用于初始化WishPager的页面
     * @return 返回值为页面要显示的view
     */
    @Override
    public View initView() {
        //加载布局文件wishpager_main.xml
        View view = View.inflate(mActivity, R.layout.wishpager_main, null);
        //找到页面布局上需要用到的相关控件
        tv_wishpagermain_addwish = (TextView) view.findViewById(R.id.tv_wishpagermain_addwish);   //添加愿望
        tv_wishpagermain_wishfund = (TextView) view.findViewById(R.id.tv_wishpagermain_wishfund); //愿望资金
        lv_wishpagermain_wish = (ListView) view.findViewById(R.id.lv_wishpagermain_wish);          //显示所有未完成愿望

        //给tv_wishpagermain_addwish添加点击事件
        tv_wishpagermain_addwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击“添加”，跳转到添加愿望的Activity

            }
        });

        //计算愿望资金
        calculateWishFund();
        //tv_wishpagermain_wishfund.setText();

        return view;
    }

    /**
     * 该方法用于计算愿望资金
     * 愿望资金的计算公式为：（月收入-月支出）/本月天数  *本月已过天数
     * @return 返回当前日期的愿望资金
     */
    private float calculateWishFund() {
        //获取当前的月收入、月支出

        //获取当前的日期
       /* Calendar calendar = Calendar.getInstance();
        //取得系统日期:
        // year = c.get(Calendar.YEAR)
        month = calendar.grt(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)*/
        return 0;
    }

    @Override
    public void initData() {

    }
}
