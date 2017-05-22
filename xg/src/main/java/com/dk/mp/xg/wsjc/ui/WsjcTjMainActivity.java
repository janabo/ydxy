package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.Role;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;


/**
 * 卫生检查统计
 * 作者：janabo on 2017/1/13 10:49
 */
public class WsjcTjMainActivity extends MyActivity implements View.OnClickListener{
    private ErrorLayout mError;
    private LinearLayout content;
//    private LinearLayout bzr,xb,fdy,xgc;
    public static WsjcTjMainActivity instance;

    private CoreSharedPreferencesHelper preference;

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjctj_main;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        content = (LinearLayout)findViewById(R.id.maincontext);
        instance = this;
//        bzr = (LinearLayout) findViewById(bzr);
//        xb = (LinearLayout) findViewById(xb);
//        fdy = (LinearLayout) findViewById(fdy);
//        xgc = (LinearLayout) findViewById(xgc);
    }

    @Override
    protected void initialize() {
        super.initialize();
        preference = getSharedPreferences();
        getData();
    }

//    /**
//     * 班主任统计界面
//     * @param v
//     */
//    public void tobzr(View v){
//        toDetail("1");
//    }
//
//    /**
//     * 系部统计界面
//     * @param v
//     */
//    public void toxb(View v){
//        toDetail("3");
//    }
//
//    /**
//     * 学工处统计界面
//     * @param v
//     */
//    public void toxgc(View v){
//        toDetail("4");
//    }
//
//    /**
//     * 辅导员统计界面
//     * @param v
//     */
//    public void tofdy(View v){
//        toDetail("2");
//    }

    public void getData(){
        if(DeviceUtil.checkNet()){
            getList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    private void getList() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/sswsdftj/role", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                     if(result != null) {
                        GsonData<Role> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Role>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            final List<Role> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                if(dfxxes.size() == 1) {//如果只有一g个角色数据之间跳转响应界面
                                    toDetail(dfxxes.get(0).getId());
                                    finish();
                                }else{
                                    int i=0;
                                    for(final Role r : dfxxes){
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
                                        if (i == dfxxes.size()-1){
                                            params.setMargins(0,0,0,0);
                                        } else {
                                            params.setMargins(0,0,0, StringUtils.dip2px(mContext,10));
                                        }
                                        i++;
                                        View view = getLayoutInflater().inflate(R.layout.app_wsjc_main_item, null);
                                        view.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                               toDetail(r.getId());
                                            }
                                        });
                                        LinearLayout linearboder = (LinearLayout) view.findViewById(R.id.lineborder);
                                        TextView text = (TextView) view.findViewById(R.id.linebordertext);
                                        TextView name = (TextView) view.findViewById(R.id.linebordername);
                                        switch (r.getId()) {
                                            case "1": {
                                                linearboder.setBackgroundResource(R.drawable.tro_blue_entry_style);
                                                text.setText("班");
                                                name.setText("班主任视角");
                                                break;
                                            }
                                            case "2": {
                                                linearboder.setBackgroundResource(R.drawable.tro_org_entry_style);
                                                text.setText("辅");
                                                name.setText("辅导员视角");
                                                break;
                                            }
                                            case "3": {
                                                linearboder.setBackgroundResource(R.drawable.tro_green_entry_style);
                                                text.setText("系");
                                                name.setText("系部视角");
                                                break;
                                            }
                                            case "4": {
                                                linearboder.setBackgroundResource(R.drawable.tro_yel_entry_style);
                                                text.setText("学");
                                                name.setText("学工处视角");
                                                break;
                                            }
                                            default:
                                                break;
                                        }
                                        content.addView(view,params);
                                    }
                                }
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
     * 跳转界面
     * @param role
     */
    public void toDetail(String role){
        if (role.equals("4")){
            String tjSslId = preference.getValue("tjSslId");
            if ( tjSslId == null || tjSslId == ""){
                Intent intent = new Intent(this,WsjcChooseSslActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this,WsjcTjTabActivity.class);
                intent.putExtra("role",role);
                intent.putExtra("title",getIntent().getStringExtra("title"));
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(this,WsjcTjTabActivity.class);
            intent.putExtra("role",role);
            intent.putExtra("title",getIntent().getStringExtra("title"));
            startActivity(intent);
        }

    }


}
