package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.WsjcRecordListAdapter;
import com.dk.mp.xg.wsjc.entity.DfRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 卫生检查列表界面
 * 作者：janabo on 2017/1/12 14:26
 */
public class WsjcRecordListActivity extends MyActivity implements View.OnClickListener{
    LinearLayout mRootView;
    ErrorLayout mError;
    RecyclerView mRecyclerView;
    String ssqId;//宿舍区id
    String sslId;//宿舍楼id
    String lcId;//楼层id
    List<DfRecord> mData = new ArrayList<>();
    WsjcRecordListAdapter mainAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjc_record_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("卫生检查记录");
        mRootView = (LinearLayout) findViewById(R.id.mRootView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mainAdapter = new WsjcRecordListAdapter(mContext,mData);
        mRecyclerView.setAdapter ( mainAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initialize() {
        super.initialize();
        Intent intent = getIntent();
        ssqId = intent.getStringExtra("ssqId");
        sslId = intent.getStringExtra("sslId");
        lcId = intent.getStringExtra("lcId");
        getData();
    }

    public void getData(){
        if(DeviceUtil.checkNet()){
            getList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    /**
     * 获取数据
     */
    public void getList(){
        Map<String,Object> map = new HashMap<>();
        map.put("ssqId",ssqId);
        map.put("sslId",sslId);
        map.put("lcId",lcId);
        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdf/record", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<DfRecord> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<DfRecord>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<DfRecord> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mData.addAll(dfxxes);
                                mainAdapter.notifyDataSetChanged();
                                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            }else{
                                mError.setErrorType(ErrorLayout.NODATA);
                            }
                        } else {
                            mError.setErrorType(ErrorLayout.DATAFAIL);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    /**
     * 搜索
     * @param v
     */
    public void toSearch(View v){
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
    }
}
