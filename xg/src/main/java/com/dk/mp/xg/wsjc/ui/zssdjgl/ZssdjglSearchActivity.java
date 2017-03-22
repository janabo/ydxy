package com.dk.mp.xg.wsjc.ui.zssdjgl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：janabo on 2017/2/15 15:35
 */
public class ZssdjglSearchActivity extends MyActivity implements View.OnClickListener{
    private EditText mKeywords;//搜索关键字
    private TextView cancle;//
    private LinearLayout layout_search;
    private MyListView mRecycle;
    private List<Zssdjgl> mData = new ArrayList<>();
    private ErrorLayout mError;
    private String mType;

    @Override
    protected int getLayoutID() {
        return R.layout.wsjc_search;
    }

    @Override
    protected void initialize() {
        super.initialize();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.select_title));
        }
        findView();
        if(DeviceUtil.checkNet()){//判断是否有网络
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
        BroadcastUtil.registerReceiver(mContext, mRefreshBroadcastReceiver, "zssdjgl_search_refresh");
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("zssdjgl_search_refresh")) {
                mData.clear();
                mError.setErrorType(ErrorLayout.LOADDATA);
                mRecycle.setPageNo(1);
                getData();
            }
        }
    };

    /**
     * 初始化界面
     */
    private void findView(){
        mType = getIntent().getStringExtra("type");
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        mKeywords = (EditText) findViewById(R.id.search_Keywords);
        cancle = (TextView) findViewById(R.id.cancle_search);
        mRecycle = (MyListView) findViewById(R.id.person_recycle);
        mRecycle.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mRecycle.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecycle.setAdapterInterface(mData, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_wsjc_record_list_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                Zssdjgl zssdjgl = mData.get(position);
                ((MyView)holder).ssl_fjh.setText(zssdjgl.getName());
                ((MyView)holder).ssq.setText(zssdjgl.getRoom());
                ((MyView)holder).fs.setText(zssdjgl.getTime());
            }

            @Override
            public void loadDatas() {
                getData();
            }
        });

        mKeywords.setHint("房间号或姓名");
        mKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = mKeywords.getText().toString();
                    Logger.info(keywords);
                    hideSoftInput();
                    if (StringUtils.isNotEmpty(keywords)) {
                        mError.setErrorType(ErrorLayout.LOADDATA);
                        mRecycle.setPageNo(1);
                        getData();
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

    public void getData(){
        if(DeviceUtil.checkNet()) {
            mData.clear();
            mRecycle.clearList();
            query();
        }else{
            if(mRecycle.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(mRecycle,R.string.net_no2);
            }
        }
    }

    public void query(){
        Map<String, Object> map = new HashMap<>();
        map.put("key", mKeywords.getText().toString());
        map.put("pageNo",mRecycle.pageNo);
        map.put("role", "1");
        map.put("type",mType);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Zssdjgl>>(){}, "apps/zsdjgl/xsList", map, new HttpListener<PageMsg<Zssdjgl>>() {
            @Override
            public void onSuccess(PageMsg<Zssdjgl> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
                    mData.addAll(result.getList());
//                    mRecycle.addList(result.getList());
                    mRecycle.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(mRecycle.pageNo == 1) {
                        mError.setErrorType(ErrorLayout.SEARCHNODATA);
                    }else{
                        SnackBarUtil.showShort(mRecycle,R.string.searchnodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(mRecycle.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(mRecycle,R.string.data_fail);
                }
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

    @Override
    public void onClick(View view) {
        getData();
    }


    class MyView extends RecyclerView.ViewHolder{
        public TextView ssl_fjh;
        public TextView ssq;
        public TextView fs;

        public MyView(View itemView) {
            super(itemView);
            ssl_fjh = (TextView) itemView.findViewById(R.id.ssl_fjh);
            ssq = (TextView) itemView.findViewById(R.id.ssq);
            fs = (TextView) itemView.findViewById(R.id.fs);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Zssdjgl zssgl = mData.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, ZssdjglDetailActivity.class);
                    intent.putExtra("detailid",zssgl.getId());
                    intent.putExtra("type",mType);
                    startActivity(intent);
                }
            });
        }
    }
}
