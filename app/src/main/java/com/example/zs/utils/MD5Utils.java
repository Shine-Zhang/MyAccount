package com.example.zs.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/8/6 0006.
 *
 *
 */
public class MD5Utils {

    /**
     * 该函数的功能是：任意字符串到MD5值
     * @param target 想要转化的目标字符串，通常为password
     * @return target所对应的MD5值
     */
    public static String getMD5Values(String target){
        StringBuffer ret = new StringBuffer("");
        MessageDigest Md5digest = null;
        try {
            Md5digest = MessageDigest.getInstance("MD5");
            byte[] digest = Md5digest.digest(target.getBytes());
            for (byte x:digest) {
                int lowdigits = x&0x000000ff;
                //可以在这加盐
                String partialValue = Integer.toHexString(lowdigits);
                if(partialValue.length()==1){
                    //这样也可以，但老师的方法似乎更好。
                    //partialValue="0"+partialValue;
                    //这个技巧还是可以的
                    ret.append("0");
                }
                ret.append(partialValue);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
       return ret.toString();
    }
}
