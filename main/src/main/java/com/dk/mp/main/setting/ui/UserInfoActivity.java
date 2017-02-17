package com.dk.mp.main.setting.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.entity.User;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.main.R;

/**
 * 用户信息
 * 作者：janabo on 2016/12/14 17:43
 */
public class UserInfoActivity extends MyActivity{
    private ImageView photo;
    private TextView username, depart, xh,xhorgh,yxorbm;
    private Context context = UserInfoActivity.this;
    private CoreSharedPreferencesHelper h;
    private Button loginout;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_userinfo;
    }

    @Override
    protected void initialize() {
        super.initialize();
        h = new CoreSharedPreferencesHelper(this);
        setTitle("个人资料");
        findView();
        dd();
    }

    /**
     * 初始化控件.
     */
    private void findView() {
        photo = (ImageView) findViewById(R.id.photo);
        username = (TextView) findViewById(R.id.username);
        depart = (TextView) findViewById(R.id.depart);
        xh = (TextView) findViewById(R.id.xh);
        xhorgh = (TextView) findViewById(R.id.xhorgh);
        yxorbm = (TextView) findViewById(R.id.yxorbm);
        loginout = (Button) findViewById(R.id.loginout);
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOut();
            }
        });
    }

    /**
     * 填充个人信息到界面.
     */
    private void dd() {
        User user= h.getUser();
        if (user != null) {
            username.setText(StringUtils.checkEmpty(user.getUserName()));
            depart.setText(StringUtils.checkEmpty(user.getDepartName()));
            xh.setText(StringUtils.checkEmpty(user.getUserId()));
            if("teacher".equals(user.getRoles())){
                xhorgh.setText("工号");
                yxorbm.setText("部门");
            }else{
                xhorgh.setText("学号");
                yxorbm.setText("院系");
            }

            if(StringUtils.isNotEmpty(user.getPhoto())){
                photo.setImageURI(Uri.parse(user.getPhoto()));
            }
        }
    }

    /**
     * 处理登录.
     */
    private void loginOut() {
        HttpUtil.getInstance().getGsonRequestJson(JsonData.class, "logout", new HttpListener<JsonData>() {
            @Override
            public void onSuccess(JsonData result) {
            }
            @Override
            public void onError(VolleyError error) {
            }
        });
        h.cleanUser();
        h.setValue("nick", null);
        BroadcastUtil.sendBroadcast(context, "user");
        back();
    }
}
