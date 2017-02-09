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
import com.dk.mp.xg.wsjc.entity.Student;
import com.dk.mp.xg.wsjc.ui.zssgl.ZssglSelectPersonsActivity;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：janabo on 2017/1/23 14:50
 */
public class ZssglSelectPersonsAdapter extends RecyclerView.Adapter<ZssglSelectPersonsAdapter.MyViewHolder>{
    private ZssglSelectPersonsActivity activity;
    private Context mContext;
    private List<Student> mData;
    private LayoutInflater inflater;
    private HashMap<String, Object> isSelected = new HashMap<String, Object>();

    public ZssglSelectPersonsAdapter(Context mContext, List<Student> mData,ZssglSelectPersonsActivity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        inflater = LayoutInflater.from(mContext);
    }

    public List<Student> getList() {
        return mData;
    }

    public void setList(List<Student> list) {
        this.mData = list;
    }

    public void notify(List<Student> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public HashMap<String, Object> getIsSelected() {
        return isSelected;
    }

    @Override
    public ZssglSelectPersonsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_ssws_selectperson_item,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZssglSelectPersonsAdapter.MyViewHolder holder, int position) {
        Student bean = mData.get(position);
        holder.name.setText(bean.getXm());
        if(isSelected.get(bean.getXh()) != null) {
            holder.mRadioButton.setChecked(true);
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
            if(isSelected.get(mData.get(getLayoutPosition()).getXh()) != null){
                isSelected.clear();
            }else{
                isSelected.clear();
                isSelected.put(mData.get(getLayoutPosition()).getXh(),mData.get(getLayoutPosition()));
            }
            if(isSelected.isEmpty()){
                activity.dealOK(false);
            }else{
                activity.dealOK(true);
            }
            notifyDataSetChanged();
        }
    }
}
