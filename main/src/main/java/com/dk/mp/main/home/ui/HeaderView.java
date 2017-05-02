package com.dk.mp.main.home.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.News;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.view.DraweeView;
import com.dk.mp.main.R;
import com.dk.mp.main.home.entity.SlideNews;
import com.dk.mp.xxxw.ui.NewsDetailActivity;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeaderView {

    private RollPagerView mLoopViewPager;
    private TestLoopAdapter mLoopAdapter;
    private List<SlideNews> slideList = new ArrayList<SlideNews>();
    private Context context;
    private Gson gson = new Gson();
    private View view;

    public void init(final View view, final Context context){
        this.view = view;
        slideList.add(new SlideNews("1","http://default","移动校园,老师学生的好帮手","res://com.dk.mp.zjhy/"+ R.mipmap.banner_def,null));
        mLoopViewPager = (RollPagerView) view.findViewById(R.id.loop_view_pager);
        mLoopViewPager.setPlayDelay(3000);
        mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
        mLoopViewPager.setAdapter(mLoopAdapter);
        mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));
//        mLoopViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//            @Override
//            public void onPageSelected(int position) {
//                TextView textView = (TextView) view.findViewById(R.id.tip);
//
//                Log.e("test","-----"+slideList.size()+"------------"+position+"--------------------");
//
//                textView.setText(slideList.get(position).getName());
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {}
//        });
        this.context = context;
       getData();
        //注册网络状态监听
        BroadcastUtil.registerReceiver(context, mRefreshBroadcastReceiver, new String[]{"ref_headerview"});
    }
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("ref_headerview")) {
                getData();
            }
        }
    };

    public void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/xxxw/slide", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<SlideNews> list = gson.fromJson(result.getJSONArray("data").toString(),new TypeToken<List<SlideNews>>(){}.getType());
                        if (list.size() != 0) {
                            slideList.clear();
                        }
                        if(list.size()>4) {
                            slideList.addAll(list.subList(0, 4));
                        }
                        mLoopAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
            }
        });
    }


    private class TestLoopAdapter extends LoopPagerAdapter {
        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }
        @Override
        public View getView(ViewGroup container, final int position) {
            DraweeView view = new DraweeView(container.getContext());
//            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setImageURI(Uri.parse(slideList.get(position).getImage()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,NewsDetailActivity.class);
                    News news = new News();
                    news.setImage(slideList.get(position).getImage());
                    news.setUrl(slideList.get(position).getUrl());
                    intent.putExtra("news", (Serializable)news);
//                    context.startActivity(intent);
                    ViewCompat.setTransitionName(v, "detail_element");
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,v,
                                    context.getString(com.dk.mp.xxxw.R.string.transition__img));
                    ActivityCompat.startActivity((Activity) context,intent,options.toBundle());
                    ((Activity) context).overridePendingTransition(com.dk.mp.xxxw.R.anim.slide_up, com.dk.mp.xxxw.R.anim.scale_down);
                }
            });

            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setPlaceholderImage(R.color.bg)
                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();
            view.setHierarchy(hierarchy);

            return view;
        }
        @Override
        public int getRealCount() {
            return slideList.size();
        }
    }

    private class newColorPointHintView extends ColorPointHintView{
        public newColorPointHintView(Context context, int focusColor, int normalColor) {
            super(context, focusColor, normalColor);
        }

        @Override
        public void setCurrent(int current) {
            super.setCurrent(current);
            TextView textView = (TextView) view.findViewById(R.id.tip);
            textView.setText(slideList.get(current).getName());
        }
    }
}