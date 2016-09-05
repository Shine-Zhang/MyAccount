package com.example.zs.myaccount;

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

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vp_guide;

    private int[] imgResIds = new int[]{R.drawable.ic_guide_0,R.drawable.ic_guide_1,R.drawable.ic_guide_2};

    private List<ImageView> imageViewList;
    private Button bt_guideactivity_enter;
    private ImageButton ib_guideactivity_enter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();

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
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
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
}
