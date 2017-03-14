package com.dk.mp.xxxw.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dk.mp.core.entity.News;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xxxw.R;
import com.dk.mp.xxxw.db.RealmHelper;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校新闻列表
 * 作者：janabo on 2016/12/19 10:08
 */
public class NewsListActivity extends MyActivity implements View.OnClickListener{
    ErrorLayout mError;
    private MyListView myListView;
    private List<News> list = new ArrayList<>();
    private RealmHelper mRealmHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.app_news;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        mRealmHelper = new RealmHelper(this);
        myListView = (MyListView) findViewById(R.id.newslist);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

        myListView.setAdapterInterface(list, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_news_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                ((MyView)holder).title.setText(list.get(position).getName());
                ((MyView)holder).time.setText(list.get(position).getPublishTime());

                if (list.get(position).getImage() == null ){
                    ((MyView)holder).image.setVisibility(View.GONE);
                } else {
                    ((MyView)holder).image.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(list.get(position).getImage()).fitCenter().into(((MyView)holder).image);
                }
            }

            @Override
            public void loadDatas() {
                getData();
            }
        });
        getData();
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            getList();
        }else{
            getNewsLocal(1,myListView.pageNo);
        }
    }

    public void getList(){
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<News>>(){}, "apps/xxxw/list", map, new HttpListener<PageMsg<News>>() {
            @Override
            public void onSuccess(PageMsg<News> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
                    if(myListView.pageNo == 1){
                        mRealmHelper.deleteAllNews();//删除之前的数据
                    }
                    mRealmHelper.addNews(result.getList());
                    list.addAll(result.getList());
                    myListView.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(myListView.pageNo == 1) {
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else{
                        SnackBarUtil.showShort(myListView,R.string.nodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
//                if(myListView.pageNo == 1) {
//                    mError.setErrorType(ErrorLayout.DATAFAIL);
//                }else{
//                    SnackBarUtil.showShort(myListView,R.string.data_fail);
//                }
                getNewsLocal(2,myListView.pageNo);
            }
        });
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    private class MyView extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title,time;
        public MyView(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.news_img);// 取得实例
            title = (TextView) itemView.findViewById(R.id.news_title);// 取得实例
            time = (TextView) itemView.findViewById(R.id.news_time);// 取得实例

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.setTransitionName(view, "detail_element");

                    News news = list.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra("news", (Serializable) news);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((NewsListActivity) mContext,view,
                                    mContext.getString(R.string.transition__img));
                    ActivityCompat.startActivity((NewsListActivity) mContext,intent,options.toBundle());
                    ((NewsListActivity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
                }
            });
        }
    }

    /**
     * 本地获取数据
     * @param par  1 无网络  2，获取数据失败
     */
    public void getNewsLocal(int par,int pageNo){
        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        List<News> newses = mRealmHelper.queryAllNews();
        if(newses != null && newses.size()>0){
            SnackBarUtil.showShort(myListView,R.string.net_no2);
            list.addAll(newses);
            myListView.finish(1,1);
        }else{
            if(par == 1 && pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else if(par ==2 && pageNo == 1){
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }else if(par == 1 && pageNo != 1){
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }else if(par == 2 && pageNo != 1){
                SnackBarUtil.showShort(myListView,R.string.data_fail);
            }
        }
    }

}
