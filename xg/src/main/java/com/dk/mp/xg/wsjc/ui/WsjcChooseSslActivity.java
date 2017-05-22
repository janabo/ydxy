package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.WsjcChooseSslAdapter;
import com.dk.mp.xg.wsjc.entity.ChooseSsl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cobb on 2017/5/8.
 */

public class WsjcChooseSslActivity extends MyActivity {

    private TextView back,ok,title;
    private LinearLayout mRootView;
    private ErrorLayout mError;
    private RecyclerView mRecyclerView;

    WsjcChooseSslAdapter mAdapter;
    List<ChooseSsl> mData = new ArrayList<>();

    private CoreSharedPreferencesHelper preference;


    @Override
    protected int getLayoutID() {
        return R.layout.wsjc_choose_ssl;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        preference = getSharedPreferences();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.select_title));
        }

        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.ok);
        title = (TextView) findViewById(R.id.title);
        mRootView = (LinearLayout) findViewById(R.id.mRootView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.person_recycle);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new WsjcChooseSslAdapter(mContext,mData,WsjcChooseSslActivity.this);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        title.setText("选择宿舍楼");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                back();
                overridePendingTransition(0,R.anim.push_down_out);
                tjintent();
            }
        });

        if (getIntent().getStringExtra("styles") != null && getIntent().getStringExtra("styles").equals("0")){
            dealOK(true);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tjintent();
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();

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
       // apps/sswsdftj/ssl
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/sslList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<ChooseSsl> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<ChooseSsl>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<ChooseSsl> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mAdapter.notify(dfxxes);
                                mData.addAll(dfxxes);

                                if (preference.getValue("tjSslId") != null){
                                    for (ChooseSsl s : mData){
                                        mAdapter.getIsSelected().put(preference.getValue("tjSslId"),s);
                                    }
                                }

                                mAdapter.notifyDataSetChanged();
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
//        super.back();
//        finish();
//        overridePendingTransition(0,R.anim.push_down_out);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            back();
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    public void tjintent(){
        Map<String,Object> map =  mAdapter.getIsSelected();
        if(map.isEmpty()){
            showErrorMsg(mRootView,"请选择宿舍楼");
        }else{
            ChooseSsl s = null;
            for(Map.Entry<String,Object> entry : map.entrySet()){
                s = (ChooseSsl) entry.getValue();
                if(s != null){
                    preference.setValue("tjSslId",s.getId());
                    preference.setValue("tjSslName",s.getName());
                    Intent intent = new Intent(getApplicationContext(),WsjcTjTabActivity.class);
                    intent.putExtra("role","4");
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
    }
}
