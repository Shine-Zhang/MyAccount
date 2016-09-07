package com.example.zs.myaccount;

/**
 * Create by Wang Yu on 2016/09/02
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zs.application.MyAplication;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    private ViewPager vp_guide;

    private int[] imgResIds = new int[]{R.drawable.ic_guide_0,R.drawable.ic_guide_1,R.drawable.ic_guide_2};

    private List<ImageView> imageViewList;
    private Button bt_guideactivity_enter;
    private ImageButton ib_guideactivity_enter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vp_guide = (ViewPager) findViewById(R.id.vp_guide);

        vp_guide.setAdapter(new MyGuidePageAdaper());

        ib_guideactivity_enter = (ImageButton) findViewById(R.id.ib_guideactivity_enter);

        imageViewList = new ArrayList<ImageView>();

        for (int i = 0; i < 3; i++){

            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(imgResIds[i]);
            imageViewList.add(iv);

            vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                //设置imagebutton在第三页显示，其它页隐藏
                public void onPageSelected(int position) {


                    if (position==2){

                        ib_guideactivity_enter.setVisibility(View.VISIBLE);
                    }
                    else {

                        ib_guideactivity_enter.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            //记录向导页面是否进入到过SharePreferce
            /*SharedPreferences enterGuide = getSharedPreferences("EnterGuide", MODE_PRIVATE);
            SharedPreferences.Editor edit = enterGuide.edit();
            SharedPreferences.Editor enterguide = edit.putBoolean("enterGuide", true);
            enterguide.commit();*/
            MyAplication.saveBooleanToSp("enterguide", true);
        }
    }

    class MyGuidePageAdaper extends PagerAdapter{

        //共需要实现四个方法

        @Override
        public int getCount() {
            return 3;    //GuideActivity中共有三个page
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((ImageView)object);
        }
    }

    //在点击imagebutton时，跳转至MainActivity
    public void enterMainActivity (View v){

        startActivity(new Intent(this,MainActivity.class));
        finish();

    }
}
