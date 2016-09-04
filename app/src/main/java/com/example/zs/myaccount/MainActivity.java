package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.zs.pager.BasePager;
import com.example.zs.pager.OwnerPager;
import com.example.zs.pager.ReportFormPager;
import com.example.zs.pager.WishPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String tag="MainActivity";
    public ViewPager vp_mainactivity;
    public RadioGroup rg_mainactivity_bottom;
    public RadioButton rb_mainactivity_detail;
    public RadioButton rb_mainactivity_wish;
    public RadioButton rb_mainactivity_plus;
    public RadioButton rb_mainactivity_list;
    public RadioButton rb_mainactivity_mine;
    //新建ArrayList用于存储ViewPager里的不同page，从BasePager里面拿View
    List<BasePager> pageList =  new ArrayList<BasePager>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();

        //初始化主页面的控件,并抽成成员变量，方便调用
        //ViewPager
        vp_mainactivity = (ViewPager) findViewById(R.id.vp_mainactivity);
        //RadioGroup
        rg_mainactivity_bottom = (RadioGroup) findViewById(R.id.rg_mainactivity_bottom);
        //RadioButton
        rb_mainactivity_detail = (RadioButton) findViewById(R.id.rb_mainactivity_detail);
        rb_mainactivity_wish = (RadioButton) findViewById(R.id.rb_mainactivity_wish);
        rb_mainactivity_plus = (RadioButton) findViewById(R.id.rb_mainactivity_plus);
        rb_mainactivity_list = (RadioButton) findViewById(R.id.rb_mainactivity_list);
        rb_mainactivity_mine = (RadioButton) findViewById(R.id.rb_mainactivity_mine);

        //将“明细”（第一个）按钮默认设为选定状态
        rg_mainactivity_bottom.check(R.id.rb_mainactivity_detail);

        //将每个page页面加入pageList
        //pageList.add(new 帅神页面);
        pageList.add(new WishPager());
        //pageList.add(new 57页面);
        pageList.add(new ReportFormPager());
        pageList.add(new OwnerPager());

        //新建Adapter用于每个RadioButton点击显示不同页面
        vp_mainactivity.setAdapter(new MainActivity_ContentAdapter());

        //处理RadioGroup的点击事件，使之与对应的的ViewPager页面对应
        rg_mainactivity_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId){

                    case R.id.rb_mainactivity_detail:
                        vp_mainactivity.setCurrentItem(0);
                        break;

                    case R.id.rb_mainactivity_wish:
                        vp_mainactivity.setCurrentItem(1);
                        break;

                    case R.id.rb_mainactivity_plus:
                        vp_mainactivity.setCurrentItem(2);
                        //跳转

                        break;

                    case R.id.rb_mainactivity_list:
                        vp_mainactivity.setCurrentItem(3);
                        break;

                    case R.id.rb_mainactivity_mine:
                        vp_mainactivity.setCurrentItem(4);
                        break;
                }
            }
        });

        Log.i(tag,"wennm");
    }

    //新建Adapter用于每个RadioButton点击显示不同页面
    class MainActivity_ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return pageList.size();
            //return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
            // return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pageList.get(position);
            container.addView(basePager.mrootView);
            return basePager.mrootView;
            // return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }



    public void test(){


        Log.i("haha","云中歌");

    }

    public  void  test2(){
        Log.i("zhangxudong","zhangxudong");

        Log.i("lll","llll");

        Log.i("aola","aolaaolaaola");

    }

    public  void  test3(){


        Log.i("11","大油桃");


    }

}
