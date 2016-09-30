package com.example.zs.utils;

import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/29 0029.
 * @author  Shine-Zhang
 */
public class SeletorUtils {

    private static List<Integer> all_selectors = new ArrayList<Integer>() ;
    private static List<Integer> all_shapes = new ArrayList<Integer>() ;

    private static void resultSelectors(){

        //首先获得目标文件的Class
        Class src = com.example.zs.myaccount.R.drawable.class;

        Field[] picFields = src.getFields();

        for(Field field:picFields){
            //此处的名字可以自己随心意而定
            if(field.getName().startsWith("selecter_addrecordactivity_color")){

                try {
                    //System.out.println("*******************************"+field.getName().substring(4,field.getName().length())+"******************");
                    all_selectors.add(field.getInt(src));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }



    private static void resultShapes(){

        //首先获得目标文件的Class
        Class src = com.example.zs.myaccount.R.drawable.class;

        Field[] picFields = src.getFields();

        for(Field field:picFields){
            //此处的名字可以自己随心意而定
            if(field.getName().startsWith("bg_addicon_circular0")){

                try {
                    //System.out.println("*******************************"+field.getName().substring(4,field.getName().length())+"******************");
                    all_shapes.add(field.getInt(src));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public static void setSelector(int id, ImageView target){
        if(all_selectors.size()==0){
            resultSelectors();
        }
        target.setBackgroundResource(all_selectors.get(id%all_selectors.size()));
    }


    public static void setBackgroud(int id, ImageView target){
        if(all_shapes.size()==0){
            resultShapes();
        }
        target.setBackgroundResource(all_shapes.get(id%all_shapes.size()));
    }
}
