package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzSdluActivity;
import com.dk.mp.xg.wsjc.ui.zssdjgl.ZssdjglAddLiusuActivity;

import java.util.List;

/**
 * 作者：janabo on 2017/2/8 17:46
 */
public class SswzSdluPersonsAdapter extends RecyclerView.Adapter{
    private List<Zssdjgl> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private SswzSdluActivity activity;

    public SswzSdluPersonsAdapter(List<Zssdjgl> mData, Context mContext, SswzSdluActivity activity) {
        this.mData = mData;
        this.mContext = mContext;
        this.activity = activity;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.app_zssdjgl_persons,null,false);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(DeviceUtil.getScreenWidth(mContext)/5, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Zssdjgl bean = mData.get(position);
        if("addperson".equals(bean.getId())) {
            ((MyViewHolder) holder).wjxs_lin.setVisibility(View.GONE);
            ((MyViewHolder) holder).wjxs_img.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).wjxs_name.setVisibility(View.GONE);
            ((MyViewHolder) holder).wjxs_img.setImageResource(R.mipmap.add_r);
        }else{
            ((MyViewHolder) holder).wjxs_lin.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).wjxs_img.setVisibility(View.GONE);
            ((MyViewHolder) holder).wjxs_name.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).wjxs_x.setText(StringUtils.isNotEmpty(bean.getName())?bean.getName().substring(0,1):"");
            ((MyViewHolder) holder).wjxs_name.setText(bean.getName());
            activity.getBackgroud(((MyViewHolder) holder).wjxs_lin,StringUtils.isNotEmpty(bean.getName())?bean.getName().substring(0,1):"");
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView wjxs_img;
        private TextView wjxs_name;
        private LinearLayout wjxs_lin;
        private TextView wjxs_x;

        public MyViewHolder(View itemView) {
            super(itemView);
            wjxs_img = (ImageView) itemView.findViewById(R.id.wjxs_img);
            wjxs_name = (TextView) itemView.findViewById(R.id.wjxs_name);
            wjxs_lin = (LinearLayout) itemView.findViewById(R.id.wjxs_lin);
            wjxs_x = (TextView) itemView.findViewById(R.id.wjxs_x);

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
