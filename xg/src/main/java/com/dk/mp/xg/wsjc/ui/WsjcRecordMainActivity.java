package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.WsjcRecordMainAdapter;
import com.dk.mp.xg.wsjc.entity.Kf;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 卫生检查记录 选择楼层界面
 * 作者：janabo on 2017/1/11 16:24
 */
public class WsjcRecordMainActivity extends MyActivity implements WsjcRecordMainAdapter.OnItemClickListener {
    ErrorLayout mErrorLayout;//加载和错误提示
    RecyclerView ssqRecyclerView;//宿舍区
    RecyclerView sslRecyclerView;//宿舍楼
    RecyclerView lcRecyclerView;//楼层
    WsjcRecordMainAdapter qAdapter;//
    WsjcRecordMainAdapter lAdapter;//
    WsjcRecordMainAdapter cAdapter;//
    List<Kf> mSsqList = new ArrayList<>();//宿舍区
    List<Kf> mSslList = new ArrayList<>();//宿舍楼
    List<Kf> mCList = new ArrayList<>();//楼层
    Map<String,List<Kf>> sslMap = new HashMap<>();//存放宿舍楼信息
    int selectSsq = 0;//选择第几个
    int selectSsl = 0;//选择第几个
    int selectLc = 0;//选择第几个

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjc_record_main;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("卫生检查记录");
        mErrorLayout = (ErrorLayout) findViewById(R.id.error_layout);
        ssqRecyclerView = (RecyclerView) findViewById(R.id.ssq);
        sslRecyclerView = (RecyclerView) findViewById(R.id.ssl);
        lcRecyclerView = (RecyclerView) findViewById(R.id.lc);

        ssqRecyclerView.setHasFixedSize ( false );
        ssqRecyclerView.setLayoutManager ( new LinearLayoutManager( this ) );
        qAdapter = new WsjcRecordMainAdapter( mContext,mSsqList,1,selectSsq);
        ssqRecyclerView.setAdapter ( qAdapter );
        ssqRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        ssqRecyclerView.setItemAnimator(new DefaultItemAnimator());

        sslRecyclerView.setHasFixedSize ( true );
        sslRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        lAdapter = new WsjcRecordMainAdapter( mContext,mSslList,2,selectSsl);
        sslRecyclerView.setAdapter ( lAdapter );
        sslRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        sslRecyclerView.setItemAnimator(new DefaultItemAnimator());

        lcRecyclerView.setHasFixedSize ( true );
        lcRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        cAdapter = new WsjcRecordMainAdapter( mContext,mCList,3,selectLc);
        lcRecyclerView.setAdapter ( cAdapter );
        lcRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        lcRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initialize() {
        super.initialize();
        getData();
    }


    public void getData(){
        if(DeviceUtil.checkNet()) {//判断是否有网络
            getList();
        }else{
            mErrorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    /**
     * 获取数据
     */
    public void getList(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdf/level", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result.getInt("code") == 200){
                        JSONArray ja = result.optJSONArray("data");
                        if(ja != null && ja.length()>0) {
                            for (int i=0;i<ja.length();i++) {//解析宿舍区数据
                                Kf kf = new Gson().fromJson(ja.getJSONObject(i).toString(), Kf.class);
                                mSsqList.add(kf);

                                List<Kf> lcs = new ArrayList<Kf>();
                                JSONArray jaa = ja.getJSONObject(i).optJSONArray("ssls");
                                if(jaa != null){//解析宿舍楼数据
                                    for(int j=0;j<jaa.length();j++){
                                        Kf k = new Gson().fromJson(jaa.getJSONObject(j).toString(),Kf.class);
                                        lcs.add(k);
                                        if(i == 0 && j == 0){
                                            List<String> cs = k.getLc();
                                            for (String c : cs){
                                                mCList.add(new Kf(c,c));
                                            }
                                        }
                                    }
                                }
                                if(i == 0){//默认塞入第一条的数据
                                    mSslList.addAll(lcs);
                                }
                                sslMap.put(kf.getId(),lcs);
                            }
                            qAdapter.notifyDataSetChanged();
                            lAdapter.notifyDataSetChanged();
                            cAdapter.notifyDataSetChanged();
                            mErrorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        }else{
                            mErrorLayout.setErrorType(ErrorLayout.NODATA);
                        }
                    }else{
                        mErrorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mErrorLayout.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mErrorLayout.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position,int type) {
        if(type == 1) {
            selectSsq = position;
            qAdapter.setSelected(selectSsq);
            qAdapter.notifyDataSetChanged();
            Kf k = mSsqList.get(position);
            mSslList.clear();
            selectSsl = 0;
            List<Kf> mKfs = sslMap.get(k.getId());
            mSslList.addAll(mKfs);
            lAdapter.setSelected(selectSsl);
            lAdapter.notifyDataSetChanged();
            mCList.clear();
            if (mKfs.size() > 0) {
                List<String> cs = mKfs.get(0).getLc();
                for (String c : cs) {
                    mCList.add(new Kf(c, c));
                }
            }
            cAdapter.notifyDataSetChanged();
        }else if(type == 2) {
            selectSsl  = position;
            lAdapter.setSelected(selectSsl);
            lAdapter.notifyDataSetChanged();
            Kf ssl = mSslList.get(selectSsl);
            mCList.clear();
            List<String> cs = ssl.getLc();
            for (String c : cs) {
                mCList.add(new Kf(c, c));
            }
            cAdapter.notifyDataSetChanged();
        }else if(type == 3){
            String ssqId = mSsqList.get(selectSsq).getId();
            List<Kf> mKfs = sslMap.get(ssqId);
            String sslId = mKfs.get(selectSsl).getId();
            String lcId = mKfs.get(selectSsl).getLc().get(position);

            Intent intent = new Intent(this, WsjcRecordListActivity.class);
            intent.putExtra("ssqId",ssqId);
            intent.putExtra("sslId",sslId);
            intent.putExtra("lcId",lcId);
            startActivity(intent);
        }
    }

    /**
     * 搜索
     * @param view
     */
    public void toSearch(View view){
        Intent intent = new Intent(this,SearchActivity.class);
        intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
        intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
        startActivity(intent);
    }
}
