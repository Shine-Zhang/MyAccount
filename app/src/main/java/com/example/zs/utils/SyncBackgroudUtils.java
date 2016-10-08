package com.example.zs.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class SyncBackgroudUtils {

    static List<Integer> colors;
    static{
        colors = new ArrayList<>();
        colors.add(WholeCustomColors.phonefeeColor);
        colors.add(WholeCustomColors.hufuColor);
        colors.add(WholeCustomColors.spriteColor);
        colors.add(WholeCustomColors.liwuColor);
        colors.add(WholeCustomColors.sportColor);
        colors.add(WholeCustomColors.clothColor);
        colors.add(WholeCustomColors.stardyColor);
        colors.add(WholeCustomColors.medchineColor);
        colors.add(WholeCustomColors.fruitColor);
        colors.add(WholeCustomColors.lifeColor);
        colors.add(WholeCustomColors.travelColor);
        colors.add(WholeCustomColors.transportColor);
        colors.add(WholeCustomColors.hongbaoColor);
    }

    public static int getCorrespondingColor(int resId){

        return colors.get(resId%13);
    }

    private static StateListDrawable createStateListDrawable(int resId)
    {
        StateListDrawable drawable = new StateListDrawable();
        GradientDrawable normalSta = new  GradientDrawable();
        normalSta.setColor(Color.rgb(230,230,255));
        normalSta.setShape(GradientDrawable.OVAL);
        GradientDrawable pressSta = new  GradientDrawable();
        pressSta.setColor(getCorrespondingColor(resId));
        pressSta.setShape(GradientDrawable.OVAL);
        //Pressed按下去时的颜色
        drawable.addState(new int[]{android.R.attr.state_enabled}, pressSta);

        drawable.addState(new int[]{-android.R.attr.state_enabled},normalSta);

        return drawable;
    }

    public static  void setSelector(int resId,ImageView target){

        target.setBackground(createStateListDrawable(resId));
    }

    public static void setBackgroud(int resId,ImageView target){
        if(target.getBackground() instanceof GradientDrawable) {
            GradientDrawable myGrad = (GradientDrawable) target.getBackground();
            myGrad.setColor(getCorrespondingColor(resId));
        }
    }


    public static void setTimeLineBackgroud(int resId, ImageView target,int color){
            GradientDrawable myGrad = new GradientDrawable();
            if(color>0) {

                myGrad.setColor(getCorrespondingColor(resId));

            }else if(color<0){
                myGrad.setColor(color);
            }

        myGrad.setShape(GradientDrawable.OVAL);
        target.setBackground(myGrad);
    }
}

 class WholeCustomColors{

     public  static int phonefeeColor = Color.rgb(92, 228, 162);
     public  static int hufuColor = Color.rgb(248, 137, 235);
     public  static int spriteColor = Color.rgb(247, 181, 45);
     public  static int liwuColor = Color.rgb(243, 59, 151);
     public  static int sportColor = Color.rgb(75, 206, 241);
     public  static int clothColor = Color.rgb(0, 137, 230);
     public  static int stardyColor = Color.rgb(251, 143, 51);
     public  static int medchineColor = Color.rgb(145, 189, 254);
     public  static int fruitColor = Color.rgb(253, 53, 100);
     public  static int lifeColor = Color.rgb(252, 218, 49);
     public  static int travelColor = Color.rgb(170, 233, 61);
     public  static int transportColor = Color.rgb(92, 228, 162);
     public  static int hongbaoColor = Color.rgb(255, 41, 12);
}

