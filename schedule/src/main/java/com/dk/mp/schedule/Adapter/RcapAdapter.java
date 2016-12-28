package com.dk.mp.schedule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.schedule.R;
import com.dk.mp.schedule.RcapDetailActivity;

import java.util.List;

/**
 * 日程安排首页日程适配器
 * 作者：janabo on 2016/12/27 17:06
 */
public class RcapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<RcapDetail> mData;
    LayoutInflater inflater;
    String date;

    public RcapAdapter(Context mContext,List<RcapDetail> mData,String date) {
        this.mContext = mContext;
        this.mData = mData;
        this.date = date;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_rcap_main_item,parent,false);
        return new RcapAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof RcapAdapter.MyViewHolder) {
            RcapDetail j = (RcapDetail) mData.get(position);
            ((MyViewHolder) holder).rcsj.setText(j.getStime());
            ((MyViewHolder) holder).rczt.setText(j.getTitle());
            ((MyViewHolder) holder).rcdd.setText(j.getLocation());
            ((MyViewHolder) holder).rcnr.setText(j.getContent());
            String currtime = TimeUtils.getCurrTime();
            if(TimeUtils.comparedTime2(currtime,j.getTime_start())){
                ((MyViewHolder) holder).image_rcap.setImageResource(R.mipmap.not_start);
            }else if(TimeUtils.comparedTime2(j.getTime_end(),currtime)){
                ((MyViewHolder) holder).image_rcap.setImageResource(R.mipmap.end);
            }else{
                ((MyViewHolder) holder).image_rcap.setImageResource(R.mipmap.not_end);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rcsj;//日程开始时间
        private TextView rczt;//日程主题
        private TextView rcdd;//日程地点
        private TextView rcnr;//日程内容
        private ImageView image_rcap;

        public MyViewHolder(View itemView) {
            super(itemView);
            image_rcap = (ImageView) itemView.findViewById(R.id.image_rcap);
            rcsj  = (TextView) itemView.findViewById(R.id.rcsj);
            rczt  = (TextView) itemView.findViewById(R.id.rczt);
            rcdd  = (TextView) itemView.findViewById(R.id.rcdd);
            rcnr  = (TextView) itemView.findViewById(R.id.rcnr);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RcapDetailActivity.class);
                    intent.putExtra("title",date);
                    intent.putExtra("rcapDetail",mData.get(getLayoutPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
