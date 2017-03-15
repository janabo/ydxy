package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Student;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzWjrqPickActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 停宿申请
 * 作者：janabo on 2017/1/19 17:23
 */
public class ZsstjAddTingSuActivity extends MyActivity implements View.OnClickListener{
    private TextView jsrq_pick,ksrq_pick;//结束日期，开始日期
    private EditText tsyy;//停宿原因
    private ScrollView mRootView;
    private LinearLayout ok;
    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;
    private TextView wjxs_x,wjxs_name,ok_text;//退宿原因
    private ImageView wjxs_img;
    private LinearLayout wjxs_lin;//退宿原因
    Student student =null;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_add_tingsu;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("学生停宿申请");
    }

    @Override
    protected void initView() {
        super.initView();
        wjxs_img = (ImageView) findViewById(R.id.wjxs_img);
        wjxs_lin = (LinearLayout) findViewById(R.id.wjxs_lin);
        wjxs_x = (TextView) findViewById(R.id.wjxs_x);
        wjxs_name = (TextView) findViewById(R.id.wjxs_name);
        jsrq_pick = (TextView) findViewById(R.id.jsrq_pick);
        ksrq_pick = (TextView) findViewById(R.id.ksrq_pick);
        tsyy = (EditText) findViewById(R.id.tsyy);
        mRootView = (ScrollView) findViewById(R.id.mRootView);
        ok = (LinearLayout) findViewById(R.id.ok);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
        ok_text = (TextView) findViewById(R.id.ok_text);
        jsrq_pick.addTextChangedListener(mTextWatcher);
        ksrq_pick.addTextChangedListener(mTextWatcher);
        tsyy.addTextChangedListener(mTextWatcher);
        wjxs_lin.setOnClickListener(this);

        ok.setEnabled(false);
    }

    /**
     * 增加提报人
     * @param view
     */
    public void addWjxs(View view){
        Intent intent = new Intent(mContext, ZssglSelectPersonsActivity.class);
        intent.putExtra("lmlb","1");
        intent.putExtra("categoryTitle","停宿学生");
        startActivityForResult(intent, 3);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    /**
     * 停宿开始日期
     * @param v
     */
    public void toPickKfyy(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    /**
     * 停宿结束日期
     * @param v
     */
    public void toPickJsrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    ksrq_pick.setText(mWjrq);
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    jsrq_pick.setText(mWjrq);
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    student = (Student) data.getSerializableExtra("student");
                    if (student != null) {
                        wjxs_name.setText(student.getXm());
                        wjxs_x.setText(student.getXm().substring(0, 1));
                        dealOkButton();
                        getBackgroud(wjxs_lin,student.getXm().substring(0, 1));
                    }
                    wjxs_lin.setVisibility(View.VISIBLE);
                    wjxs_name.setVisibility(View.VISIBLE);
                    wjxs_img.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 提交停宿
     * @param v
     */
    public void submitTingsu(View v){
        if(ksrq_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择停宿开始日期");
            return;
        }
        if(jsrq_pick.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请选择停宿结束日期");
            return;
        }
        if(tsyy.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请填写停宿原因");
            return;
        }
        if(tsyy.getText().toString().length()>200){
            showErrorMsg(mRootView,"停宿原因不能大于200个字");
            return;
        }

        if(!TimeUtils.comparedDate(ksrq_pick.getText().toString(),jsrq_pick.getText().toString())){
            showErrorMsg("停宿开始时间不能大于停宿结束时间");
            return ;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("userIdString",student.getXsid());//学生id
        map.put("tsyy",tsyy.getText().toString());//停宿原因
        map.put("ksrq",ksrq_pick.getText().toString());//开始日期
        map.put("jsrq",jsrq_pick.getText().toString());//结束日期

        ok_text.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tingsusq", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                    if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                        progress.setVisibility(View.GONE);
                        progress_check.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {//等待成功动画结束
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                ok.setEnabled(true);
                                BroadcastUtil.sendBroadcast(mContext, "zssgl_refresh");
                                finish();
                            }
                        },1000);
                    }else{
                        SnackBarUtil.showShort(mRootView,jd.getMsg());
                        errorInfo();
                    }
                }catch (Exception e){
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


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            dealOkButton();
        }
    };

    /**
     * 设置ok按钮的样式
     */
    public void dealOkButton(){
        if(student != null && jsrq_pick.getText().toString().length()>0 && ksrq_pick.getText().toString().length()>0 && tsyy.getText().toString().length()>0){
            ok.setBackground(getResources().getDrawable(R.drawable.ripple_bg));
            ok.setEnabled(true);
        }else{
            ok.setBackgroundColor(getResources().getColor(R.color.rcap_gray));
            ok.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        if(R.id.wjxs_lin == view.getId()){//违纪学生
            wjxs_lin.setVisibility(View.GONE);
            wjxs_name.setVisibility(View.GONE);
            wjxs_img.setVisibility(View.VISIBLE);
            dealOkButton();
        }
    }

    private void errorInfo(){
        progress.setVisibility(View.GONE);
        progress_cross.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {//等待成功动画结束
            @Override
            public void run() {
                progress_cross.setVisibility(View.GONE);
                ok_text.setVisibility(View.VISIBLE);
                ok.setEnabled(true);
            }
        },1000);
    }

    @Override
    public void back() {
        new AlertDialog(mContext).show(null, "确定退出停宿申请？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ZsstjAddTingSuActivity.super.back();
            }
        });
    }
}
