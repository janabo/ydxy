package com.dk.mp.core.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dk.mp.core.R;
import com.dk.mp.core.util.AdapterInterface;

import java.util.List;

/**
 * 作者：janabo on 2017/1/22 14:05
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater lif;
    private ProgressBar progressBar;
    private Button reload;
    private List list;
    private AdapterInterface adapterInterface;
    private View.OnClickListener onClickListener;

    /**
     * 构造方法.
     */
    public MyAdapter(Context context, List list, AdapterInterface adapterInterface, View.OnClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.adapterInterface = adapterInterface;
        this.onClickListener = onClickListener;
    }

    private class FootView extends RecyclerView.ViewHolder{
        public FootView(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        lif = LayoutInflater.from(context);// 转化到context这个容器
        if (viewType == 0){
            return adapterInterface.setItemView(parent, viewType);
        } else if (viewType == 1) {
            View view = lif.inflate(R.layout.core_listview_footview, parent, false);// 设置要转化的layout文件
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            reload = (Button) view.findViewById(R.id.reload);
            reload.setOnClickListener(onClickListener);
            return new FootView(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == list.size()) {
        } else {
            adapterInterface.setItemValue(holder, position);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == list.size()?1:0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == gridManager.getItemCount() - 1 ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public void loadingMore(boolean t){
        if (progressBar != null) {
            progressBar.setVisibility(t?View.VISIBLE:View.GONE);
            reload.setVisibility(View.GONE);
        }
    }

    public void loadFaile(){
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            reload.setVisibility(View.VISIBLE);
        }
    }
}
