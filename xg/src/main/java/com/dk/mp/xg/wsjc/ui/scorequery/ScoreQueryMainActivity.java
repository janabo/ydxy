package com.dk.mp.xg.wsjc.ui.scorequery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.ScoreQueryMainAdapter;
import com.dk.mp.xg.wsjc.entity.SchoolYearOrTeram;
import com.dk.mp.xg.wsjc.entity.Score;
import com.dk.mp.xg.wsjc.entity.ScoreComment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：janabo on 2017/5/8 10:39
 */
public class ScoreQueryMainActivity extends MyActivity implements View.OnTouchListener{
    private RelativeLayout mTop,mRootView;
    private RecyclerView recyclerView;
    private ScoreComment sc;
    private LinearLayout schoolterm_lin,schoolyear_lin;//学期，学年
    private TextView schoolterm,schoolyear,py;
    List<Score> mData = new ArrayList<>();
    ScoreQueryMainAdapter mAdapter;
    private String xqid="", xnid="",xq="",xn="";
    private List<SchoolYearOrTeram> xns = new ArrayList<>();
    private List<SchoolYearOrTeram> xqs = new ArrayList<>();
    ErrorLayout error_layout;
    private EditText comment;
    private LinearLayout submit;
    private TextView submit_text;
    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;
    private LinearLayout py_lin,comment_lay;

    @Override
    protected int getLayoutID() {
        return R.layout.app_scorequery_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mTop = (RelativeLayout) findViewById(R.id.mtop);
        mTop.setBackgroundColor(Color.rgb(19,181,177));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(19,181,177));
            mTop.setElevation(0);
        }
        submit = (LinearLayout) findViewById(R.id.submit);
        submit_text = (TextView) findViewById(R.id.submit_text);
        comment = (EditText) findViewById(R.id.comment);
        comment.addTextChangedListener(mTextWatcher);
        comment.setOnTouchListener(this);
        submit.setEnabled(false);
        py_lin = (LinearLayout) findViewById(R.id.py_lin);
        py = (TextView) findViewById(R.id.py);
        comment_lay = (LinearLayout) findViewById(R.id.comment_lay);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
        error_layout = (ErrorLayout) findViewById(R.id.error_layout);
        error_layout.setTextColor(getResources().getColor(com.dk.mp.core.R.color.white));
        mRootView = (RelativeLayout) findViewById(R.id.mRootView);
        schoolterm_lin = (LinearLayout) findViewById(R.id.schoolterm_lin);
        schoolyear_lin = (LinearLayout) findViewById(R.id.schoolyear_lin);
        schoolterm = (TextView) findViewById(R.id.schoolterm);
        schoolyear = (TextView) findViewById(R.id.schoolyear);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setTitle(getIntent().getStringExtra("title"));
        recyclerView.setHasFixedSize ( false );
        recyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));
        mAdapter = new ScoreQueryMainAdapter(mData,mContext);
        recyclerView.setAdapter(mAdapter);
        getData();
        schoolyear_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xns.size()>0) {
                    Intent intent = new Intent(mContext, SchoolYearPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("xns", (Serializable) xns);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.push_up_in, 0);
                }else{
                    getData();
                }
            }
        });

        schoolterm_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xns.size()>0) {
                    Intent intent = new Intent(mContext, SchoolYearPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("xns", (Serializable) xqs);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                    overridePendingTransition(R.anim.push_up_in, 0);
                }else{
                    if(DeviceUtil.checkNet()) {
                        error_layout.setErrorType(ErrorLayout.LOADDATA);
                        getXq(xnid);
                    }else{
                        error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    String _xn = data.getStringExtra("kfs");
                    String _xnid = data.getStringExtra("kfsid");
                    if(!xnid.equals(_xnid)) {
                        xnid = _xnid;
                        xn = _xn;
                        schoolyear.setText(xn);
                        schoolterm.setText("");
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    String _xq = data.getStringExtra("kfs");
                    String _xqid = data.getStringExtra("kfsid");
                    if (!xnid.equals(_xqid)) {
                        xqid = _xqid;
                        xq = _xq;
                        schoolterm.setText(xq);
                        if(DeviceUtil.checkNet()) {
                            error_layout.setErrorType(ErrorLayout.LOADDATA);
                            getScore(xnid, xqid);
                        }else{
                            error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
                        }
                    }
                }
                break;
        }
    }

    public void getXn(){
        Map<String, Object> map = new HashMap<>();
        HttpUtil.getInstance().postJsonObjectRequest("apps/cjpy/xnlb", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    GsonData<SchoolYearOrTeram> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<SchoolYearOrTeram>>() {
                    }.getType());
                    if (gsonData.getCode() == 200) {
                        xns.addAll(gsonData.getData());
                        schoolyear.setText(xns.get(0).getMc());
                        xnid = xns.get(0).getId();
                        getXq(xnid);
                    }else{
                            error_layout.setErrorType(ErrorLayout.DATAFAIL);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                        error_layout.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                    error_layout.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    /**
     * 获取学期
     * @param mXnid 学年id
     */
    public void getXq(final String mXnid){
        Map<String, Object> map = new HashMap<>();
        map.put("xnId",mXnid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/cjpy/xqlb", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    GsonData<SchoolYearOrTeram> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<SchoolYearOrTeram>>() {
                    }.getType());
                    if (gsonData.getCode() == 200) {
                        xqs.addAll(gsonData.getData());
                        schoolterm.setText(xqs.get(0).getMc());
                        xqid = xqs.get(0).getId();
                        getScore(mXnid,xqid);
                    }else{
                            error_layout.setErrorType(ErrorLayout.DATAFAIL);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                        error_layout.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                    error_layout.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    public void getScore(String mXnid,String mXqid){
        Map<String, Object> map = new HashMap<>();
        map.put("xqId",mXqid);
        map.put("xnId",mXnid);
        HttpUtil.getInstance().gsonRequest(new TypeToken<ScoreComment>(){}, "apps/cjpy/cjlb", map, new HttpListener<ScoreComment>() {
            @Override
            public void onSuccess(ScoreComment result) {
                sc = result;
                mData.clear();
                if(sc != null && sc.getList() != null && sc.getList().size()>0) {
                    Score s = new Score("课程名称","成绩");
                    mData.add(s);
                    mData.addAll(sc.getList());
                    error_layout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                }else{
                    error_layout.setErrorType(ErrorLayout.NODATA);
                }
                mAdapter.notifyDataSetChanged();

                if(sc != null && StringUtils.isNotEmpty(sc.getPy())){
                    comment_lay.setVisibility(View.GONE);
                    py_lin.setVisibility(View.VISIBLE);
                    py.setText(sc.getPy());
                }else{
                    py_lin.setVisibility(View.GONE);
                    comment_lay.setVisibility(View.VISIBLE);
                    comment.setText("");
                    progress_cross.setVisibility(View.GONE);
                    progress_check.setVisibility(View.GONE);
                    submit_text.setVisibility(View.VISIBLE);
                    submit_text.setTextColor(Color.rgb(119,167,164));
                    submit.setEnabled(false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                error_layout.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }


    public void getData(){
        if(DeviceUtil.checkNet()) {
            error_layout.setErrorType(ErrorLayout.LOADDATA);
            getXn();
        }else{
            error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
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
            if(comment.getText().length()>0){
                submit_text.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                submit.setEnabled(true);
                if(comment.getText().length()>200){
                    showErrorMsg("学生成绩评语不能大于200字");
                    comment.setText(comment.getText().toString().substring(0,200));
                }

            }else{
                submit_text.setTextColor(Color.rgb(119,167,164));
                submit.setEnabled(false);
            }
        }
    };

    /**
     * 提交
     * @param v
     */
    public void submit(View v){
        if(comment.getText().length()>200){
            showErrorMsg("学生成绩评语不能大于200字");
            return;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("xnId",xnid);
        map.put("xqId",xqid);
        map.put("py",comment.getText().toString());

        submit_text.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        HttpUtil.getInstance().postJsonObjectRequest("apps/cjpy/tjpy", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    final JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                    if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                        progress.setVisibility(View.GONE);
                        progress_check.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {//等待成功动画结束
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                submit.setEnabled(true);
                                showErrorMsg(jd.getMsg());
                                error_layout.setErrorType(ErrorLayout.LOADDATA);
                                getScore(xnid, xqid);
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


    private void errorInfo(){
        progress.setVisibility(View.GONE);
        progress_cross.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {//等待成功动画结束
            @Override
            public void run() {
                progress_cross.setVisibility(View.GONE);
                submit_text.setVisibility(View.VISIBLE);
                submit.setEnabled(true);
            }
        },1000);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.comment && canVerticalScroll(comment))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}