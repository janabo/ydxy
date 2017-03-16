package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Common;
import com.dk.mp.xg.wsjc.entity.Student;
import com.dk.mp.xg.wsjc.ui.WsjcTjWeekPickActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退宿申请
 * 作者：janabo on 2017/1/19 16:27
 */
public class ZssglAddTuiSuActivity extends MyActivity implements View.OnClickListener{
    private ScrollView mRootView;
    private ImageView wjxs_img;
    private LinearLayout wjxs_lin,wskfyx,ok;//退宿原因
    private TextView wjxs_x,wjxs_name,dfxx_pick,ok_text;//退宿原因
    private EditText sqly,bz,shyj;//申请理由,备注
    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;
    private List<Common> tsyys = new ArrayList<>();
    private String tsyyid="";
    Student student =null;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_add_tuisu;
    }

    @Override
    protected void initView() {
        super.initView();
        mRootView = (ScrollView) findViewById(R.id.mRootView);
        wjxs_img = (ImageView) findViewById(R.id.wjxs_img);
        wjxs_lin = (LinearLayout) findViewById(R.id.wjxs_lin);
        wjxs_lin.setOnClickListener(this);
        wskfyx = (LinearLayout) findViewById(R.id.wskfyx);
        ok = (LinearLayout) findViewById(R.id.ok);
        wjxs_x = (TextView) findViewById(R.id.wjxs_x);
        wjxs_name = (TextView) findViewById(R.id.wjxs_name);
        dfxx_pick = (TextView) findViewById(R.id.dfxx_pick);
        ok_text = (TextView) findViewById(R.id.ok_text);
        sqly = (EditText) findViewById(R.id.sqly);
        bz = (EditText) findViewById(R.id.bz);
        shyj = (EditText) findViewById(R.id.shyj);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);

        sqly.addTextChangedListener(mTextWatcher);
        shyj.addTextChangedListener(mTextWatcher);
        dfxx_pick.addTextChangedListener(mTextWatcher);
        ok.setEnabled(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("学生退宿申请");
        getTuisu();
    }

    /**
     * 提交退宿申请
     * @param v
     */
    public void submitTuisu(View v){
        if(sqly.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请填写申请理由");
            return;
        }
//        if(dfxx_pick.getText().toString().length()<=0){
//            showErrorMsg(mRootView,"请选择退宿原因");
//            return;
//        }
        if(shyj.getText().toString().length()<=0){
            showErrorMsg(mRootView,"请填写审核意见");
            return;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("bz",bz.getText().toString());//备注
        map.put("userIdString",student.getXsid());//学生id
        map.put("sqly",sqly.getText().toString());//申请理由
        map.put("shyj",shyj.getText().toString());//申请理由
        map.put("tsyy",tsyyid);//学生id

        ok_text.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tuisusq", map, new HttpListener<JSONObject>() {
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

    /**
     * 增加提报人
     * @param view
     */
    public void addWjxs(View view){
        Intent intent = new Intent(mContext, ZssglSelectPersonsActivity.class);
        intent.putExtra("lmlb","3");
        intent.putExtra("categoryTitle","退宿学生");
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    /**
     * 选取退宿原因
     * @param v
     */
    public void toPickKfyy(View v){
        if(tsyys.size()>0) {
            Intent intent = new Intent(mContext, WsjcTjWeekPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) tsyys);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.push_up_in, 0);
        }else{
            getTuisu();
            if(tsyys.size()>0) {
                Intent intent = new Intent(mContext, WsjcTjWeekPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) tsyys);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_up_in, 0);
            }else {
                showErrorMsg(mRootView, "未获取到退宿原因选项");
            }
        }
    }

    /**
     * 获取退宿原因
     */
    public void getTuisu(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tzyyList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                tsyys.addAll(dfxxes);
                            }else{

                            }
                        } else {
                        }
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    tsyyid = data.getStringExtra("kfsid");
                    String tsyy = data.getStringExtra("kfs");
                    dfxx_pick.setText(tsyy);
                }
                break;
            case 2:
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
        if(student != null && sqly.getText().toString().length()>0 && shyj.getText().toString().length()>0){
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

    @Override
    public void back() {
        new AlertDialog(mContext).show(null, "确定退出退宿申请？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ZssglAddTuiSuActivity.super.back();
            }
        });
    }

}
