package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.core.util.StringUtils;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.DfRecord;
import com.dk.mp.xg.wsjc.ui.WsjcRecordDetailActivity;

import java.util.List;

/**
 * 作者：janabo on 2017/1/12 14:57
 */
public class WsjcRecordListAdapter extends RecyclerView.Adapter<WsjcRecordListAdapter.MyViewHolder>{
    private Context mContext;
    private List<DfRecord> mData;
    LayoutInflater inflater;

    public WsjcRecordListAdapter(Context mContext, List<DfRecord> mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_wsjc_record_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DfRecord m = mData.get(position);
        holder.ssl_fjh.setText(m.getSsl()+"-"+m.getFjh());
        holder.ssq.setText(m.getSsq());
        holder.fs.setText(m.getFs());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView ssl_fjh;
        private TextView ssq;
        private TextView fs;

        public MyViewHolder(View itemView) {
            super(itemView);
            ssl_fjh = (TextView) itemView.findViewById(R.id.ssl_fjh);
            ssq = (TextView) itemView.findViewById(R.id.ssq);
            fs = (TextView) itemView.findViewById(R.id.fs);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DfRecord df = mData.get(getLayoutPosition());
                    Intent intent = new Intent(mContext,WsjcRecordDetailActivity.class);
                    intent.putExtra("id",df.getId());
                    intent.putExtra("x",(view.getLeft() + view.getRight()) / 2);
                    intent.putExtra("y",(view.getTop() + view.getBottom()) / 2 + StringUtils.dip2px(mContext,40));
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
