package com.dk.mp.oldoa.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dk.mp.oldoa.interfaces.IconPagerAdapter;

import java.util.ArrayList;


public class HomePageAdapter extends PagerAdapter implements IconPagerAdapter {

	private ArrayList<View> pageViews;

	public HomePageAdapter(ArrayList<View> views) {
		this.pageViews = views;
	}

	@Override
	public int getCount() {
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public Object instantiateItem(View arg0, int position) {
		((ViewPager) arg0).addView(pageViews.get(position));
		return pageViews.get(position);
	}

	@Override
	public int getIconResId(int index) {
		return 0;
	}
}
