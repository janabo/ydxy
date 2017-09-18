package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.DormitoryBuilding;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：janabo on 2017/9/15 15:15
 */
public class SelectDormitoryBuildingNewAdapter extends RecyclerView.Adapter<SelectDormitoryBuildingNewAdapter.MyViewHolder>{

    private Context mContext;
    private List<DormitoryBuilding> mData;
    private LayoutInflater inflater;
    private static HashMap<String, Object> isSelected = new HashMap<String, Object>();

    public SelectDormitoryBuildingNewAdapter(Context mContext, List<DormitoryBuilding> mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    public List<DormitoryBuilding> getList() {
        return mData;
    }

    public void setList(List<DormitoryBuilding> list) {
        this.mData = list;
    }

    public static HashMap<String, Object> getIsSelected() {
        return isSelected;
    }

    public void notify(List<DormitoryBuilding> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_wdrycx_xzlh_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DormitoryBuilding dormitoryBuilding = mData.get(position);
        holder.mSsl.setText(dormitoryBuilding.getName());
        if(isSelected.get(dormitoryBuilding.getId()) == null){
            holder.mSLin.setBackgroundResource(R.drawable.border_1c9ceb);
            holder.mSsl.setTextColor(Color.rgb(100,100,100));
            holder.mSsl2.setTextColor(Color.rgb(100,100,100));
        }else{
            holder.mSLin.setBackgroundResource(R.drawable.border_bg_1c9ceb);
            holder.mSsl.setTextColor(Color.rgb(255,255,255));
            holder.mSsl2.setTextColor(Color.rgb(255,255,255));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mSsl;
        private TextView mSsl2;
        private LinearLayout mSLin;

        public MyViewHolder(View itemView) {
            super(itemView);
            mSsl = (TextView) itemView.findViewById(R.id.ssl_name);
            mSsl2 = (TextView) itemView.findViewById(R.id.ssl_name2);
            mSLin = (LinearLayout) itemView.findViewById(R.id.select_lin);
            final OnItemClickListener itemClickListener = (OnItemClickListener) mContext;
            if(itemClickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view,getLayoutPosition());
                    }
                });
            }
//            mSLin.setOnClickListener(new View.OnClickListener() {//点击
//                @Override
//                public void onClick(View v) {
//                    DormitoryBuilding db = mData.get(getLayoutPosition());
//                    if(isSelected.get(db.getId()) != null){
//                        isSelected.remove(db.getId());
//                    }else {
//                        isSelected.clear();
//                        isSelected.put(db.getId(), db.getName());
//                    }
//                    notifyDataSetChanged();
//                }
//            });
        }
    }
}
