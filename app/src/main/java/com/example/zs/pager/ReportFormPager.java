package com.example.zs.pager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.zs.myaccount.R;

/**
 * 本类是五个pager中的“报表类”，继承至基类BasePager,主要用饼状图的形式分别呈现出各种类型的收入和支出所占的比重
 */
public class ReportFormPager extends BasePager{
    //报表视图
    private View reportformpager_content_view;
    //时间段
    private TextView tv_reportform_time;
    //对应”收入“和”支出“按钮的RadioGroup
    private RadioGroup rg_reportform;
    //对应“收入”和“支出”的报表详情
    private ViewPager vp_reportform;

    @Override
    public View initView() {
        reportformpager_content_view = View.inflate(mActivity, R.layout.reportformpager_content, null);
        tv_reportform_time = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_time);
        rg_reportform = (RadioGroup) reportformpager_content_view.findViewById(R.id.rg_reportform);
        //vp_reportform = (ViewPager) reportformpager_content_view.findViewById(R.id.vp_reportform);

        //绑定适配器
        vp_reportform.setAdapter(new RepotformAdapter());
        //为RadioGroup设置监听事件，根据是收入还是支出呈现不同的page
        rg_reportform.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_reportform_income:
                        vp_reportform.setCurrentItem(0,false);
                        break;
                    case  R.id.rb_reportform_expenditure:
                        vp_reportform.setCurrentItem(1,false);
                        break;
                }
            }
        });

        return reportformpager_content_view;
    }

    @Override
    public void initData() {

    }

    class RepotformAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
