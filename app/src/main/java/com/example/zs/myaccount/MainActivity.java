package com.example.zs.myaccount;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String tag="MainActivity";
    private ViewPager vp_main;
    private RadioGroup rg_main_bottom;
    private RadioButton rb_main_detail;
    private RadioButton rb_main_wish;
    private RadioButton rb_main_plus;
    private RadioButton rb_main_list;
    private RadioButton rb_main_mine;

    List<View> pageList =  new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();

        //初始化主页面的控件,并抽成成员变量，方便调用
        //ViewPager
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        //RadioGroup
        rg_main_bottom = (RadioGroup) findViewById(R.id.rg_main_bottom);
        //RadioButton
        rb_main_detail = (RadioButton) findViewById(R.id.rb_main_detail);
        rb_main_wish = (RadioButton) findViewById(R.id.rb_main_wish);
        rb_main_plus = (RadioButton) findViewById(R.id.rb_main_plus);
        rb_main_list = (RadioButton) findViewById(R.id.rb_main_list);
        rb_main_mine = (RadioButton) findViewById(R.id.rb_main_mine);

        //将明细按钮默认设为选定状态
        rg_main_bottom.check(R.id.rb_main_detail);

        //新建Adapter用于每个RadioButton点击显示不同页面
        vp_main.setAdapter(new MainContentAdapter());

        //处理RadioGroup的点击事件
        rg_main_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId){

                    case R.id.rb_main_detail:

                        break;

                    case R.id.rb_main_wish:

                        break;

                    case R.id.rb_main_plus:

                        break;

                    case R.id.rb_main_list:

                        break;

                    case R.id.rb_main_mine:

                        break;
                }
            }
        });

        Log.i(tag,"wennm");
    }
    //新建Adapter用于每个RadioButton点击显示不同页面
    class MainContentAdapter extends PagerAdapter{

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
