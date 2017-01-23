package com.dk.mp.core.view.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.dk.mp.core.util.Logger;

/**
 * @since 
 * @version 2013-6-28
 * @author wangwei
 */
public class CleanEditText extends EditText {
	private Drawable dRight,dLeft;
	Context context;
	private Rect rBounds;
	public PopupWindow popupWindow;
	private boolean show = false;

	public CleanEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		checkOpen();
		initEditText();
	}

	public CleanEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		checkOpen();
		this.context = context;
		initEditText();
	}

	public CleanEditText(Context context) {
		super(context);
		checkOpen();
		this.context = context;
		initEditText();
	}

	private void checkOpen() {
		if (getTag() != null) {
			String str = (String) getTag();
			Logger.info("getTag:" + str);
			if ("open".equals(str)) {
				show = true;
			}
		}
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if (right != null) {
			dRight = right;
		}if (left != null) {
			dLeft = left;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	public Drawable getRightDrawable() {
		return dRight;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((this.dRight != null) && (event.getAction() == 1)) {
			this.rBounds = this.dRight.getBounds();
			int i = (int) event.getX();
			if (i > getRight() - 3 * this.rBounds.width()) {
				setText("");
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		dRight = null;
		rBounds = null;
		super.finalize();
	}

	// 初始化edittext 控件
	private void initEditText() {
		setEditTextDrawable();
		addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
			public void afterTextChanged(Editable paramEditable) {
			}

			public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
			}

			public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
				CleanEditText.this.setEditTextDrawable();
			}
		});


		setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
					if (getText().length() > 0&&dRight!=null) {
						setCompoundDrawables(null, null, dRight, null);
					}
				} else {
					setCompoundDrawables(null, null, null, null);
					// 此处为失去焦点时的处理内容
				}
			}
		});
	}

	// 控制图片的显示
	private void setEditTextDrawable() {
		Logger.info("setEditTextDrawable:" + show);
		if (show) {
			setCompoundDrawables(dLeft, null, this.dRight, null);
		} else {
			if (getText().toString().length() == 0) {
				setCompoundDrawables(dLeft, null, null, null);
			} else {
				setCompoundDrawables(dLeft, null, this.dRight, null);
			}
		}
	}




}