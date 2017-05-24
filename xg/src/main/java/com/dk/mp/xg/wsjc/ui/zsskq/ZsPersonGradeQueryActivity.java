package com.dk.mp.xg.wsjc.ui.zsskq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.GradeQu;
import com.dk.mp.xg.wsjc.entity.InformationQuery;
import com.dk.mp.xg.wsjc.util.DVHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by cobb on 2017/5/9.
 */

public class ZsPersonGradeQueryActivity extends MyActivity implements View.OnClickListener {

    private LinearLayout colse;

    private ListView mRecycle;
    ErrorLayout mError;
    List<GradeQu> mData = new ArrayList<>();
    List<GradeQu> mData2 = new ArrayList<>();

    private String sname;

    private EditText name;
    private ImageButton nameclear;
    private String bjmc = "";

    private boolean isClick = true;
    private boolean scrollFlag = false;// 标记是否滑动

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
        name.setEnabled(true);
        nameclear = (ImageButton) findViewById(R.id.nameclear);

        mRecycle = (ListView) findViewById(R.id.listview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        MyView myView = new MyView(this,mData);
        mRecycle.setAdapter(myView);

        mRecycle.setOnScrollListener(new AbsListView.OnScrollListener() {
                                         @Override
                                         public void onScrollStateChanged(AbsListView view, int scrollState) {
                                             switch (scrollState) {
                                                 case SCROLL_STATE_TOUCH_SCROLL:
                                                     scrollFlag = true;
                                                     break;
                                                 case SCROLL_STATE_FLING:
                                                     scrollFlag = true;
                                                     break;
                                                 case SCROLL_STATE_IDLE:
                                                     scrollFlag = false;
                                                     break;
                                                 default:
                                                     break;
                                             }
                                         }

                                         @Override
                                         public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                         }
                                     });


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
               backback();

            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backback(){
        Bundle bundle = getIntent().getExtras();
        Intent intent = new Intent(ZsPersonGradeQueryActivity.this,ZsPersonInformationQueryActivity.class);
        intent.putExtra("name",bundle.getString("name"));
        intent.putExtra("grade",bundle.getString("grade"));
        intent.putExtra("bjId", bundle.getString("bjId"));
        intent.putExtra("t","0");
        startActivity(intent);
        ZsPersonInformationQueryActivity.activity.finish();

        back();
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
                if (isClick == true && scrollFlag == false){
                    name.setText("");
                    bjmc = "";
                    nameclear.setVisibility(View.GONE);

                    mError.setErrorType(ErrorLayout.LOADDATA);
                    getList();

                }

            }
        });

    }

    public void getData(){
        Bundle bundle = getIntent().getExtras();
        sname = bundle.getString("name");
        mData2 = (List<GradeQu>) bundle.getSerializable("gradelist");
        if (mData2 !=null && mData2.size()>0){
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            mData.addAll(mData2);
        }else {
            if(DeviceUtil.checkNet()){
                getList();
            }else{
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        }

    }

    public void getList(){
        isClick = false;
        name.setEnabled(false);
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
//                                HashSet<GradeQu> hs = new HashSet<GradeQu>(dfxxes);
//                                mData.addAll(hs);
                                removeDuplicate(dfxxes);
                                mData.addAll(dfxxes);
                                Log.e("查询成功了","查询成功了" + mData.size());
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
                isClick = true;
                name.setEnabled(true);
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
                isClick = true;
                name.setEnabled(true);
            }
        });
    }

    private List<GradeQu> removeDuplicate(List<GradeQu> list) {
                Set<GradeQu> set = new HashSet<GradeQu>();
                List<GradeQu> newList = new ArrayList<GradeQu>();
                 for (Iterator<GradeQu> iter = list.iterator(); iter.hasNext();) {
                     GradeQu element = (GradeQu) iter.next();
                         if (set.add(element))
                                 newList.add(element);
                     }
                Log.e("sjadishudhcuicdssa",newList.size() + "a");
                 return newList;
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
