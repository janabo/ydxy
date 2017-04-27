package com.dk.mp.xg.wsjc.ui.Sswz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.dk.mp.xg.wsjc.adapter.PeopleAdapter;
import com.dk.mp.xg.wsjc.adapter.PeopleAdapter2;
import com.dk.mp.xg.wsjc.adapter.ZssdjglSelectPersonsAdapter;
import com.dk.mp.xg.wsjc.adapter.ZssglSelectClassAdapter;
import com.dk.mp.xg.wsjc.adapter.ZssglSelectRoomAdapter;
import com.dk.mp.xg.wsjc.entity.Class;
import com.dk.mp.xg.wsjc.entity.Room;
import com.dk.mp.xg.wsjc.entity.Sswz;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/8 11:22
 */
public class SswzSelectPersonsActivity2 extends MyActivity implements View.OnClickListener{
    private TextView back,ok,title;
    private ErrorLayout mError;
    private LinearLayout mRootView;
    private RecyclerView mRecyclerView;
    PeopleAdapter2 mAdapter;
    List<Zssdjgl> mData = new ArrayList<>();
    private List<Zssdjgl> selectPersons = new ArrayList<>();
    private String fjhid;
    String mUrl="";

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

        fjhid = getIntent().getStringExtra("fjhid");
        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.ok);
        title = (TextView) findViewById(R.id.title);
        mRootView = (LinearLayout) findViewById(R.id.mRootView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.person_recycle);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new PeopleAdapter2(mContext,mData,SswzSelectPersonsActivity2.this);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        selectPersons.addAll((List<Zssdjgl>)getIntent().getSerializableExtra("persons"));

        back.setOnClickListener(new View.OnClickListener() {
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
                    showErrorMsg(mRootView,"请选择人员");
                }else{
                    ArrayList<Zssdjgl> zssdjgls = new ArrayList<Zssdjgl>();
                    for(Map.Entry<String,Object> entry : map.entrySet()){
                        Zssdjgl p = (Zssdjgl) entry.getValue();
                        if(p != null && !"addperson".equals(p.getId())){
                            zssdjgls.add(p);
                        }
                    }
                    Intent in = new Intent();
                    in.putExtra("persons",(Serializable)zssdjgls);
                    setResult(RESULT_OK, in);
                    back();
                }
            }
        });

        for(Zssdjgl z : selectPersons){
            mAdapter.getIsSelected().put(z.getId(),z);
        }

    }

    @Override
    protected void initialize() {
        super.initialize();

        title.setText("违纪学生");
        mUrl = "apps/sswzdj/xsList";

        getData();
    }

    /**
     * 获取数据
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            getList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getList(){
        Map<String,Object> map = new HashMap<>();
        map.put("fjh",fjhid);
        HttpUtil.getInstance().postJsonObjectRequest(mUrl, map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Zssdjgl> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Zssdjgl>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Zssdjgl> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mAdapter.notify(dfxxes);
                                mData.addAll(dfxxes);
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
