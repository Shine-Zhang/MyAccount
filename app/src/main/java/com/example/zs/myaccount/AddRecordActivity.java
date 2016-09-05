package com.example.zs.myaccount;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.zs.addPage.AddBasePage;
import com.example.zs.addPage.IncomePage;
import com.example.zs.addPage.PayOutPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  wuqi
 * 此activity为“+”后跳转的activity
 * 处理逻辑：用户支出和收入信息的输入
 */

public class AddRecordActivity extends AppCompatActivity {
    private int VIEWPAGE_NUMBER = 2;
    private List<AddBasePage> addBasePageInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        //隐藏标题栏
        getSupportActionBar().hide();

        //找到viewpager控件
        ViewPager vp_addRecordActivity_content = (ViewPager) findViewById(R.id.vp_addRecordActivity_content);
        //
        addBasePageInfos = new ArrayList<AddBasePage>();
        //默认page为支出page
        addBasePageInfos.add(new PayOutPage(this));
        addBasePageInfos.add(new IncomePage(this));
        vp_addRecordActivity_content.setAdapter(new MyViewPagerAdapter());
    }

    /**
     * 内部类为viewPager的适配填充类，确定内部page的个数和对应的布局
     * 组成为2个page
     */
    class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return VIEWPAGE_NUMBER;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            //确定关系
            return view ==(View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //确定page的布局
            View mView = addBasePageInfos.get(position).mView;
            container.addView(mView);
            return mView;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }

    /**
     * button点击事件，弹出日期选择器，获取日期
     * @param v
     */
    public void choiceTime(View v){
        //不用指定位置，就不需要使用popupwindow

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = View.inflate(this, R.layout.date_choice, null);
        builder.setView(inflate)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
        .show()

            ;

    }
}

