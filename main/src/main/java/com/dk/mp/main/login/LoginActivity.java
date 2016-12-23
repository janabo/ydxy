package com.dk.mp.main.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.encrypt.Base64Utils;
import com.dk.mp.core.view.ValidationCode;
import com.dk.mp.core.view.edittext.CleanEditText;
import com.dk.mp.main.R;
import com.dk.mp.main.setting.ui.SettingActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 * 作者：janabo on 2016/12/14 17:42
 */
public class LoginActivity extends MyActivity implements View.OnClickListener{
    ValidationCode valicode;//验证码
    LinearLayout putview;
    CleanEditText uid;//用户名
    CleanEditText psw;//密码
    CleanEditText yzm;//验证码
    TextView showPwsText;//显示完整密码
    LinearLayout yzm_lin;
    View yzm_view;
    Button ok;//提交按钮
    int yzmcount = 1;
    private boolean flag = false;
    private CoreSharedPreferencesHelper preference;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_login;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("登录");
        preference = getSharedPreferences();
        valicode = (ValidationCode) findViewById(R.id.valicode);
        putview = (LinearLayout) findViewById(R.id.putview);
        uid = (CleanEditText) findViewById(R.id.uid);
        psw = (CleanEditText) findViewById(R.id.psw);
        yzm = (CleanEditText) findViewById(R.id.yzm);
        yzm_view = findViewById(R.id.yzm_view);
        yzm_lin = (LinearLayout) findViewById(R.id.yzm_lin);
        showPwsText = (TextView) findViewById(R.id.showPwsText);
        ok = (Button) findViewById(R.id.ok);
        valicode.setOnClickListener(this);
        yzmcount = preference.getInt("yzmcount");
        if(yzmcount >2){//错误两次显示验证码
            yzm_lin.setVisibility(View.VISIBLE);
            yzm_view.setVisibility(View.VISIBLE);
            getYzm();
        }
        uid.addTextChangedListener(mTextWatcher);
        psw.addTextChangedListener(mTextWatcher);
        yzm.addTextChangedListener(mTextWatcher);
    }

    /**
     * 点击重新获取验证码
     * @param v
     */
    @Override
    public void onClick(final View v) {
        hideKeyb(v);
        getYzm();
    }

    private void getYzm(){
        HttpUtil.getInstance().getGsonRequestJson(JsonData.class, "http://192.168.3.127:8082/mp/getRadomCode",
            new HttpListener<JsonData>() {
                @Override
                public void onSuccess(JsonData result) {
                    double code = (Double)result.getData();
                    valicode.setText((int)code+"",mContext);
                }
                @Override
                public void onError(VolleyError error) {
                    SnackBarUtil.showShort(putview,"获取验证码失败，请重新获取");
                }
            });
    }

    /**
     * 登录操作
     * @param v
     */
    public void tologin(View v){
        final String userId = uid.getText().toString().trim();
        final String pass = psw.getText().toString().trim();
        if(userId.length()<=0 || pass.length()<=0){//用户名密码不能为空
            return;
        }
        String mYzm = yzm.getText().toString().trim();
        if(yzm_lin.getVisibility() == View.VISIBLE && mYzm.length()<=0){//需要填验证码的话，验证码不能为空
            return;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", Base64Utils.getBase64(pass));
        if(yzm_lin.getVisibility() == View.VISIBLE){
            map.put("radomCode", mYzm);
        }

        hideKeyb(v);

        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.127:8082/mp/login", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        if(yzmcount == -1){
                            yzmcount = 0;
                        }else if(yzmcount>=2){
                            yzm_lin.setVisibility(View.VISIBLE);
                            yzm_view.setVisibility(View.VISIBLE);
                        }
                        preference.setInt("yzmcount",yzmcount++);
                        SnackBarUtil.showShort(putview,result.getString("msg"));
                        getYzm();
                    }else{
                        preference.setInt("yzmcount",0);
                        preference.setLoginMsg(userId,Base64Utils.getBase64(pass));
                        preference.setUserInfo(result.getJSONObject("data").toString());
                        if(SettingActivity.instance != null){
                            SettingActivity.instance.finish();
                        }
                        back();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    SnackBarUtil.showShort(putview,getReString(R.string.login_fail));
                }
            }

            @Override
            public void onError(VolleyError error) {
                SnackBarUtil.showShort(putview,getReString(R.string.login_fail));
            }
        });

//        HttpUtil.getInstance().gsonRequestJson(JsonData.class, "http://192.168.3.127:8082/mp/login", map, new HttpListener<JsonData>() {
//            @Override
//            public void onSuccess(JsonData result) {
//                    if(result.getCode() != 200){
//                        if(yzmcount == -1){
//                            yzmcount = 0;
//                        }else if(yzmcount>=2){
//                            yzm_lin.setVisibility(View.VISIBLE);
//                            yzm_view.setVisibility(View.VISIBLE);
//                        }
//                        preference.setInt("yzmcount",yzmcount++);
//                        SnackBarUtil.showShort(putview,result.getMsg());
//                        getYzm();
//                    }else{
//                        preference.setInt("yzmcount",0);
//                        SnackBarUtil.showShort(putview,result.getMsg());
//                }
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                SnackBarUtil.showShort(putview,getReString(R.string.data_fail));
//            }
//        });

    }

    /**
     * 显示完整密码
     * @param v
     */
    public void showPws(View v){
        if (flag) {
            flag = false;
            psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPwsText.setText("显示密码");
        } else {
            flag = true;
            psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPwsText.setText("隐藏密码");
        }
    }


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void afterTextChanged(Editable s) {
            String userId = uid.getText().toString().trim();
            String pass = psw.getText().toString().trim();
            String mYzm = yzm.getText().toString().trim();
            if(userId.length()>0 && pass.length()>0 && (yzm_lin.getVisibility()
                    == View.GONE || (yzm_lin.getVisibility() == View.VISIBLE && mYzm.length()>0))){
                ok.setBackground(getDrawable(R.drawable.ripple_bg));
            }else{
                ok.setBackgroundColor(getColor(R.color.rcap_gray));
            }
        }
    };

    /**
     * 隐藏键盘
     * @param v
     */
    private void hideKeyb(View v){
        InputMethodManager imm = (InputMethodManager) LoginActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//收起虚拟键盘
    }
}
