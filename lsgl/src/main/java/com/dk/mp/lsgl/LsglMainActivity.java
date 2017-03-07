package com.dk.mp.lsgl;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.lsgl.entity.RoleEntity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dongqs on 2017/1/23.
 */

public class LsglMainActivity extends MyActivity implements View.OnClickListener{

//    private LinearLayout bzr;
//    private LinearLayout xb;
//    private LinearLayout xgc;
//    private LinearLayout fdy;
    private LinearLayout content;

    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.app_lsgl_main;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("留宿学生管理");
        initViews();
        loadDatas();
    }

    private void initViews() {
        content = (LinearLayout)findViewById(R.id.maincontext);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
    }

    private void loadDatas() {
        mError.setErrorType(ErrorLayout.LOADDATA);
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/role", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<RoleEntity> roles = getGson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<RoleEntity>>(){}.getType());
                        if (roles == null){
                            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                        } else if (roles.size() == 0) {
                            mError.setErrorType(ErrorLayout.NODATA);
                        } else {
                            if (roles.size() == 1) {
                                Intent intent = new Intent(LsglMainActivity.this,LsglTabActivity.class);
                                intent.putExtra("role", roles.get(0).getId());
                                startActivity(intent);
                                finish();
                            } else {
                                for (int i=0;i<roles.size();i++) {
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
                                    final RoleEntity role = roles.get(i);
                                    if (i == roles.size()-1){
                                        params.setMargins(0,0,0,0);
                                    } else {
                                        params.setMargins(0,0,0,StringUtils.dip2px(LsglMainActivity.this,10));
                                    }
                                    View view = getLayoutInflater().inflate(R.layout.app_lsgl_main_item, null);
                                    view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            doStartActivity(role.getId(),view);
                                        }
                                    });
                                    LinearLayout linearboder = (LinearLayout) view.findViewById(R.id.lineborder);
                                    TextView text = (TextView) view.findViewById(R.id.linebordertext);
                                    TextView name = (TextView) view.findViewById(R.id.linebordername);
                                    switch (role.getId()){
                                        case "1" :{
                                            linearboder.setBackgroundResource(R.drawable.tro_blue_entry_style);
                                            text.setText("班");
                                            name.setText("班主任视角");
                                            break;
                                        }
                                        case "2" : {
                                            linearboder.setBackgroundResource(R.drawable.tro_org_entry_style);
                                            text.setText("辅");
                                            name.setText("辅导员视角");
                                            break;
                                        }
                                        case "3" : {
                                            linearboder.setBackgroundResource(R.drawable.tro_green_entry_style);
                                            text.setText("系");
                                            name.setText("系部视角");
                                            break;
                                        }
                                        case "4" : {
                                            linearboder.setBackgroundResource(R.drawable.tro_yel_entry_style);
                                            text.setText("学");
                                            name.setText("学工处视角");
                                            break;
                                        }
                                        default : break;
                                    }
                                    content.addView(view,params);
                                }
                            }
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        }
                    } catch (JSONException e) {
                        mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        });
    }

    android.os.Handler handler=new android.os.Handler();
    boolean isposting = false;

    private void doStartActivity(final String role , final View view){
        if (!isposting) {
            isposting = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LsglMainActivity.this,LsglTabActivity.class);
                    intent.putExtra("role",role);
                    intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
                    intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(LsglMainActivity.this,40));
                    startActivity(intent);
                    isposting = false;
                }
            },500);
        }
    }

    @Override
    public void onClick(View view) {
        loadDatas();
    }
}
