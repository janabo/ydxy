package com.dk.mp.main.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.main.R;

import java.util.List;

/**
 * Created by dongqs on 2016/10/28.
 */

public class AddAppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<App> list;

    public AddAppAdapter(){
        //获取数据
        CoreSharedPreferencesHelper preference = new CoreSharedPreferencesHelper(MyApplication.getContext());
        list = preference.getNotinList();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView icon;
        public ImageView add;

        public MyViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            add = (ImageView) view.findViewById(R.id.gragItemViewAdd);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_grid_item, null);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).name.setText(list.get(position).getName());
        ((MyViewHolder)holder).icon.setImageResource(ImageUtil.getResource(list.get(position).getIcon()));
        //把数据保存在tag中
        ((MyViewHolder)holder).name.setTag(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //onitemclicklistener事件
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, App data);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(App)(v.findViewById(R.id.name).getTag()));
        }
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public List<App> getList() {
        return list;
    }
}
