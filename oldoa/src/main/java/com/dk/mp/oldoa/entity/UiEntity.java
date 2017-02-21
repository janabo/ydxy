package com.dk.mp.oldoa.entity;

import java.io.Serializable;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 存储要更新的控件
 * 
 * @author lj.zhang
 * 
 */
public class UiEntity implements Serializable {

	public TextView getTextV() {
		return textV;
	}

	public void setTextV(TextView textV) {
		this.textV = textV;
	}

	public ProgressBar getProgressB() {
		return progressB;
	}

	public void setProgressB(ProgressBar progressB) {
		this.progressB = progressB;
	}

	private TextView textV;// 文本控件
	private ProgressBar progressB;// 进度控件
	private ImageView imageView;//下载图标

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
}
