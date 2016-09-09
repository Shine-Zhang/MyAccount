package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.zs.bean.payouContentInfo;
import com.example.zs.pager.BasePager;
import com.example.zs.pager.OwnerPager;
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

        /*//透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/

        //初始化主页面的控件,并抽成成员变量，方便调用
        //ViewPager
        vp_mainactivity = (ViewPager) findViewById(R.id.vp_mainactivity);
        //RadioGroup
        rg_mainactivity_bottom = (RadioGroup) findViewById(R.id.rg_mainactivity_bottom);
        //RadioButton
        rb_mainactivity_detail = (RadioButton) findViewById(R.id.rb_mainactivity_detail);
        rb_mainactivity_wish = (RadioButton) findViewById(R.id.rb_mainactivity_wish);
       // rb_mainactivity_plus = (RadioButton) findViewById(R.id.rb_mainactivity_plus);
        rb_mainactivity_list = (RadioButton) findViewById(R.id.rb_mainactivity_list);
        rb_mainactivity_mine = (RadioButton) findViewById(R.id.rb_mainactivity_mine);
        //将“明细”（第一个）按钮默认设为选定状态
        rg_mainactivity_bottom.check(R.id.rb_mainactivity_detail);

        //将每个page页面加入pageList
        //pageList.add(new 帅神页面);
        pageList.add(new WishPager(this));
        //pageList.add(new 57页面);
        //pageList.add(new ReportFormPager(this));
        pageList.add(new OwnerPager(this));

        //新建Adapter用于每个RadioButton点击显示不同页面
        vp_mainactivity.setAdapter(new MainActivity_ContentAdapter());

        //处理RadioGroup的点击事件，使之与对应的的ViewPager页面对应
        //（暂时跳转两个页面做测试）
        rg_mainactivity_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId){

                    case R.id.rb_mainactivity_detail:
                        vp_mainactivity.setCurrentItem(0);
                        //pageList.get(0).initData();
                        break;

                    case R.id.rb_mainactivity_wish:
                        Log.i(tag,"00");
                        vp_mainactivity.setCurrentItem(1);
                        pageList.get(0).initData();
                        break;
                    case R.id.rb_mainactivity_list:
                        vp_mainactivity.setCurrentItem(2);
                        pageList.get(0).initData();
                        break;

                    case R.id.rb_mainactivity_mine:
                        vp_mainactivity.setCurrentItem(3);
                        pageList.get(1).initData();
                        break;
                }
            }
        });

        //vp_mainactivity.setCurrentItem(0);
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
            /*测试代码
            Log.i("hahaha","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
           if(basePager!=null)
                Log.i("hahaha","##################");
            if(basePager.mrootView!=null)
                Log.i("hahaha","000000000000000");*/
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
    public void add(View v){
        startActivityForResult(new Intent(MainActivity.this,AddRecordActivity.class),310);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(tag,resultCode+"--");
        //确认健返回
        if(resultCode==555&&intent!=null){
            int id = intent.getIntExtra("id", 0);
            int resourceID = intent.getIntExtra("resourceID", 0);
            String categoryName = intent.getStringExtra("categoryName");
            int year = intent.getIntExtra("year", 0);
            int mouth = intent.getIntExtra("mouth", 0);
            int day = intent.getIntExtra("day", 0);
            String money = intent.getStringExtra("money");
            String marks = intent.getStringExtra("marks");
            String photo = intent.getStringExtra("photo");
            payouContentInfo payouContentInfo = new payouContentInfo(id, resourceID, categoryName, year, mouth, day, money, marks, photo);
            Log.i(tag,payouContentInfo.toString());
            //super无法执行到
            // return;
        }else if(resultCode==444){

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
