package com.dk.mp.core.util.encrypt;

import android.util.Base64;

/**
 * 作者：janabo on 2016/12/21 16:51
 */
public class Base64Utils {
    public static void main(String[] args) {
        System.out.println(getBase64("111111"));


        System.out.println(getFromBase64("MTU4NTA1MjAyODc="));

    }


    // 加密
    public static String getBase64(String str) {
        String strBase64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        return strBase64.replaceAll("\n","");
    }

    // 解密
    public static String getFromBase64(String str) {
        if(!str.contains("\n")){
           str = str+"\n";
        }
        String enToStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        return enToStr;
    }
}
