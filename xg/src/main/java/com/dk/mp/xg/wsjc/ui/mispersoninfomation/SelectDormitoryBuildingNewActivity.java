package com.dk.mp.xg.wsjc.ui.mispersoninfomation;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.SelectDormitoryBuildingNewAdapter;
import com.dk.mp.xg.wsjc.entity.DormitoryBuilding;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzWjrqPickActivity;
import com.dk.mp.xg.wsjc.util.SpacesItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择宿舍楼和日期
 * 作者：janabo on 2017/9/15 13:57
 */
public class SelectDormitoryBuildingNewActivity extends MyActivity implements SelectDormitoryBuildingNewAdapter.OnItemClickListener{
    private LinearLayout mSDate;//选择日期按钮
    private TextView mTime;//显示日期
    private RecyclerView mRecyclerView;//gridview
    private LinearLayout ok;//提交按钮
    private SelectDormitoryBuildingNewAdapter mAdapter;
    List<DormitoryBuilding> mData = new ArrayList<>();
    private ErrorLayout mError;//错误提示
    private String mTitle;
    private CoreSharedPreferencesHelper preference;
    LoginMsg loginMsg;

    @Override
    protected int getLayoutID() {
        return R.layout.app_wdrycx_xzlh;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        preference = getSharedPreferences();
        loginMsg = preference.getLoginMsg();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mSDate = (LinearLayout) findViewById(R.id.select_date);
        mTime = (TextView) findViewById(R.id.mtime);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ok = (LinearLayout) findViewById(R.id.ok);
        ok.setEnabled(false);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new GridLayoutManager( mContext,2));
        mAdapter = new SelectDormitoryBuildingNewAdapter(mContext,mData);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(DeviceUtil.dip2px(mContext, 10)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTitle = getIntent().getStringExtra("title");

        mSDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPickRq();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map =  mAdapter.getIsSelected();
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
                startActivity(in);
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
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
                                mData.addAll(dfxxes);
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


    /**
     * 选择查询的日期
     */
    public void toPickRq(){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String mDate = data.getStringExtra("date");
                    mTime.setText(mDate);
                    dealOkButtonUi();
                }
                break;
        }
    }

    /**
     * 处理确定按钮的背景色和是否可点击
     */
    public void dealOkButtonUi(){
        if(StringUtils.isNotEmpty(mTime.getText().toString()) && !SelectDormitoryBuildingNewAdapter.getIsSelected().isEmpty()){
            ok.setEnabled(true);
            ok.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else{
            ok.setEnabled(false);
            ok.setBackgroundColor(getResources().getColor(R.color.rcap_gray));
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        DormitoryBuilding db = mData.get(position);
        if(mAdapter.getIsSelected().get(db.getId()) != null){
            mAdapter.getIsSelected().remove(db.getId());
        }else {
            mAdapter.getIsSelected().clear();
            mAdapter.getIsSelected().put(db.getId(), db.getName());
        }
        dealOkButtonUi();
        mAdapter.notifyDataSetChanged();
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
