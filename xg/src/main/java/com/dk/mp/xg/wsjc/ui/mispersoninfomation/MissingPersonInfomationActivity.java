package com.dk.mp.xg.wsjc.ui.mispersoninfomation;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.OADetailView;
import com.dk.mp.xg.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2017/5/12 09:39
 */
public class MissingPersonInfomationActivity extends MyActivity{
    private ErrorLayout mError;
    private OADetailView content;
    private String sslid;

    @Override
    protected int getLayoutID() {
        return R.layout.app_missing_person_infomation;
    }

    @Override
    protected void initView() {
        super.initView();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        content = (OADetailView) findViewById(R.id.content);
        setTitle(getIntent().getStringExtra("title"));
        sslid = getIntent().getStringExtra("sslid");
        if(DeviceUtil.checkNet()){
            mError.setErrorType(ErrorLayout.LOADDATA);
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getData(){
        Map<String,Object> map = new HashMap<>();
        map.put("sslid",sslid);
        Logger.info("sslid="+sslid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsscx/kqycryxx", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null && result.getInt("code") == 200){
                        String detail = result.getString("data");
                        Logger.info("detail="+detail);
                        if(StringUtils.isNotEmpty(detail)) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
//                            content.setText(detail);
//                            content.loadUrl(detail);
                        }else {
                            mError.setErrorType(ErrorLayout.NODATA);
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

}
