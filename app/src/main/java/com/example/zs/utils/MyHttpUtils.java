package com.example.zs.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/8/4.
 */
public class MyHttpUtils {

    public static String getStringFromInputStream(InputStream is){
        String ret = "";

        //字节数组输出流：
        // 在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] b = new byte[1024];
        int len = -1;
        try {
            //边读边写
             while((len = is.read(b,0,1024))!=-1){//当读入的这行数据不为空，就写出
                 byteArrayOutputStream.write(b,0,len);// 获取输入的数据
            }

            //设置输出流的编码，与服务器相同
            ret = byteArrayOutputStream.toString("GBK");

            byteArrayOutputStream.close();//close()仅表示无法往流中写数据
        } catch (IOException e) {
            e.printStackTrace();
        }
        //以字符串的形式，返回文件中的数据
        return ret;
    }
}
