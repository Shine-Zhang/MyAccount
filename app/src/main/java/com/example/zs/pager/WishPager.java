package com.example.zs.pager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zs.myaccount.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 韦宇 on 2016/9/2.
 * 该类实现愿望页面的业务逻辑。
 * 主要展示用户的愿望，使用的布局文件是 wishpager.xml
 * @author 韦宇
 */

public class WishPager extends BasePager {

    private TextView tv_wishpagermain_addwish;
    private TextView tv_wishpagermain_wishfund;
    private ListView lv_wishpagermain_wish;
    private FragmentManager fragmentManager;

    /**
     * 该方法用于初始化WishPager的页面
     * @return 返回值为页面要显示的view
     */
    @Override
    public View initView() {
        //加载布局文件wishpager.xml
        View view = View.inflate(mActivity, R.layout.wishpager, null);
        fragmentManager = mActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


//        fragmentTransaction.replace(R.id.fl_wishpager_content,new BlankWishFragment(),"blankwish");
//        fragmentTransaction.replace(R.id.fl_wishpager_content,new DetailWishFragment(),"detailwish");
        fragmentTransaction.commit();


        /*//找到页面布局上需要用到的相关控件
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

        //
        return view;*/


        return null;
    }

    /**
     * 该方法用于计算愿望资金
     * 愿望资金的计算公式为：（月收入-月支出）/本月天数  *本月已过天数
     * @return 返回当前日期的愿望资金
     */
    private float calculateWishFund() {
        //获取当前的月收入、月支出

        //获取当前的日期
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //获取当前日期:
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取当前月最后一天,即计算当前月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = dateFormater.format(calendar.getTime());

        //计算愿望资金

        return 0;
    }

    @Override
    public void initData() {

    }
}
