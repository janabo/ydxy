package com.dk.mp.xg.wsjc.ui.mispersoninfomation;

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
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.SelectDormitoryBuildingAdapter;
import com.dk.mp.xg.wsjc.entity.DormitoryBuilding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择宿舍楼
 * 作者：janabo on 2017/5/12 10:07
 */
public class SelectDormitoryBuildingActivity extends MyActivity implements View.OnClickListener{
    private TextView back,ok,title;
    private LinearLayout mRootView;
    private ErrorLayout mError;
    private RecyclerView mRecyclerView;
    SelectDormitoryBuildingAdapter mAdapter;
    List<DormitoryBuilding> mData = new ArrayList<>();
    private String mTitle;
    LoginMsg loginMsg;
    private CoreSharedPreferencesHelper preference;


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
        preference = getSharedPreferences();
        loginMsg = preference.getLoginMsg();
        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.ok);
        title = (TextView) findViewById(R.id.title);
        mRootView = (LinearLayout) findViewById(R.id.mRootView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.person_recycle);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ));
        mAdapter = new SelectDormitoryBuildingAdapter(mContext,mData,SelectDormitoryBuildingActivity.this);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTitle = getIntent().getStringExtra("title");
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
                    showErrorMsg(mRootView,"请选择宿舍楼");
                }else{
                    String sslid = "";
                    for(Map.Entry<String,Object> entry : map.entrySet()){
                        sslid = entry.getKey();
                        if(StringUtils.isNotEmpty(sslid)){
                            break;
                        }
                    }

                    Intent in = new Intent(mContext, MissingPersonActivity.class);
                    in.putExtra("title",mTitle);
                    in.putExtra("url",getUrl(sslid));
//                    Intent in = new Intent(mContext,MissingPersonInfomationActivity.class);
//                    in.putExtra("title",mTitle);
//                    in.putExtra("sslid",sslid);
                    startActivity(in);
                }
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        title.setText("选择宿舍楼");
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
//        apps/sswsdftj/ssl                           apps/zxzssgl/sslList
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/sslList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<DormitoryBuilding> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<DormitoryBuilding>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<DormitoryBuilding> dfxxes = gsonData.getData();
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

//    @Override
//    public void back() {
//        finish();
//        overridePendingTransition(0, R.anim.push_down_out);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public String getUrl(String sslId){
        String mUrl = mContext.getString(R.string.rootUrl)+"apps/zsscx/kqycryxx?sslid="+sslId;
        if(loginMsg != null){
            mUrl += "&uid="+loginMsg.getUid()+"&pwd="+ loginMsg.getPsw()+"&userId="+loginMsg.getUid();
        }
        Logger.info("######murl="+mUrl);
        return mUrl;
    }
}
