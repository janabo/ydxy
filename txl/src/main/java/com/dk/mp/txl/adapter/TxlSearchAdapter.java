package com.dk.mp.txl.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.txl.R;
import com.dk.mp.txl.ui.PhonesDialog;

import java.util.List;

/**
 * 作者：janabo on 2016/12/26 16:03
 */
public class TxlSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Activity activity;
    Context mContext;
    List mData;
    LayoutInflater inflater;

    public TxlSearchAdapter(Context mContext,Activity activity,List mData) {
        this.mContext = mContext;
        this.activity = activity;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_search_item,parent,false);
        return new TxlSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof TxlSearchAdapter.MyViewHolder) {
            Jbxx j = (Jbxx) mData.get(position);
            ((TxlSearchAdapter.MyViewHolder) holder).name.setText(j.getName());
            ((TxlSearchAdapter.MyViewHolder) holder).department.setText(j.getDepartmentname());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;//姓名
        private TextView department;//部门

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            department = (TextView) itemView.findViewById(R.id.department);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PhonesDialog dlg = new PhonesDialog(mContext,activity);
                    Jbxx j = (Jbxx) mData.get(getLayoutPosition());
                    dlg.show(j);
                }
            });
        }
    }
}
