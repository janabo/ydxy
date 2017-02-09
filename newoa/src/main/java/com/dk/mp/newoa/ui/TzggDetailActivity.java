package com.dk.mp.newoa.ui;

import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.widget.OADetailView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通知公告详情
 * 作者：janabo on 2017/1/3 17:29
 */
public class TzggDetailActivity extends MyActivity implements View.OnClickListener{
    private OADetailView content;
    private LinearLayout lin_footer;
    private String url = "";
    private String detail="";
    private ErrorLayout mError;
    @Override
    protected int getLayoutID() {
        return R.layout.oa_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        findView();
        url = getIntent().getStringExtra("url");
        getData();
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            getOaDetail();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void findView(){
        content = (OADetailView)findViewById(R.id.content);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        lin_footer = (LinearLayout) findViewById(R.id.lin_footer);
        lin_footer.setVisibility(View.GONE);

        ViewCompat.setTransitionName(content, "detail_element");
    }

    /**
     * 获取oa详情数据
     */
    private void getOaDetail(){
        HttpUtil.getInstance().postJsonObjectRequest(url, null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null && result.getInt("code") == 200){
                        detail = result.getJSONObject("data").optString("html");
                        if(StringUtils.isNotEmpty(detail))
                            mHandler.sendEmptyMessage(0);
                        else
                            mHandler.sendEmptyMessage(-1);
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                } catch (JSONException e) {
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

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    String html = detail;
                    if("Android 4.4.2".equals(DeviceUtil.getOsType()) && html.contains("<meta name='viewport' content='initial-scale=1.0, maximum-scale=1.0, user-scalable=no' />")){
                        html = html.replace("<meta name='viewport' content='initial-scale=1.0, maximum-scale=1.0, user-scalable=no' />", "");
                    }
                    Logger.info("##############################"+html);
                    Logger.info("手机系统版本号"+DeviceUtil.getOsType());
                    content.setText(html);
                    break;
                case -1:
                    mError.setErrorType(ErrorLayout.NODATA);
                    break;
            }
        };
    };

    @Override
    public void onClick(View view) {
        getData();
    }
}
