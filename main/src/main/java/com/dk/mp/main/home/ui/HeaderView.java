package com.dk.mp.main.home.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.view.DraweeView;
import com.dk.mp.main.R;
import com.dk.mp.main.home.entity.SlideNews;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;

public class HeaderView {

    private RollPagerView mLoopViewPager;
    private TestLoopAdapter mLoopAdapter;
    private List<SlideNews> slideList = new ArrayList<SlideNews>();
    private Context context;

    public void init(View view, final Context context){
        slideList.add(new SlideNews("1","http://default","移动校园,老师学生的好帮手","res://com.dk.mp.zjhy/"+ R.mipmap.banner_def,null));
        mLoopViewPager = (RollPagerView) view.findViewById(R.id.loop_view_pager);
        mLoopViewPager.setPlayDelay(5000);
        mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
        mLoopViewPager.setAdapter(mLoopAdapter);
        mLoopViewPager.setHintView(new ColorPointHintView(context, Color.YELLOW, Color.WHITE));
        this.context = context;
//        new HttpClientUtil().post("apps/xxxw/slide",null,SlideNews.class)
//                .subscribe(new Action1<SlideNews>() {
//                    @Override
//                    public void call(SlideNews slideNews) {
//                        slideList.add(slideNews);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                }, new Action0() {
//                    @Override
//                    public void call() {
//                        slideList.remove(0);
//                        mLoopAdapter.notifyDataSetChanged();
//                    }
//                });
        HttpUtil.getInstance().gsonRequestJson(SlideNews.class, "apps/xxxw/slide", null, new HttpListener<SlideNews>() {
            @Override
            public void onSuccess(SlideNews result) {
                slideList.remove(0);
                mLoopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class TestLoopAdapter extends LoopPagerAdapter {
        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }
        @Override
        public View getView(ViewGroup container, final int position) {
            DraweeView view = new DraweeView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageURI(Uri.parse(slideList.get(position).getImage()));
            view.setTransitionName("headerimage");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,HeaderDetailsActivity.class);
                    intent.putExtra("uri",slideList.get(position).getImage());
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, v, "headerimage");
                    ActivityCompat.startActivity((Activity) context,intent, options.toBundle());
                }
            });
            return view;
        }
        @Override
        public int getRealCount() {
            return slideList.size();
        }
    }
}