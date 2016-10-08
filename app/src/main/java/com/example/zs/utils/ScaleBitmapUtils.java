package com.example.zs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class ScaleBitmapUtils {
    static Uri  result = null;
    public static Uri scaleBitmap(final Bitmap bitmap, final int width, final int height){

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
/*                Matrix bitmapMatrix = new Matrix();
                bitmapMatrix.postScale(width/originalWidth,height/originalHight);
                Log.i("xufengnian",width+"---"+originalWidth);
                Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, originalWidth,
                        originalHight, bitmapMatrix, true);*/
                Bitmap resultBitmap = bitmap;

                try
                {
                    File maps = new File(path + fileName);
                    if(!maps.exists()) {
                        Log.i("wawawawa11","4567890");
                        FileOutputStream out = new FileOutputStream(maps);
                        Log.i("wawawawa22","4567890");
                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        Log.i("wawawawa33","4567890");
                    }

                    resultBitmap.recycle();
                    result = Uri.fromFile(maps);

                    Log.i("aqaqaq", "********************"+result.toString());
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
