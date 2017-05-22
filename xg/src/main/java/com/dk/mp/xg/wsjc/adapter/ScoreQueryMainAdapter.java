package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Score;

import java.util.List;


/**
 * 作者：janabo on 2017/5/8 14:07
 */
public class ScoreQueryMainAdapter extends RecyclerView.Adapter<ScoreQueryMainAdapter.MyViewHolder>{
    private List<Score> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public ScoreQueryMainAdapter(List<Score> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.app_scorequery_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewGroup.LayoutParams lp_c = holder.coursename.getLayoutParams();
        if(position == 0){
            holder.coursename.setTextColor(Color.rgb(19,181,177));
            holder.coursescore.setTextColor(Color.rgb(19,181,177));
//            lp_c.height = DeviceUtil.dip2px(mContext,34);
//            holder.coursename.setLayoutParams(lp_c);
//            holder.coursescore.setLayoutParams(lp_c);
        }else{
            int wColor = ContextCompat.getColor(mContext,R.color.black21);
            holder.coursename.setTextColor(wColor);
            holder.coursescore.setTextColor(wColor);
//            lp_c.height = DeviceUtil.dip2px(mContext,45);
//            holder.coursename.setLayoutParams(lp_c);
//            holder.coursescore.setLayoutParams(lp_c);
        }
        Score score = mData.get(position);
        holder.coursename.setText(score.getKcmc());
        holder.coursescore.setText(score.getCj());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView coursename;
        private TextView coursescore;

        public MyViewHolder(View itemView) {
            super(itemView);
            coursename = (TextView) itemView.findViewById(R.id.coursename);
            coursescore = (TextView) itemView.findViewById(R.id.coursescore);
        }
    }
}
