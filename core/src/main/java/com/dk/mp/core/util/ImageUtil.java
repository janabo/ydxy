package com.dk.mp.core.util;

import android.content.Context;

import com.dk.mp.core.application.MyApplication;

/**
 * 图片工具类.
 * @since 
 * @version 2012-10-16
 * @author wangw
 */
public class ImageUtil {
	/** 
	 * 获取图片名称获取图片的资源id的方法 .
	 * @param imageName 图片名称
	 * @return  资源id
	 */
	public static int getResource(String imageName) {
		Context context = MyApplication.getContext();
		int resId = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
		return resId;
	}
	
}
