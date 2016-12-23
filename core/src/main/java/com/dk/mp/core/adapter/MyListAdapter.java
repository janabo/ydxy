package com.dk.mp.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dk.mp.core.R;

import java.util.List;

/**
 * 作者：janabo on 2016/12/19 13:56
 */
public class MyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List mData;
    private ProgressBar progressBar;
    private Button reload;
    private AdapterInterface adapterInterface;

    /**
     * 构造方法.
     */
    public MyListAdapter(Context context, List mData,AdapterInterface adapterInterface) {
        this.context = context;
        this.mData = mData;
        this.adapterInterface = adapterInterface;
        mLayoutInflater = LayoutInflater.from ( context );
    }

    /**
     * 获取当前滑动到的view 类型
     * @param position 当前滑动位置
     * @return 内容或者footView
     */
    @Override
    public int getItemViewType ( int position ) {
        if ( position + 1 == getItemCount ( ) ) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( viewType == TYPE_ITEM ) {
            return adapterInterface.setItemView(parent, viewType);
        } else {
            //最后放置一个加载更多的 footView
            View view = mLayoutInflater.inflate(R.layout.core_listview_footview, parent, false);// 设置要转化的layout文件
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            reload = (Button) view.findViewById(R.id.reload);
            return new FootView(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == mData.size()) {

        } else {
            adapterInterface.setItemValue(holder, position);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        //多一个放加载更多
        return mData == null ? 1 : mData.size ( ) + 1;
    }

    private class FootView extends RecyclerView.ViewHolder{
        public FootView(View itemView) {
            super(itemView);
        }
    }

    /**
     * 加载更多
     * @param t
     */
    public void loadingMore(boolean t){
        progressBar.setVisibility(t?View.VISIBLE:View.GONE);
        reload.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    public void loadFailed(){
        progressBar.setVisibility(View.GONE);
        reload.setVisibility(View.VISIBLE);
    }
}

