package com.example.zs.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/29 0029.
 * @author  Shine-Zhang
 */
public class SeletorUtils {

    private static List<Integer> all_pics = new ArrayList<Integer>() ;

    private static void result(){

        //首先获得目标文件的Class
        Class src = com.example.zs.myaccount.R.drawable.class;

        Field[] picFields = src.getFields();

        for(Field field:picFields){
            //此处的名字可以自己随心意而定
            if(field.getName().startsWith("pic")){

                try {

                    //System.out.println("*******************************"+field.getName().substring(4,field.getName().length())+"******************");
                    all_pics.add(field.getInt(src));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public static List<Integer> getSelectorResources(){
        result();
        return all_pics;
    }
}
