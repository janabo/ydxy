package com.dk.mp.oldoa.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;
/**
 * 重写listview 增加手势事件.
 * @since 
 * @version 2013-1-21
 * @author admin
 */
public class GestureListView extends ListView {
	private GestureDetector  gDetector;
	
	
	public GestureDetector getgDetector() {
		return gDetector;
	}
	public void setgDetector(GestureDetector gDetector) {
		this.gDetector = gDetector;
	}
	public GestureListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    public GestureListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//return false;
		return this.gDetector.onTouchEvent(ev);
	}

	
	
}