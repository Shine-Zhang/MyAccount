package com.example.zs.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.example.zs.application.MyAplication;

import java.text.SimpleDateFormat;


/**
 * Create by Wang Yu on 2016/09/05
 */
public class SplashActivity extends Activity {

    private View rl_splashactivity_bg;
    private SharedPreferences enterGuide;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_splashactivity_bg = findViewById(R.id.rl_splashactivity_bg);

        showAmination();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    }

    private void showAmination() {

        AnimationSet animationSet = new AnimationSet(false);

        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(5000);

        animationSet.addAnimation(alphaAnimation);
        rl_splashactivity_bg.setAnimation(animationSet);

        //开启动画
        animationSet.start();


       /* enterGuide = getSharedPreferences("EnterGuide", MODE_PRIVATE);*/


        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始播放时回调

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /*Log.i("haha","动画结束啦啦啦啦啦啦");*/
                boolean enterguide = MyAplication.getBooleanFromSp("enterguide");
               /* Log.i("haha","@@@@@@@@@@@@@@@@@enterguide: "+enterguide);*/
                if (enterguide){

                    //如果之前进入过想到页面则直接进入主页面
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
                else {

                    //动画结束后回调，在这里进入向导页面
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复播放时回调
            }
        });
    }
}
