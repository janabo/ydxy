package com.dk.mp.core.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.mp.core.R;
import com.dk.mp.core.adapter.MyAdapter;
import com.dk.mp.core.util.AdapterInterface;

import java.util.List;

/**
 * Created by dongqs on 2016/10/29.
 *
 */

public class MyListView extends SwipeRefreshLayout {

    private RecyclerView recyclerView;
    private Context context;
    private LinearLayout zwsj;
    private ImageView zwsj_icon;
    private TextView zwsj_text;
    private boolean showprograss = true;
    private MyAdapter adapter;
    private boolean isLoadingMore = false;
    private boolean isEnd = false;
    private AdapterInterface adapterInterface;
    private List list;

    public int pageNo = 1;
    public int totalPages = -1;
    private int pageSize = 20;
    public enum Error {
        NoNetwork,
        NoDatas,
        OnError
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.core_listview, this, true);
        recyclerView = (RecyclerView)view.findViewById(R.id.core_recyclerView);
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItemPosition = 0;
                if (layoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] last = null;
                    int[] first = null;
                    last = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    first = new int[last.length];
                    int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(last);
                    for (int i : lastVisibleItemPositions) {
                        lastVisibleItemPosition = i > lastVisibleItemPosition ? i : lastVisibleItemPosition;
                    }
                }
                if (adapter != null && (lastVisibleItemPosition == adapter.getItemCount() - 3) && !isLoadingMore && !isRefreshing() && !isEnd) {
                    setEnabled(false);
                    isLoadingMore = true;
                    loadingMore(true);
                    loadMore();
                }
            }
        });
        //recycleview刷新时clear后加载新的item会报错
        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRefreshing()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        setProgressBackgroundColorSchemeResource(android.R.color.white);
        setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_blue_light,android.R.color.holo_orange_light, android.R.color.holo_red_light);
        setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        zwsj = (LinearLayout) findViewById(R.id.zwsj);
        zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
        zwsj_text = (TextView) findViewById(R.id.zwsj_text);

        setOnRefreshListener(listener);
    }

    public void startRefresh(){
        if (!isRefreshing()){
            post(new Runnable() {
                @Override
                public void run() {
                    if (showprograss){
                        setRefreshing(true);
                    }
                }
            });
        }
    }

    public void error(Error error){
        switch (error){
            case NoNetwork :{
                zwsj_icon.setImageResource(R.mipmap.nonet);
                zwsj_text.setText(context.getString(R.string.net_no2));
                break;
            }
            case NoDatas :{
                zwsj_icon.setImageResource(R.mipmap.nodata_n);
                zwsj_text.setText(context.getString(R.string.nodata));
                break;
            }
            case OnError :{
                zwsj_icon.setImageResource(R.mipmap.errorserver);
                zwsj_text.setText(context.getString(R.string.data_fail));
                break;
            }
            default:{
                break;
            }
        }
        setDatasEnd(true);
        if (pageNo != 1){
            adapter.loadFaile();
        }
        stopRefresh(false);
    }

    public void flish(){
        adapter.notifyDataSetChanged();
        if (list.size() % pageSize != 0 || totalPages == pageNo){
            setDatasEnd(true);
        } else {
            setDatasEnd(false);
        }
        loadingMore(false);
        stopRefresh(true);
    }

    public void finish(int totalPages,int currentPage){
        adapter.notifyDataSetChanged();
        if (totalPages<=currentPage){
            setDatasEnd(true);
        } else {
            setDatasEnd(false);
        }
        loadingMore(false);
        stopRefresh(true);
    }


    public void stopRefresh(boolean success){
        showprograss = false;
        isLoadingMore = false;
        this.setRefreshing(false);
        setEnabled(true);
        if (zwsj != null && adapter != null && adapter.getItemCount() == 1 && !success){ // 显示图标+文字的错误提示（首次加载页面失败）
            zwsj.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            zwsj.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (!success) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, zwsj_text.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout){
        recyclerView.setLayoutManager(layout);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor){
        recyclerView.addItemDecoration(decor);
    }

    public void setDatasEnd(boolean end) {
        isEnd = end;
    }

    public void setAdapterInterface(List list , AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
        this.list = list;
        adapter = new MyAdapter(context, list, this.adapterInterface, new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.loadingMore(true);
                pageNo--;
                loadMore();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void loadingMore(boolean t){
        adapter.loadingMore(t);
    }

    private OnRefreshListener listener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageNo = 1;
            list.clear();
            adapterInterface.loadDatas();
        }
    };

    private void loadMore(){
        pageNo ++;
        adapterInterface.loadDatas();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public MyAdapter getAdapter() {
        return adapter;
    }

    public void addList(List mData){
        list.addAll(mData);
        adapter.notifyDataSetChanged();
    }

    public void clearList(){
        if(list!=null && list.size()>0){
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    public void reLoadDatas(){
        startRefresh();
        pageNo = 1;
        list.clear();
        adapterInterface.loadDatas();
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
