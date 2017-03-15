package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.xg.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 住宿生管理提交
 * 作者：janabo on 2017/2/22 09:51
 */
public class ZssglSubmitActivity extends MyActivity {
    EditText mark;
    TextView cancel,set;
    TextView mTitle;
    RelativeLayout mRootView;
    String detailid,flag,url,flagName;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_submit;
    }

    @Override
    protected void initView() {
        super.initView();
        mark = (EditText) findViewById(R.id.mark);
        cancel = (TextView) findViewById(R.id.cancel);
        set = (TextView) findViewById(R.id.set);
        mTitle = (TextView) findViewById(R.id.mTitle);
        mark.addTextChangedListener(mTextWatcher);
        mRootView = (RelativeLayout) findViewById(R.id.mRootView);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                            0);
                }
                    if(mark.getText().toString().length()<=0){
                    SnackBarUtil.showShort(mRootView,"请输入审核意见");
                }else if(mark.getText().toString().length()>500){
                    SnackBarUtil.showShort(mRootView,"审核意见不能大于500个字");
                }else{
                    submit();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.push_down_out);
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        detailid = getIntent().getStringExtra("detailid");
        flag = getIntent().getStringExtra("flag");
        url = getIntent().getStringExtra("url");
        flagName = getIntent().getStringExtra("flagName");
        mTitle.setText(flagName);

        mark.setFocusable(true);
        mark.setFocusableInTouchMode(true);
        mark.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)mark.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mark,0);
    }

    public void submit(){
        Map<String,Object> map = new HashMap<>();
        map.put("bz",mark.getText().toString());
        map.put("id",detailid);
        map.put("flag",flag);
        HttpUtil.getInstance().postJsonObjectRequest(url, map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    JsonData jd = new Gson().fromJson(result.toString(), JsonData.class);
                    if(jd.getCode() == 200 && (Boolean) jd.getData()){
                        SnackBarUtil.showShort(mRootView,jd.getMsg());
                        BroadcastUtil.sendBroadcast(mContext, "zssgl_refresh");
                        if(ZssglDetailActivity.instance != null){
                            ZssglDetailActivity.instance.finish();
                        }
                        finish();
                        overridePendingTransition(0, R.anim.push_down_out);
                    }else{
                        SnackBarUtil.showShort(mRootView,jd.getMsg());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    SnackBarUtil.showShort(mRootView,getResources().getString(R.string.data_fail));
                }
            }

            @Override
            public void onError(VolleyError error) {
                SnackBarUtil.showShort(mRootView,getResources().getString(R.string.data_fail));
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(mark.getText().toString().length()>0){
                set.setTextColor(getResources().getColor(R.color.colorPrimary));
                set.setEnabled(true);
            }else{
                set.setTextColor(getResources().getColor(R.color.colorPrimary50));
                set.setEnabled(false);
            }
        }
    };

    @Override
    public void back() {
        new AlertDialog(mContext).show(null, "确定退出审核？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ZssglSubmitActivity.super.back();
            }
        });
    }
}
