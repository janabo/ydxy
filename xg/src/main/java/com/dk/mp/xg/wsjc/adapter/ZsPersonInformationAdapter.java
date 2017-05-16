package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.ChooseSsl;
import com.dk.mp.xg.wsjc.entity.InformationQuery;
import com.dk.mp.xg.wsjc.ui.WsjcChooseSslActivity;
import com.dk.mp.xg.wsjc.ui.zsskq.ZsPersonInformationQueryActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cobb on 2017/5/8.
 */

public class ZsPersonInformationAdapter extends RecyclerView.Adapter<ZsPersonInformationAdapter.MyViewHolder>{
    private ZsPersonInformationQueryActivity activity;
    private Context mContext;
    private List<InformationQuery> mData;
    private LayoutInflater inflater;

    public ZsPersonInformationAdapter(Context mContext, List<InformationQuery> mData, ZsPersonInformationQueryActivity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        inflater = LayoutInflater.from(mContext);
    }

    public List<InformationQuery> getList() {
        return mData;
    }

    public void setList(List<InformationQuery> list) {
        this.mData = list;
    }

    public void notify(List<InformationQuery> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    @Override
    public ZsPersonInformationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.ad_zs_infortain_adapter,null,false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.ad_zs_infortain_adapter, parent, false);
        return new ZsPersonInformationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZsPersonInformationAdapter.MyViewHolder holder, int position) {
        InformationQuery bean = mData.get(position);

        holder.bjmc.setText(bean.getBjmc());
        holder.xm.setText(bean.getXm());
        holder.lxdh.setText(bean.getLxdh());
        holder.ssq.setText(bean.getSsq());
        holder.ssl.setText(bean.getSsl());
        holder.fjh.setText(bean.getFjh());
        holder.bzr.setText(bean.getBzr());
        holder.bzrlxdh.setText(bean.getBzrlxdh());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView bjmc, xm, lxdh, ssq, ssl, fjh, bzr, bzrlxdh;

        public MyViewHolder(View itemView) {
            super(itemView);

            bjmc = (TextView) itemView.findViewById(R.id.bjmc);
            xm = (TextView) itemView.findViewById(R.id.xm);
            lxdh = (TextView) itemView.findViewById(R.id.lxdh);
            ssq = (TextView) itemView.findViewById(R.id.ssq);
            ssl = (TextView) itemView.findViewById(R.id.ssl);
            fjh = (TextView) itemView.findViewById(R.id.fjh);
            bzr = (TextView) itemView.findViewById(R.id.bzr);
            bzrlxdh = (TextView) itemView.findViewById(R.id.bzrlxdh);

        }

    }
}
