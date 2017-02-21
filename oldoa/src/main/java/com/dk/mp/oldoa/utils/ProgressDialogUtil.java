package com.dk.mp.oldoa.utils;

import android.content.Context;

public class ProgressDialogUtil {

	public final static ProgressDialogUtil progress = new ProgressDialogUtil();
	public MyDialog cDialog = null;
	static Context mContext;

	// 限制住不能直接产生一个实例
	private ProgressDialogUtil() {

	};

	/*
	 * 
	 * 采用这种单例防止多线程，导致业务逻辑混乱
	 * 
	 * @return
	 */

	public synchronized static ProgressDialogUtil getIntence(Context context) {
		mContext = context;
		return progress;
	}

	public void onLoading(String msg) {
		dismissDialog();
		cDialog = new MyDialog(mContext, 0);
		cDialog.isBottom = true;
		cDialog.setMessage(msg);
		cDialog.show();
	}

	public void dismissDialog() {
		if (cDialog != null) {
			cDialog.dismiss();
			cDialog = null;
		}
	}
}
