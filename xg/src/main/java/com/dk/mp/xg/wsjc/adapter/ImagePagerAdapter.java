package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.xg.R;

import java.util.List;

/**
 * 作者：janabo on 2017/3/7 10:08
 */
public class ImagePagerAdapter extends PagerAdapter {

    private List<String> list;
    private Context context;
    private int screenWidth;
    private boolean animating = false;
    private boolean show = true;

    public ImagePagerAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
        screenWidth = DeviceUtil.getScreenWidth(context);
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (list.get(position).startsWith("http")) {
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.photo_item_image, container, false);
            imageView.setMaxWidth(screenWidth);
            Glide.with(context).load(list.get(position)).fitCenter().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switchHeadBar();
                }
            });
            container.addView(imageView);
            return imageView;
        } else {
            ImageView imageView = (ImageView) LayoutInflater.from(context)
                    .inflate(R.layout.photo_item_image, container, false);
            imageView.setMaxWidth(screenWidth);
            Glide.with(context).load(list.get(position)).fitCenter().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switchHeadBar();
                }
            });
            container.addView(imageView);
            return imageView;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    private void switchHeadBar() {
        if (animating) {
            return;
        }
        if (show) {
            Animation headAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.translate_top_up);
            headAnimation.setFillAfter(true);
            headAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {

                }

                public void onAnimationRepeat(Animation animation) {

                }
                public void onAnimationEnd(Animation animation) {
                    animating = false;
                    show = false;
                }
            });
            Animation bottomAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.translate_bottom_down);
            bottomAnimation.setFillAfter(true);
            bottomAnimation.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationStart(Animation animation) {

                }

                public void onAnimationRepeat(Animation animation) {

                }

                public void onAnimationEnd(Animation animation) {

                }
            });
            animating = true;
        } else {
            Animation headAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.translate_top_down);
            headAnimation.setFillAfter(true);
            headAnimation.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationStart(Animation animation) {

                }

                public void onAnimationRepeat(Animation animation) {

                }

                public void onAnimationEnd(Animation animation) {
                    animating = false;
                    show = true;
                }
            });
            Animation bottomAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.translate_bottom_up);
            bottomAnimation.setFillAfter(true);
            bottomAnimation.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationStart(Animation animation) {

                }

                public void onAnimationRepeat(Animation animation) {

                }

                public void onAnimationEnd(Animation animation) {

                }
            });
            animating = true;
        }
    }

}
