package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.core.util.StringUtils;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Dfxx;

import java.util.List;

/**
 * 作者：janabo on 2017/1/9 15:54
 */
public class WsjcDetailAdapter extends RecyclerView.Adapter<WsjcDetailAdapter.MyViewHolder>{
    Context mContext;
    List<Dfxx> mData;
    LayoutInflater inflater;

    public WsjcDetailAdapter(Context mContext,List<Dfxx> mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_wsjc_detail_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Dfxx dfxx = mData.get(position);
        holder.wsxx.setText(dfxx.getMc());
        String fs;
        if(StringUtils.isNotEmpty(dfxx.getFs()))
            fs = dfxx.getFs();
        else
            fs = "0";
        holder.star.setText(fs);
    }

    public interface SaveEditListener {
        void SaveEdit(String dfid,int score);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView wsxx;//打分名称
        private TextView star;//分值
        private ImageView subtract;//减少
        private ImageView add;//增加

        public MyViewHolder(View itemView) {
            super(itemView);
            wsxx = (TextView) itemView.findViewById(R.id.wsxx);
            star = (TextView) itemView.findViewById(R.id.star);
            subtract = (ImageView) itemView.findViewById(R.id.subtract);
            add = (ImageView) itemView.findViewById(R.id.add);
            final SaveEditListener listener= (SaveEditListener) mContext;
            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int starscore = Integer.parseInt(star.getText().toString());

                    if(starscore>0){
                        star.setText(starscore-1+"");
                        listener.SaveEdit(mData.get(getLayoutPosition()).getId(),-1);
                    }
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dfxx dfxx = mData.get(getLayoutPosition());
                    int starscore = Integer.parseInt(star.getText().toString());
                    int fs = 0;
                    if(StringUtils.isNotEmpty(dfxx.getFs())){
                        fs = Integer.parseInt(dfxx.getFs());
                    }
                    if(starscore<fs){
                        star.setText(starscore+1+"");
                        listener.SaveEdit(mData.get(getLayoutPosition()).getId(),+1);
                    }
                }
            });
        }
    }
}
