package com.example.zs.utils;

import android.content.Context;


/**
 * 该类实现dp到px的转化，以实现屏幕适配
 */
public class DensityUtil {
	/** 
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素) 
	 * @return  返回值为dp所对应的px的值(四舍五入得到)，单位为像素
     */  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  //获取屏幕的密度
        return (int) (dpValue * scale + 0.5f); //+0.5f四舍五入   3.7  3   3.7+0.5 = 4.2   4
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @return  返回值为px所对应的dp的值，无单位,类型为int型(四舍五入得到)
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
