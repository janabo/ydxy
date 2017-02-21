package com.dk.mp.oldoa.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.dk.mp.oldoa.R;

/**
 * 用于生成日历展示的GridView布局
 *
 * @author lj.zhang
 */
public class CalendarGridView extends GridView {

	/**
	 * 当前操作的上下文对象
	 */
	private Context mContext;

	/**
	 * CalendarGridView 构造器
	 *
	 * @param context 当前操作的上下文对象
	 */
	public CalendarGridView(Context context) {
		super(context);
		mContext = context;

		setGirdView();
	}

	public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * 初始化gridView 控件的布局
	 */
	private void setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		setLayoutParams(params);
		setNumColumns(7);// 设置每行列数
		setGravity(Gravity.CENTER_VERTICAL);// 位置居中
		setVerticalSpacing(1);// 垂直间隔
		setHorizontalSpacing(1);// 水平间隔
		setCacheColorHint(getResources().getColor(R.color.transparent));
		setBackgroundColor(getResources().getColor(R.color.snow));
		setSelector(getResources().getDrawable(R.color.transparent));

		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		setPadding(x, 0, 0, 0);// 居中
	}
}
