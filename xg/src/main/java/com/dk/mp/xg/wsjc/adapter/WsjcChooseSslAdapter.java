package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.ChooseSsl;
import com.dk.mp.xg.wsjc.ui.WsjcChooseSslActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cobb on 2017/5/8.
 */

public class WsjcChooseSslAdapter extends RecyclerView.Adapter<WsjcChooseSslAdapter.MyViewHolder>{
    private WsjcChooseSslActivity activity;
    private Context mContext;
    private List<ChooseSsl> mData;
    private LayoutInflater inflater;
    private HashMap<String, Object> isSelected = new HashMap<String, Object>();

    public WsjcChooseSslAdapter(Context mContext, List<ChooseSsl> mData,WsjcChooseSslActivity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        inflater = LayoutInflater.from(mContext);
    }

    public List<ChooseSsl> getList() {
        return mData;
    }

    public void setList(List<ChooseSsl> list) {
        this.mData = list;
    }

    public void notify(List<ChooseSsl> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public HashMap<String, Object> getIsSelected() {
        return isSelected;
    }

    @Override
    public WsjcChooseSslAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_ssws_selectperson_item,parent,false);
        return new WsjcChooseSslAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WsjcChooseSslAdapter.MyViewHolder holder, int position) {
        ChooseSsl bean = mData.get(position);
        holder.name.setText(bean.getName());
        if(isSelected.get(bean.getId()) != null) {
            holder.mRadioButton.setChecked(true);
            isSelected.put(bean.getId(),bean);
            Log.e("宿舍选择最后一条", position + "@" + bean.getId());
        }else{
            holder.mRadioButton.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private AppCompatRadioButton mRadioButton;
        private TextView name;
        private LinearLayout checkbox;
        public MyViewHolder(View itemView) {
            super(itemView);
            mRadioButton = (AppCompatRadioButton) itemView.findViewById(R.id.mRadioButton);
            name = (TextView) itemView.findViewById(R.id.name);
            checkbox= (LinearLayout) itemView.findViewById(R.id.checkbox);
            mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delRadioButton();
                }
            });

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delRadioButton();
                }
            });
        }

        public void delRadioButton(){
            if(isSelected.get(mData.get(getLayoutPosition()).getId()) != null){
                isSelected.clear();
            }else{
                isSelected.clear();
                isSelected.put(mData.get(getLayoutPosition()).getId(),mData.get(getLayoutPosition()));
                Log.e("宿舍选择最后一条", getLayoutPosition() + "@@" + mData.get(getLayoutPosition()).getId());
            }

//            if(isSelected.get(mData.get(getLayoutPosition()).getId()) != null){
//                isSelected.remove((mData.get(getLayoutPosition()).getId()));
//            }else{
//                isSelected.put(mData.get(getLayoutPosition()).getId(),mData.get(getLayoutPosition()));
//            }

            if(isSelected.isEmpty()){
                activity.dealOK(false);
            }else{
                activity.dealOK(true);
            }
            notifyDataSetChanged();
        }
    }
}
