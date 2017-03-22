package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Class;

import java.util.List;

/**
 * 作者：janabo on 2017/2/8 15:22
 */
public class ZssglSelectClassAdapter extends RecyclerView.Adapter<ZssglSelectClassAdapter.MyViewHolder>{
    private Context mContext;
    List<Class> mData;
    LayoutInflater inflater;
    int selected;//选择第几个

    public ZssglSelectClassAdapter(Context mContext,List<Class> mData,int selected) {
        this.mContext = mContext;
        this.mData = mData;
        this.selected = selected;
        inflater = LayoutInflater.from(mContext);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public ZssglSelectClassAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_wsjc_record_item,parent,false);
        return new ZssglSelectClassAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZssglSelectClassAdapter.MyViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getClassName());
        if(selected == position){
            holder.isselect.setVisibility(View.VISIBLE);
            holder.lin_lc.setBackgroundColor(mContext.getResources().getColor(R.color.page_bg));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            holder.isselect.setVisibility(View.INVISIBLE);
            holder.lin_lc.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.black50));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView isselect;
        private ImageView arror_icon;
        private LinearLayout lin_lc;

        public MyViewHolder(View itemView) {
            super(itemView);
            lin_lc = (LinearLayout) itemView.findViewById(R.id.lin_lc);
            name = (TextView) itemView.findViewById(R.id.name);
            isselect = (TextView) itemView.findViewById(R.id.isselect);
            arror_icon = (ImageView) itemView.findViewById(R.id.arror_icon);

            arror_icon.setVisibility(View.GONE);
            isselect.setVisibility(View.VISIBLE);
            name.setGravity(Gravity.CENTER);
            final OnItemClickListener itemClickListener = (OnItemClickListener) mContext;
            if(itemClickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view,getLayoutPosition());
                    }
                });
            }
        }
    }
}
