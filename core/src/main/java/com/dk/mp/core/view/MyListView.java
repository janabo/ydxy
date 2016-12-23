package com.dk.mp.core.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.dk.mp.core.R;
import com.dk.mp.core.adapter.AdapterInterface;
import com.dk.mp.core.adapter.MyListAdapter;

import java.util.List;

/**
 *
 */
public class MyListView extends SwipeRefreshLayout{
    private RecyclerView recyclerView;
    private Context context;
    private MyListAdapter adapter;
    private List list;
    public int pageNo = 1;
    private AdapterInterface adapterInterface;
    private boolean showprograss = true;
    private int pageSize = 20;//每页多少条

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public MyListView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.core_listview, this, true);
        recyclerView = (RecyclerView)findViewById(R.id.core_listview);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ( (LinearLayoutManager) recyclerView.getLayoutManager ( ) ).findLastVisibleItemPosition ( );
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ( newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount ( ) ) {
                    onLoadData();
                }
            }
        });

        setProgressBackgroundColorSchemeResource(android.R.color.white);
        setColorSchemeResources ( R.color.colorPrimary, R.color.colorPrimaryDark );
        setOnRefreshListener(listener);
    }

    /**
     * 重写加载方法
     */
    public void onLoadData(){
        pageNo++;
        adapterInterface.loadDatas();
    }


    /**
     * 刷新
     */
    private OnRefreshListener listener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageNo = 1;
            list.clear();
            adapterInterface.loadDatas();
        }
    };

    public void setLayoutManager(RecyclerView.LayoutManager layout){
        recyclerView.setLayoutManager(layout);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor){
        recyclerView.addItemDecoration(decor);
    }

    public void setAdapterInterface(List list,AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
        this.list = list;
        adapter = new MyListAdapter(context,list,this.adapterInterface);
        recyclerView.setAdapter(adapter);
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

    public void stopRefresh(){
        this.setRefreshing(false);
    }

    public void addList(List mData){
        stopRefresh();
         list.addAll(mData);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}
