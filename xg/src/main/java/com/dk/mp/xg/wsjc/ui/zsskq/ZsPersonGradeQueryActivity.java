package com.dk.mp.xg.wsjc.ui.zsskq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.GradeQu;
import com.dk.mp.xg.wsjc.entity.InformationQuery;
import com.dk.mp.xg.wsjc.util.DVHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cobb on 2017/5/9.
 */

public class ZsPersonGradeQueryActivity extends MyActivity implements View.OnClickListener {

    private LinearLayout colse;

    private ListView mRecycle;
    ErrorLayout mError;
    List<GradeQu> mData = new ArrayList<>();
    List<InformationQuery> mData2 = new ArrayList<>();

    private String sname;

    private EditText name;
    private String bjmc = "";

    @Override
    protected int getLayoutID() {
        return R.layout.app_zs_person_grade_query;
    }

    @Override
    protected void initView() {
        super.initView();

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        colse = (LinearLayout) findViewById(R.id.colse);
        name = (EditText) findViewById(R.id.name);

        mRecycle = (ListView) findViewById(R.id.listview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        MyView myView = new MyView(this,mData);
        mRecycle.setAdapter(myView);

        mRecycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                    intent.putExtra("bjId",mData.get(position).getBjid());
                    intent.putExtra("bjmc",mData.get(position).getBjmc());
                    intent.putExtra("name",sname);
                    setResult(RESULT_OK,intent);

                    back();
            }
        });

        colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();

            }
        });

    }

    @Override
    protected void initialize() {
        super.initialize();

        getData();

        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
                    if(inputMethodManager.isActive()){
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }

                    bjmc = name.getText().toString().trim();
                    mError.setErrorType(ErrorLayout.LOADDATA);
                    getList();

                    return true;
                }
                return false;
            }
        });
    }

    public void getData(){
        Bundle bundle = getIntent().getExtras();
        sname = bundle.getString("name");
        mData2 = (List<InformationQuery>) bundle.getSerializable("gradelist");
        if (mData2 !=null && mData2.size()>0){
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            for(int i=0; i<mData2.size(); i++){
                mData.add(new GradeQu(mData2.get(i).getBjid(),mData2.get(i).getBjmc()));
            }
        }else {
            if(DeviceUtil.checkNet()){
                getList();
            }else{
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        }
    }

    public void getList(){
        mData.clear();
        mData2.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("bjmc",bjmc);

        Log.e("模糊查询",bjmc);

        HttpUtil.getInstance().postJsonObjectRequest("apps/zsscx/bjlb", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<GradeQu> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<GradeQu>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<GradeQu> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mData.addAll(dfxxes);
                                Log.e("查询成功了","查询成功了" + mData.size());
                                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            }else{
                                mError.setErrorType(ErrorLayout.NODATA);
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

    @Override
    public void onClick(View v) {
        getData();
    }

    public class MyView extends BaseAdapter{

        private Context context;
        private List<GradeQu> list;

        public MyView(Context context, List<GradeQu> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (null == list){
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.ad_grade_query,null);
            }

            TextView gradelist = DVHolder.get(convertView, R.id.gradelist);

            GradeQu gradeQu = list.get(position);
            gradelist.setText(gradeQu.getBjmc());

            return convertView;
        }
    }

}