package com.dk.mp.core.ui;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.R;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dongqs on 2017/2/14.
 */

public class SubmitButton extends LinearLayout implements View.OnClickListener{

    TextView ok;//提交按钮
    LinearLayout ok_lin;
    DrawHookView progress;//提交动画
    DrawCheckMarkView progress_check;//成功动画
    DrawCrossMarkView progress_cross;//错误动画
    Context context;
    SubmitInterface submitInterface;
    Handler handler=new Handler();

    public SubmitButton(Context context) {
        super(context);
        this.context = context;
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.core_submitbutton, this, true);
        initViews();
    }

    private void initViews(){
        ok_lin = (LinearLayout) findViewById(R.id.ok);
        ok_lin.setOnClickListener(this);
        ok = (TextView) findViewById(R.id.ok_text);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
    }

    /**
     * 错误提示和动画
     */
    private void errorInfo(){
        progress.setVisibility(View.GONE);
        progress_cross.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {//等待成功动画结束
            @Override
            public void run() {
                progress_cross.setVisibility(View.GONE);
                ok.setVisibility(View.VISIBLE);
                ok_lin.setEnabled(true);
            }
        },1000);
    }

    @Override
    public void onClick(View view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!submitInterface.beforeLoad()) {
                    return;
                }
                ok_lin.setEnabled(false);//设置不能点击
                ok.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                String url = submitInterface.setUrl();
                Map<String , Object> map = submitInterface.setMap();
                HttpUtil.getInstance().postJsonObjectRequest(url, map, new HttpListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if (result.getInt("code") != 200) {
                                ((MyActivity)context).showErrorMsg(result.getString("msg"));
                                errorInfo();
                            }else{
                                progress.setVisibility(View.GONE);
                                progress_check.setVisibility(View.VISIBLE);
                                ((MyActivity)context).setResult(RESULT_OK, null);
                                new Handler().postDelayed(new Runnable() {//等待成功动画结束
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        ok_lin.setEnabled(true);
                                        ((MyActivity)context).back();
                                    }
                                },2000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorInfo();
                        }
                    }
                    @Override
                    public void onError(VolleyError error) {
                        errorInfo();
                    }
                });
            }
        },500);
    }

    public void setSubmitInterface(SubmitInterface submitInterface) {
        this.submitInterface = submitInterface;
    }
}
