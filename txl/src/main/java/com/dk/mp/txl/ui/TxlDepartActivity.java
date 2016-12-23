package com.dk.mp.txl.ui;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.txl.R;
import com.dk.mp.txl.adapter.TxlAdapter;
import com.dk.mp.txl.db.RealmHelper;
import com.dk.mp.txl.entity.Department;
import com.dk.mp.txl.entity.Jbxx;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2016/12/22 15:57
 */
public class TxlDepartActivity extends MyActivity{
    RelativeLayout mdialog;//提示框
    ErrorLayout error_layout;//加载
    RecyclerView mRecyclerView;//星标同事
    RecyclerView mBmRecyclerView;//所有部门
    LinearLayout xbts_lin;//
    TxlAdapter mAdapter;
    TxlAdapter bAdapter;
    List<Jbxx> mList = new ArrayList<>();//星标同事
    List<Department> mData = new ArrayList<>();//所有部门
    private RealmHelper mRealmHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.app_yellowpage;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        mRealmHelper = new RealmHelper(this);
        initView();
        if(DeviceUtil.checkNet()) {
            getData();
        }else{
            SnackBarUtil.showShort(mdialog,getReString(R.string.net_no2));
        }
    }

    public void initView(){
        mdialog = (RelativeLayout) findViewById(R.id.mdialog);
        error_layout = (ErrorLayout) findViewById(R.id.error_layout);
        xbts_lin = (LinearLayout) findViewById(R.id.xbts_lin);
        mRecyclerView = (RecyclerView) findViewById(R.id.xbts_recycle);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new TxlAdapter ( mContext, mList,1);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f),Color.rgb(229, 229, 229)));
        mRecyclerView.setNestedScrollingEnabled(false);

        mBmRecyclerView = (RecyclerView) findViewById(R.id.bm_recycle);
        mBmRecyclerView.setHasFixedSize ( true );
        mBmRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        bAdapter = new TxlAdapter ( mContext, mData,2);
        mBmRecyclerView.setAdapter ( bAdapter );
        mBmRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f),Color.rgb(229, 229, 229)));
        mBmRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 搜索
     * @param v
     */
    public void toSearch(View v){

    }

    public void getData(){
        error_layout.setErrorType(ErrorLayout.LOADDATA);
        for(int i=0;i<8;i++) {
            Jbxx jbxx = new Jbxx();
            jbxx.setId(i+1+"");
            jbxx.setName("王晓敏"+i);
            mRealmHelper.addXb(jbxx);
        }
        mList.addAll(mRealmHelper.queryALlXb());
        mAdapter.notifyDataSetChanged();

        for(int i=0;i<14;i++) {
            Department jbxx = new Department();
            jbxx.setId(i+1+"");
            jbxx.setName("计算机科学与技术哈哈哈哈哈"+i);
            mData.add(jbxx);
        }
        mRealmHelper.deleteDepartment();
        mRealmHelper.addDepartment(mData);
        mData.clear();
        mData.addAll(mRealmHelper.queryAllDepartment());
        bAdapter.notifyDataSetChanged();
        error_layout.setErrorType(ErrorLayout.HIDE_LAYOUT);
    }
}
