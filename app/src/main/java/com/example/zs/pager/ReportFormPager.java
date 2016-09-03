package com.example.zs.pager;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zs.myaccount.R;

/**
 * 本类是五个pager中的“报表类”，继承至基类BasePager,主要用饼状图的形式分别呈现出各种类型的收入和支出所占的比重
 */
public class ReportFormPager extends BasePager{
    //时间段
    private TextView tv_reportform_time;
    //报表视图
    private View reportformpager_content_view;
    //各项收入或支出所占百分比
    private ListView lv_reportform_percentage;

    @Override
    public View initView() {
        reportformpager_content_view = View.inflate(mActivity, R.layout.reportformpager_content, null);
        tv_reportform_time = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_time);
        lv_reportform_percentage = (ListView) reportformpager_content_view.findViewById(R.id.lv_reportform_percentage);
        return reportformpager_content_view;
    }

    @Override
    public void initData() {

    }
}
