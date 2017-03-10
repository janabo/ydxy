package com.dk.mp.newoa.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.anni.TransitionHelper;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.NewDoc;
import com.dk.mp.newoa.entity.OATab;
import com.dk.mp.newoa.entity.SeriMap;
import com.dk.mp.newoa.ui.GzzbTabActivity;
import com.dk.mp.newoa.ui.OADetailActivity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知公告
 * 作者：janabo on 2017/1/4 14:30
 */
public class GzzbListFragment extends BaseFragment implements View.OnClickListener{
    public static final String ARGS_TABS = "args_tabs";
    private OATab mOaTabs;
    private ErrorLayout mError;
    private MyListView myListView;
    private List<NewDoc> mList = new ArrayList<>();

    public static GzzbListFragment newInstance(OATab oaTabs) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_TABS,oaTabs);
        GzzbListFragment fragment = new GzzbListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOaTabs = getArguments().getParcelable(ARGS_TABS);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.newoa_list;
    }

    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        myListView = (MyListView)view.findViewById(R.id.oa_list);
        mError.setOnLayoutClickListener(this);
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        initViews();
    }

    public void initViews(){

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(mContext, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

        myListView.setAdapterInterface(mList, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.new_oa_list_item, parent, false);// 设置要转化的layout文件
                return new GzzbListFragment.MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                ((MyView)holder).title.setText(mList.get(position).getTitle());
//                ((MyView)holder).db_lx.setText(mList.get(position).getType());
                ((MyView)holder).shijian.setText(mList.get(position).getTime());
                ((MyView)holder).bumen.setText(mList.get(position).getSubTitle());
            }

            @Override
            public void loadDatas() {
                getData();
            }
        });
        getData();
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            getList();
        }else{
            if(myListView.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }
        }
    }

    public void getList(){
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        map.put("process",mOaTabs.getProcess());
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<NewDoc>>(){}, "apps/gzzb/list", map, new HttpListener<PageMsg<NewDoc>>() {
            @Override
            public void onSuccess(PageMsg<NewDoc> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {//是否获取到数据
                    mList.addAll(result.getList());
                    myListView.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(myListView.pageNo == 1) {//是否是第一页
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else{
                        SnackBarUtil.showShort(myListView,R.string.nodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(myListView.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(myListView,R.string.data_fail);
                }
            }
        });
    }

    private class MyView extends RecyclerView.ViewHolder{
        private TextView title,bumen,shijian,db_lx;
        public MyView(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);// 取得实例
            bumen = (TextView) itemView.findViewById(R.id.bumen);// 取得实例
            shijian = (TextView) itemView.findViewById(R.id.shijian);// 取得实例
            db_lx = (TextView) itemView.findViewById(R.id.db_lx);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.setTransitionName(view, "detail_element");
                    NewDoc doc = mList.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, OADetailActivity.class);
                    intent.putExtra("isgzzb", "true");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("doc", doc);
                    SeriMap map = new SeriMap();
                    map.setMap(doc.getParam());
                    bundle.putSerializable("map", map);
                    intent.putExtras(bundle);
                    intent.putExtra("typename", mOaTabs.getTypename());

                    ActivityOptionsCompat options = TransitionHelper.makeOptionsCompat((GzzbTabActivity) mContext,
                            Pair.create(view,"detail_element"));
                    ActivityCompat.startActivity((GzzbTabActivity) mContext,intent,options.toBundle());
                    ((GzzbTabActivity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        getData();
    }
}
