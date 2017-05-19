package com.dk.mp.xg.wsjc.ui.zsskq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.ZsPersonInformationAdapter;
import com.dk.mp.xg.wsjc.entity.InformationQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cobb on 2017/5/8.
 */

public class ZsPersonInformationQueryActivity extends MyActivity{

    private Button back;
    private TextView title;

    private TextView name, grade;
    private ImageButton gradeclear, nameclear;

    private LinearLayout gradequery;

    private ErrorLayout mError;
    private RecyclerView mRecyclerView;
    private ZsPersonInformationAdapter mAdapter;
    List<InformationQuery> mData = new ArrayList<>();

    private String stuname = "";
    private String bjId = "";

    @Override
    protected int getLayoutID() {
        return R.layout.zs_person_information_query;
    }

    @Override
    protected void initView() {
        super.initView();

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        name = (TextView) findViewById(R.id.name);
        grade = (TextView) findViewById(R.id.grade);
        gradeclear = (ImageButton) findViewById(R.id.gradeclear);
        nameclear = (ImageButton) findViewById(R.id.nameclear);
        gradequery = (LinearLayout) findViewById(R.id.gradequery);

        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);

        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new ZsPersonInformationAdapter(mContext,mData,ZsPersonInformationQueryActivity.this);
        mRecyclerView.setAdapter ( mAdapter );
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 10, ContextCompat.getColor(mContext, R.color.colorPrimary61)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setTitle(getIntent().getStringExtra("title"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               back();
            }
        });

        gradequery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradequ();
            }
        });
}

    @Override
    protected void initialize() {
        super.initialize();

        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
                    if(inputMethodManager.isActive()){
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }

                    stuname = name.getText().toString().trim();

                    mError.setErrorType(ErrorLayout.LOADDATA);
                    getList();

                    return true;
                }
                return false;
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (name.getText().toString().trim().length()>0){
                    nameclear.setVisibility(View.VISIBLE);
                }else {
                    nameclear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nameclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                stuname = "";
                nameclear.setVisibility(View.GONE);

                mError.setErrorType(ErrorLayout.LOADDATA);
                getList();

            }
        });

        grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (grade.getText().length()>0){
                    gradeclear.setVisibility(View.VISIBLE);
                }else {
                    gradeclear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gradeclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade.setText("学生所在班级");
                gradeclear.setVisibility(View.GONE);
                bjId = "";

                mError.setErrorType(ErrorLayout.LOADDATA);
                getList();
            }
        });

    }

    public void getList(){
        if(DeviceUtil.checkNet()){
            mError.setErrorType(ErrorLayout.LOADDATA);
            getListT();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

    }

    public void getListT(){
        mData.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("xm",stuname);
        map.put("bjId",bjId);
        Log.e("参数查询",stuname + "和"+ bjId);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsscx/xscx", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<InformationQuery> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<InformationQuery>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<InformationQuery> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mData.addAll(dfxxes);
                                Log.e("查询成功了","查询成功了" + mData.size());
                                mAdapter.notifyDataSetChanged();
                                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            }else{
                                mError.setErrorType(ErrorLayout.SEARCHNODATA2);
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

    public void gradequ(){

        Intent intent = new Intent(ZsPersonInformationQueryActivity.this,ZsPersonGradeQueryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("gradelist", (Serializable) mData);
        if (name.getText().toString() != null){
            stuname = name.getText().toString();
        }
        bundle.putString("name",stuname);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1:
                    mData.clear();
                    String xs = data.getStringExtra("name");
                    String bj = data.getStringExtra("bjmc");

                    name.setText(xs);
                    grade.setText(bj);

                    stuname = xs;
                    bjId = data.getStringExtra("bjId");

                    getList();
                    break;
            }
        }
    }

}
