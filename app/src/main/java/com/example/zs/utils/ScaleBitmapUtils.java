package com.example.zs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class ScaleBitmapUtils {

    public static Bitmap scaleBitmap(Bitmap bitmap,int width,int height){

        BitmapFactory.Options opt = new BitmapFactory.Options();
        //拿到图片的宽高信息
       int originalHight =  bitmap.getHeight();
        int originalWidth = bitmap.getWidth();
        //opt.inSampleSize = Math.min(originalHight/height,originalWidth/ width);
        //获取Bitmap
        Matrix bitmapMatrix = new Matrix();
        bitmapMatrix.postScale(originalWidth/ width, originalHight/height);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0,  width,
                 height, bitmapMatrix, true);
        return resultBitmap;
    }

}
