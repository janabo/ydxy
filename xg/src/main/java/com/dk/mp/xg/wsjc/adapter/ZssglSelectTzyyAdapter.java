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
import com.dk.mp.xg.wsjc.entity.Common;
import com.dk.mp.xg.wsjc.ui.zssgl.ZssglSelectTzyyActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择调整原因适配器
 * 作者：janabo on 2017/3/9 19:55
 */
public class ZssglSelectTzyyAdapter extends RecyclerView.Adapter<ZssglSelectTzyyAdapter.MyViewHolder>{
    private ZssglSelectTzyyActivity activity;
    private Context mContext;
    private List<Common> mData;
    private LayoutInflater inflater;
    private HashMap<String, Object> isSelected = new HashMap<String, Object>();
    private Map<String,String> tzyyMap = new HashMap<>();

    public ZssglSelectTzyyAdapter(Context mContext, List<Common> mData,ZssglSelectTzyyActivity activity,Map<String,String> tzyyMap) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        this.tzyyMap = tzyyMap;
        inflater = LayoutInflater.from(mContext);
    }

    public List<Common> getList() {
        return mData;
    }

    public void setList(List<Common> list) {
        this.mData = list;
    }

    public void notify(List<Common> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public HashMap<String, Object> getIsSelected() {
        return isSelected;
    }

    @Override
    public ZssglSelectTzyyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_ssws_selectperson_item,null,false);
        return new ZssglSelectTzyyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZssglSelectTzyyAdapter.MyViewHolder holder, int position) {
        Common bean = mData.get(position);
        holder.name.setText(bean.getName());
        if(!tzyyMap.isEmpty() && tzyyMap.get(bean.getId()) != null){
            isSelected.put(bean.getId(),bean.getName());
            tzyyMap.remove(bean.getId());
        }
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
            if(isSelected.get(mData.get(getLayoutPosition()).getId()) != null){
                isSelected.remove(mData.get(getLayoutPosition()).getId());
            }else{
                isSelected.put(mData.get(getLayoutPosition()).getId(),mData.get(getLayoutPosition()).getName());
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
