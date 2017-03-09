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
import com.dk.mp.xg.wsjc.entity.Kf;

import java.util.List;

/**
 * 卫生检查楼层适配器
 * 作者：janabo on 2017/1/11 17:02
 */
public class WsjcRecordMainAdapter extends RecyclerView.Adapter<WsjcRecordMainAdapter.MyViewHolder>{
    private Context mContext;
    List<Kf> mData;
    LayoutInflater inflater;
    int type;//1,2,3  3代表楼层
    int selected;//选择第几个

    public WsjcRecordMainAdapter(Context mContext,List<Kf> mData,int type,int selected) {
        this.mContext = mContext;
        this.mData = mData;
        this.type = type;
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_wsjc_record_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(type == 3) {
            holder.name.setText("楼层"+mData.get(position).getName());
        }else{
            holder.name.setText(mData.get(position).getName());
        }
        if(selected == position && type != 3){
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
        void onItemClick(View view , int position,int type);
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

            if(type == 3){
                arror_icon.setVisibility(View.VISIBLE);
                isselect.setVisibility(View.INVISIBLE);
                name.setGravity(Gravity.LEFT);
            }else{
                arror_icon.setVisibility(View.GONE);
                isselect.setVisibility(View.VISIBLE);
                name.setGravity(Gravity.CENTER);
            }
            final OnItemClickListener itemClickListener = (OnItemClickListener) mContext;
            if(itemClickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view,getLayoutPosition(),type);
                    }
                });
            }
        }
    }
}
