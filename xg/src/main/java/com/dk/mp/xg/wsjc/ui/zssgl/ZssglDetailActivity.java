package com.dk.mp.xg.wsjc.ui.zssgl;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.OADetailView;
import com.dk.mp.xg.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 住宿生管理详情
 * 作者：janabo on 2017/2/6 17:51
 */
public class ZssglDetailActivity extends MyActivity{
    OADetailView detailView;
    Button pass,notpass,untread;//通过，不通过，退回
    String detailid,lmlb;
    private ErrorLayout mError;
    private LinearLayout lin_footer;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("在校生住宿管理");
        detailView = (OADetailView) findViewById(R.id.content);
        pass = (Button) findViewById(R.id.pass);
        notpass = (Button) findViewById(R.id.notpass);
        untread = (Button) findViewById(R.id.untread);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        lin_footer = (LinearLayout) findViewById(R.id.lin_footer);
    }

    @Override
    protected void initialize() {
        super.initialize();
        detailid = getIntent().getStringExtra("detailid");
        lmlb = getIntent().getStringExtra("lmlb");
        getData();
    }

    /**
     * 获取数据
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            getContent();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            lin_footer.setVisibility(View.GONE);
        }
    }

    /**
     * 获取内容
     */
    public void getContent(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",detailid);
        map.put("lmlb",lmlb);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/detail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        int code = result.getInt("code");
                        if (code == 200) {
                            lin_footer.setVisibility(View.VISIBLE);

                        } else {
                            mError.setErrorType(ErrorLayout.DATAFAIL);
                            lin_footer.setVisibility(View.GONE);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                        lin_footer.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                    lin_footer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
                lin_footer.setVisibility(View.GONE);
            }
        });

    }

}
