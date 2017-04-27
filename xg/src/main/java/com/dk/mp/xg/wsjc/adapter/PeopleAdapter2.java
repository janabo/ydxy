package com.dk.mp.xg.wsjc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzSelectPersonsActivity2;

import java.util.HashMap;
import java.util.List;


/**
 * 作者：janabo on 2017/1/17 15:17
 */
public class PeopleAdapter2 extends RecyclerView.Adapter<PeopleAdapter2.MyViewHolder>{
    private SswzSelectPersonsActivity2 activity;
    private Context mContext;
    private List<Zssdjgl> mData;
    private LayoutInflater inflater;
    private HashMap<String, Object> isSelected = new HashMap<String, Object>();

    public PeopleAdapter2(Context mContext, List<Zssdjgl> mData, SswzSelectPersonsActivity2 activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        inflater = LayoutInflater.from(mContext);
    }

    public List<Zssdjgl> getList() {
        return mData;
    }

    public void setList(List<Zssdjgl> list) {
        this.mData = list;
    }

    public void notify(List<Zssdjgl> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public HashMap<String, Object> getIsSelected() {
        return isSelected;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_ssws_selectperson_item,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Zssdjgl bean = mData.get(position);
        holder.name.setText(bean.getName());
        if(isSelected.get(bean.getId()) != null) {
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
//            if(isSelected.get(mData.get(getLayoutPosition()).getId()) != null){
//                isSelected.clear();
//            }else{
//                isSelected.clear();
//                isSelected.put(mData.get(getLayoutPosition()).getId(),mData.get(getLayoutPosition()));
//            }
//            isSelected.put(mData.get(getLayoutPosition()).getId(),mData.get(getLayoutPosition()));

            if(isSelected.get(mData.get(getLayoutPosition()).getId()) != null){
                isSelected.remove((mData.get(getLayoutPosition()).getId()));
            }else{
                isSelected.put(mData.get(getLayoutPosition()).getId(),mData.get(getLayoutPosition()));
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
