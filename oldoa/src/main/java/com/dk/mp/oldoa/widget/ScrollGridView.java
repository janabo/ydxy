package com.dk.mp.oldoa.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.dk.mp.oldoa.view.groupgridview.StickyGridHeadersGridView;


public class ScrollGridView extends StickyGridHeadersGridView {
	public ScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollGridView(Context context) {
		super(context);
	}

	public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
