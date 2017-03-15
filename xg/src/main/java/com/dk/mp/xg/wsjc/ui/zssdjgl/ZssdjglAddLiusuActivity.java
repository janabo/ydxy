package com.dk.mp.xg.wsjc.ui.zssdjgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.ZssdjglPersonsAdapter;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzWjrqPickActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新增留宿学生
 * 作者：janabo on 2017/2/8 09:51
 */
public class ZssdjglAddLiusuActivity extends MyActivity implements ZssdjglPersonsAdapter.OnItemClickListener{
    private TextView jsrq_pick,ksrq_pick,jsrq_title,ksrq_title,bt_txt;//结束日期，开始日期
    private ScrollView mRootView;
    private LinearLayout ok;
    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;
    private TextView ok_text;//退宿原因
    private String type="1";//1:周末留宿学生登记，2：日常请假登记
    private List<Zssdjgl> persons = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ZssdjglPersonsAdapter zAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssdjgl_add_liusu;
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView = (RecyclerView) findViewById(R.id.person_recycle);
        jsrq_pick = (TextView) findViewById(R.id.jsrq_pick);
        ksrq_pick = (TextView) findViewById(R.id.ksrq_pick);
        ksrq_title = (TextView) findViewById(R.id.ksrq_title);
        jsrq_title = (TextView) findViewById(R.id.jrrq_title);
        bt_txt = (TextView)findViewById(R.id.bt_txt);
        mRootView = (ScrollView) findViewById(R.id.mRootView);
        ok = (LinearLayout) findViewById(R.id.ok);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
        ok_text = (TextView) findViewById(R.id.ok_text);
        jsrq_pick.addTextChangedListener(mTextWatcher);
        ksrq_pick.addTextChangedListener(mTextWatcher);
        type = getIntent().getStringExtra("type");
        if("1".equals(type)) {
            setTitle("留宿登记");
            ksrq_title.setText("留宿开始时间");
            jsrq_title.setText("留宿结束时间");
            bt_txt.setText("住宿人员");
        }else{
            setTitle("请假登记");
            ksrq_title.setText("请假开始时间");
            jsrq_title.setText("请假结束时间");
            bt_txt.setText("请假人员");
        }
        persons.add(new Zssdjgl("addperson",""));

        zAdapter = new ZssdjglPersonsAdapter(persons,mContext,ZssdjglAddLiusuActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(zAdapter);
        ok.setEnabled(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
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
        if(jsrq_pick.getText().toString().length()>0 && ksrq_pick.getText().toString().length()>0 && persons.size()>1){
            ok.setBackground(getResources().getDrawable(R.drawable.ripple_bg));
            ok.setEnabled(true);
        }else{
            ok.setBackgroundColor(getResources().getColor(R.color.rcap_gray));
            ok.setEnabled(false);
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

    /**
     * 停宿开始日期
     * @param v
     */
    public void toPickKsrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    /**
     * 停宿结束日期
     * @param v
     */
    public void toPickJsrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
                    persons.clear();
                    persons.addAll((List<Zssdjgl>)data.getSerializableExtra("persons"));
                    persons.add(new Zssdjgl("addperson",""));
                    zAdapter.notifyDataSetChanged();
                    dealOkButton();
                }
                break;
        }
    }

    @Override
    public void back() {
        new AlertDialog(mContext).show(null, "1".equals(type)?"确定退出留宿登记？":"确定退出请假登记？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ZssdjglAddLiusuActivity.super.back();
            }
        });
    }

    /**
     * 提交
     * @param v
     */
    public void submitLiusu(View v){
        if(!TimeUtils.comparedDate(ksrq_pick.getText().toString(),jsrq_pick.getText().toString())){
            showErrorMsg("1".equals(type)?"留宿开始时间不能大于留宿结束时间":"请假开始时间不能大于请假结束时间");
            return ;
        }
        String users = "";
        for(Zssdjgl p : persons){
            if("addperson".equals(p.getId())) {
                continue;
            }
                users += p.getId() + ",";
        }
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",ksrq_pick.getText().toString());
        map.put("endTime",jsrq_pick.getText().toString());
        map.put("users",users);
        map.put("type",type);

        ok_text.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsdjgl/add", map, new HttpListener<JSONObject>() {
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
                                BroadcastUtil.sendBroadcast(mContext, "zssdjgl_refresh");
                                onBackPressed();
                            }
                        },1500);
                    }else{
                        showErrorMsg(jd.getMsg());
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

    @Override
    public void onItemClick(View view, int position) {
        if("addperson".equals(persons.get(position).getId())){
            Intent intent = new Intent(mContext, SelectPersonsActivity.class);
            intent.putExtra("type",type);
            intent.putExtra("persons",(Serializable)persons);
            startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }else{
            persons.remove(position);
            zAdapter.notifyDataSetChanged();
            dealOkButton();
        }
    }
}
