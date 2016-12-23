package com.dk.mp.txl.ui;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.txl.R;
import com.dk.mp.txl.adapter.TxlAdapter;
import com.dk.mp.txl.db.RealmHelper;
import com.dk.mp.txl.entity.Jbxx;

import java.util.ArrayList;
import java.util.List;

/**
 * 部門人員
 * 作者：janabo on 2016/12/23 14:48
 */
public class TxlPersonsActivity extends MyActivity{
    RelativeLayout mdialog;
    ErrorLayout mErrorLayout;
    RecyclerView mRecyclerView;
    List<Jbxx> mList = new ArrayList<>();
    TxlAdapter mAdapter;
    private RealmHelper mRealmHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.app_persons;
    }

    @Override
    protected void initialize() {
        super.initialize();
        initView();
        setTitle(getIntent().getStringExtra("title"));
        mRealmHelper = new RealmHelper(this);
        if(DeviceUtil.checkNet()) {
            getData();
        }else{
            SnackBarUtil.showShort(mdialog,getReString(R.string.net_no2));
        }
    }

    /**
     * 初始化樣式
     */
    private void initView(){
        mErrorLayout = (ErrorLayout) findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.bm_recycle);
        mdialog = (RelativeLayout) findViewById(R.id.mdialog);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new TxlAdapter( mContext, mList,1);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
    }

    public void getData(){
        mErrorLayout.setErrorType(ErrorLayout.LOADDATA);

        for(int i=0;i<8;i++) {
            Jbxx jbxx = new Jbxx();
            jbxx.setId(i+1+"");
            jbxx.setName("王晓敏"+i);
            mRealmHelper.addXb(jbxx);
        }
        mList.addAll(mRealmHelper.queryALlXb());
        mAdapter.notifyDataSetChanged();

        mErrorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
    }

    /**
     * 搜索
     * @param v
     */
    public void toSearch(View v){

    }
}
