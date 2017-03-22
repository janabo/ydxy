package com.dk.mp.xg.wsjc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.dk.mp.xg.wsjc.ui.zssdjgl.ZssdjglDetailActivity;
import com.dk.mp.xg.wsjc.ui.zssdjgl.ZssdjglSearchActivity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/7 17:46
 */
public class ZssdjglListFragment extends BaseFragment implements View.OnClickListener{
    public static final String ARGS_TABS = "args_tabs";
    private ErrorLayout mError;
    private MyListView myListView;
    private String mType;
    private List<Zssdjgl> mData = new ArrayList<>();
    private LinearLayout search_button;

    public static ZssdjglListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARGS_TABS,type);
        ZssdjglListFragment fragment = new ZssdjglListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString(ARGS_TABS);
    }


    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        myListView = (MyListView)view.findViewById(R.id.mList);
        mError.setOnLayoutClickListener(this);
        search_button = (LinearLayout) view.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ZssdjglSearchActivity.class);
                intent.putExtra("type",mType);
                intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
                intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
                startActivity(intent);
            }
        });

        BroadcastUtil.registerReceiver(getActivity(), mRefreshBroadcastReceiver, "zssdjgl_refresh");

    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("zssdjgl_refresh")) {
                mData.clear();
                myListView.setPageNo(1);
                getData();
            }
        }
    };

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        initViews();
    }

    public void initViews(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(mContext, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        myListView.setAdapterInterface(mData, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_wsjc_record_list_item, parent, false);// 设置要转化的layout文件
                return new ZssdjglListFragment.MyView(view);
            }
            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                Zssdjgl zssdjgl = mData.get(position);
                ((MyView)holder).ssl_fjh.setText(zssdjgl.getName());
                ((MyView)holder).ssq.setText(zssdjgl.getRoom());
                ((MyView)holder).fs.setText(zssdjgl.getTime());
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
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        map.put("type",mType);
        map.put("role", "1");
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Zssdjgl>>(){}, "apps/zsdjgl/xsList", map, new HttpListener<PageMsg<Zssdjgl>>() {
            @Override
            public void onSuccess(PageMsg<Zssdjgl> result) {
                myListView.stopRefresh(true);
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {//是否获取到数据
                    mData.addAll(result.getList());
                    if(myListView.getAdapter()!=null) {
                        myListView.getAdapter().notifyDataSetChanged();
                        myListView.flish();
                    }
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

    private class MyView extends RecyclerView.ViewHolder{
        private TextView ssl_fjh,ssq,fs;
        public MyView(View itemView) {
            super(itemView);
            ssl_fjh = (TextView) itemView.findViewById(R.id.ssl_fjh);// 取得实例
            ssq = (TextView) itemView.findViewById(R.id.ssq);// 取得实例
            fs = (TextView) itemView.findViewById(R.id.fs);// 取得实例
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Zssdjgl zssgl = mData.get(getLayoutPosition());
                    Intent intent = new Intent(getActivity(), ZssdjglDetailActivity.class);
                    intent.putExtra("detailid",zssgl.getId());
                    intent.putExtra("type",mType);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.app_zssdjgl_list;
    }
}
