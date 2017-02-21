package com.dk.mp.oldoa.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dk.mp.oldoa.R;


/**
 * 自定义对话框类，包括Alert、Confirm、Waiting Bar
 *
 */
public class MyDialog extends AlertDialog implements View.OnClickListener {

	private AlertType type = AlertType.DIALOG_ALERT;
	
	private String message;
	
	public boolean isBottom = false;	//DIALOG_WAITING 弹出框是否决定在底部显示
	
	private Context context_ = null;
	public String activityType = "";
	public long id_;
	
	TextView tv = null;
	
	public MyDialog(Context context, AlertType alertType) {
    	super(context);
		context_ = context;
		type = alertType;
	}
	
	
	public MyDialog(Context context, int theme) {
		super(context, R.style.progressDialog);
		context_ = context;
		type = AlertType.DIALOG_WAITING;
	}
	public MyDialog(Context context) {
		super(context);

	}

	public MyDialog(Context context, int theme, AlertType alertType) {
		super(context, theme);
		type = alertType;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (type) {
			case DIALOG_WAITING:
				setContentView(R.layout.dialog_waiting);
				tv = (TextView) findViewById(R.id.msg);
				tv.setText(message);
			break;
		}
	}
	
	//防止打开dialog的时候会出现异常
	@Override
	public void show() {
		try {
			super.show();
		} catch (Exception e) {
			
		}
	}
	
	//设置默认的取消按钮事件
	@Override
	public void onClick(View v) {
		dismiss();
	}

	public void setMessage(String msg) {
		message = msg;
	}

	public static enum AlertType {
		DIALOG_CONFIRM,
		DIALOG_WAITING,
		DIALOG_ALERT,
		DIALOG_ATTACHALERT
	} 
	
	/**
	 * 设置进程条相关属性
	 */
	public void setProperty(int type){
		Window window = this.getWindow();	//得到对话框的窗口．
		WindowManager.LayoutParams wl = window.getAttributes();
		if (type == 0) {
			wl.x =0;	//这两句设置了对话框的位置．0为中间
			wl.y =75;
		}
		wl.gravity=Gravity.BOTTOM;
		window.setAttributes(wl);
	}
	
	public void setTvVisiable(boolean isShow)
	{
		if (tv != null)
		{
			if (message != null)
			{
				tv.setText(message);
			}
			if (isShow)
			{
				tv.setVisibility(View.VISIBLE);
			}
			else
			{
				tv.setVisibility(View.GONE);
			}
		}
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		 switch (keyCode) {
		 case KeyEvent.KEYCODE_BACK:
			 
			 break;
		 }
		 return super.onKeyDown(keyCode, event);
	 }
}
