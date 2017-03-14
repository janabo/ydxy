package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.ZssglSelectTzyyAdapter;
import com.dk.mp.xg.wsjc.entity.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择调整原因   ·
 * 作者：janabo on 2017/3/9 19:45
 */
public class ZssglSelectTzyyActivity extends MyActivity implements View.OnClickListener{
    private TextView back,ok,title;
    private LinearLayout mRootView;
    private ErrorLayout mError;
    private RecyclerView mRecyclerView;
    ZssglSelectTzyyAdapter mAdapter;
    List<Common> mDatas = new ArrayList<>();
    private String ssqid,tzyyid;
    Map<String,String> tzyyMap = new HashMap<>();

    @Override
    protected int getLayoutID() {
        return R.layout.app_ssws_selectperson;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.select_title));
        }
        tzyyid = getIntent().getStringExtra("tzyyid");
        if(StringUtils.isNotEmpty(tzyyid)){
            String[] yy = tzyyid.split(",");
            for(int i=0;i<yy.length;i++){
                if(StringUtils.isNotEmpty(yy[i])){
                    tzyyMap.put(yy[i],"true");
                }
            }
        }
        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.ok);
        title = (TextView) findViewById(R.id.title);
        mRootView = (LinearLayout) findViewById(R.id.mRootView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.person_recycle);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );

        mAdapter = new ZssglSelectTzyyAdapter(mContext,mDatas,ZssglSelectTzyyActivity.this,tzyyMap);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                back();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Map<String,Object> map =  mAdapter.getIsSelected();
                if(map.isEmpty()){
                    showErrorMsg(mRootView,"请选择调整原因");
                }else{
                    String tzyyid="";
                    String tzyyname="";
                    for(Map.Entry<String,Object> entry : map.entrySet()){
                        tzyyid += entry.getKey()+",";
                        tzyyname += entry.getValue()+",";
                    }
                    Intent in = new Intent();
                    in.putExtra("tzyyid",tzyyid.substring(0,tzyyid.length()-1));
                    in.putExtra("tzyyname",tzyyname.substring(0,tzyyname.length()-1));
                    setResult(RESULT_OK, in);
                    back();
                }
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        title.setText("调整原因");
        ssqid = getIntent().getStringExtra("ssqid");
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
        map.put("ssq",ssqid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tzyyList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mAdapter.notify(dfxxes);
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
     * 处理确定按钮
     */
    public void dealOK(boolean flag){
        if(flag){
            ok.setEnabled(true);
            ok.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            ok.setEnabled(false);
            ok.setTextColor(getResources().getColor(R.color.colorPrimary50));
        }
    }

    @Override
    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
