package com.dk.mp.txl.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.txl.R;
import com.dk.mp.txl.adapter.TxlSearchAdapter;
import com.dk.mp.txl.http.Manager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯录搜索
 * 作者：janabo on 2016/12/26 14:46
 */
public class SearchActivity extends MyActivity{
    private EditText mKeywords;//搜索关键字
    private TextView cancle;//
    private LinearLayout layout_search;
    private RecyclerView mRecycle;
    private List<Jbxx> mList = new ArrayList<>();
    private ErrorLayout mError;
    private TxlSearchAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.app_search;
    }

    @Override
    protected void initialize() {
        super.initialize();
       if(DeviceUtil.checkNet()){//判断是否有网络
           mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
       }else{
           mError.setErrorType(ErrorLayout.NETWORK_ERROR);
       }
    }


    /**
     * 初始化界面
     */
    protected void initView() {
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        mKeywords = (EditText) findViewById(R.id.search_Keywords);
        cancle = (TextView) findViewById(R.id.cancle_search);
        mRecycle = (RecyclerView) findViewById(R.id.person_recycle);
        mRecycle.setHasFixedSize ( true );
        mRecycle.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new TxlSearchAdapter( mContext, SearchActivity.this, mList);
        mRecycle.setAdapter ( mAdapter );
        mRecycle.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mKeywords.setHint("姓名、电话");
        mKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = mKeywords.getText().toString();
                    Logger.info(keywords);
                    hideSoftInput();
                    if (StringUtils.isNotEmpty(keywords)) {
                        if(DeviceUtil.checkNet()){//判断是否有网络
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            query();
                        }else{
                            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                        }
                    } else {
                        SnackBarUtil.showShort(layout_search,"请输入关键字");
                    }
                }
                return false;
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void query(){
        mError.setErrorType(ErrorLayout.LOADDATA);
        mList.clear();
        Map<String, Object> map = new HashMap<>();
        map.put("key", mKeywords.getText().toString());
        HttpUtil.getInstance().postJsonObjectRequest("http://wap.cztljx.org/apps/txl/query", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        mError.setErrorMessage(getReString(R.string.search_fail));
                    }else{
                        List<Jbxx> departments = Manager.getPeoples(result);
                        mList.addAll(departments);
                        mAdapter.notifyDataSetChanged();
                        if(departments.size()>0) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        }else{
                            mError.setErrorType(ErrorLayout.NODATA);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    mError.setErrorMessage(getReString(R.string.search_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorMessage(getReString(R.string.search_fail));
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
