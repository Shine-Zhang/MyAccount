package com.example.zs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class ScaleBitmapUtils {

    public static Uri scaleBitmap(final Bitmap bitmap, final int width, final int height){
        Uri result = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //拿到图片的宽高信息
        final String path = Environment.getExternalStorageDirectory() + "/MyAccount/";
        final String fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        fileName= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
        final int originalHight =  bitmap.getHeight();
        final int originalWidth = bitmap.getWidth();
        //opt.inSampleSize = Math.min(originalHight/height,originalWidth/ width);
        //获取Bitmap
        new Thread() {
            @Override
            public void run() {
                Matrix bitmapMatrix = new Matrix();
                bitmapMatrix.postScale(width/originalWidth,height/originalHight);
                Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                        height, bitmapMatrix, true);
                try
                {
                    File maps = new File(path + fileName);
                    if(!maps.exists()) {
                        FileOutputStream out = new FileOutputStream(maps);
                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    }
                    resultBitmap.recycle();
                }
                catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }

        }.start();

       return  result;
    }

}
