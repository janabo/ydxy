package com.dk.mp.xg.wsjc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.adapter.AdapterInterface;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Zssgl;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dk.mp.xg.R.id.oa_list;

/**
 * 作者：janabo on 2017/1/18 17:51
 */
public class ZssglFragment extends BaseFragment implements View.OnClickListener{
    public static final String ARGS_TABS = "args_tabs";
    private ErrorLayout mError;
    private MyListView myListView;
    private List<Zssgl> mData = new ArrayList<>();
    private String mType;

    public static ZssglFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARGS_TABS,type);
        ZssglFragment fragment = new ZssglFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString(ARGS_TABS);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.app_zssgl_fragment_detail;
    }

    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        myListView = (MyListView)view.findViewById(oa_list);
        mError.setOnLayoutClickListener(this);
    }

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
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                ((MyView)holder).ssl_fjh.setText(mData.get(position).getLx());
                ((MyView)holder).ssq.setText(mData.get(position).getShzt());
                ((MyView)holder).fs.setText(mData.get(position).getSqrq());
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
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Zssgl>>(){}, "http://192.168.3.163:8082/mp-lgj/apps/zxzssgl/list", map, new HttpListener<PageMsg<Zssgl>>() {
            @Override
            public void onSuccess(PageMsg<Zssgl> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {//是否获取到数据
                    mData.addAll(result.getList());
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

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        getData();
    }
}
