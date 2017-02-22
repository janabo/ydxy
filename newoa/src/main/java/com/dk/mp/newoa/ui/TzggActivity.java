package com.dk.mp.newoa.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.anni.TransitionHelper;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.Tzgg;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OA通知公告
 * 作者：janabo on 2017/1/3 17:15
 */
public class TzggActivity extends MyActivity implements View.OnClickListener{
    ErrorLayout mError;
    private MyListView myListView;
    private List<Tzgg> list = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.oa_all_list;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        myListView = (MyListView) findViewById(R.id.oa_list);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

        myListView.setAdapterInterface(list, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.new_oa_list_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                ((MyView)holder).title.setText(list.get(position).getTitle());
                ((MyView)holder).shijian.setText(list.get(position).getTime());
                ((MyView)holder).bumen.setText(list.get(position).getSubTitle());
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
            if(myListView.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }
        }
    }

    public void getList(){
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Tzgg>>(){}, "apps/tzgg/list", map, new HttpListener<PageMsg<Tzgg>>() {
            @Override
            public void onSuccess(PageMsg<Tzgg> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {//是否获取到数据
                    list.addAll(result.getList());
                    myListView.addList(result.getList());
                }else{
                    if(myListView.pageNo == 1) {//是否是第一页
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else{
                        SnackBarUtil.showShort(myListView,R.string.nodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(myListView.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(myListView,R.string.data_fail);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    private class MyView extends RecyclerView.ViewHolder{
        private TextView title,bumen,shijian;
        public MyView(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);// 取得实例
            bumen = (TextView) itemView.findViewById(R.id.bumen);// 取得实例
            shijian = (TextView) itemView.findViewById(R.id.shijian);// 取得实例
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.setTransitionName(view, "detail_element");
                    Tzgg news = list.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, TzggDetailActivity.class);
                    intent.putExtra("title", getIntent().getStringExtra("title"));
                    intent.putExtra("url", news.getUrl());

                    ActivityOptionsCompat options = TransitionHelper.makeOptionsCompat((TzggActivity) mContext,
                            Pair.create(view,"detail_element"));

                    ActivityCompat.startActivity((TzggActivity) mContext,intent,options.toBundle());
                    ((TzggActivity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
                }
            });
        }
    }
}
