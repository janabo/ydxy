package com.dk.mp.xg.wsjc.ui.Sswz;

import android.content.Intent;
import android.graphics.Color;
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
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Sswz;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宿舍违章登记记录
 * 作者：janabo on 2017/1/16 16:55
 */
public class SswzRecordListActivity extends MyActivity implements View.OnClickListener{
    LinearLayout mRootView;
    ErrorLayout mError;
    MyListView mRecycle;
    List<Sswz> mData = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.app_ssws_record_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("宿舍违纪登记");
        mRootView = (LinearLayout) findViewById(R.id.mRootView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mRecycle = (MyListView) findViewById(R.id.person_recycle);

        mRecycle = (MyListView) findViewById(R.id.person_recycle);
        mRecycle.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mRecycle.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecycle.setAdapterInterface(mData, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_wsjc_record_list_item, parent, false);// 设置要转化的layout文件
                return new SswzRecordListActivity.MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                Sswz m = mData.get(position);
                ((MyView)holder).ssl_fjh.setText(m.getRoom()+"-"+m.getName());
                ((MyView)holder).ssq.setText(m.getClassName());
                ((MyView)holder).fs.setText(m.getMsg());
            }

            @Override
            public void loadDatas() {
                getData();
            }
        });
        getData();
    }

    public void getData(){
        if(DeviceUtil.checkNet()){
            getList();
        }else{
            if(mRecycle.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(mRecycle,R.string.net_no2);
            }
        }
    }

    /**
     * 获取数据
     */
    public void getList(){
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo",mRecycle.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Sswz>>(){}, "apps/sswzdj/list", map, new HttpListener<PageMsg<Sswz>>() {
            @Override
            public void onSuccess(PageMsg<Sswz> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
                    mData.addAll(result.getList());
                    mRecycle.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(mRecycle.pageNo == 1) {
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else{
                        SnackBarUtil.showShort(mRecycle,R.string.nodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(mRecycle.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(mRecycle,R.string.data_fail);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    /**
     * 搜索
     * @param view
     */
    public void toSearch(View view){
        Intent intent = new Intent(this,SswzSearchActivity.class);
        intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
        intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
        startActivity(intent);
    }

    class MyView extends RecyclerView.ViewHolder{
        public TextView ssl_fjh;
        public TextView ssq;
        public TextView fs;

        public MyView(View itemView) {
            super(itemView);
            ssl_fjh = (TextView) itemView.findViewById(R.id.ssl_fjh);
            ssq = (TextView) itemView.findViewById(R.id.ssq);
            fs = (TextView) itemView.findViewById(R.id.fs);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sswz sswz = mData.get(getLayoutPosition());
                    Intent intent = new Intent(mContext,SswzRecordDetailActivity.class);
                    intent.putExtra("id",sswz.getId());
                    intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
                    intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
                    startActivity(intent);
                }
            });
        }
    }
}
