package com.dk.mp.txl.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.core.entity.Department;
import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.txl.R;
import com.dk.mp.txl.ui.PhonesDialog;
import com.dk.mp.txl.ui.TxlPersonsActivity;

import java.util.List;



/**
 * 作者：janabo on 2016/12/22 17:54
 */
public class TxlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Activity activity;
    Context mContext;
    List mData;
    int type;//1,星标 2，部门
    LayoutInflater inflater;

    public TxlAdapter(Context mContext,Activity activity,List mData, int type) {
        this.mContext = mContext;
        this.activity = activity;
        this.mData = mData;
        this.type = type;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_yellowpage_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof MyViewHolder ) {
            if(type == 1) {
                Jbxx j = (Jbxx) mData.get(position);
                ((MyViewHolder) holder).name.setText(j.getName());
            }else{
                Department j = (Department) mData.get(position);
                ((MyViewHolder) holder).name.setText(j.getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(type == 2){
                        Intent intent = new Intent(mContext, TxlPersonsActivity.class);
                        Department j = (Department) mData.get(getLayoutPosition());
                        intent.putExtra("id",j.getId());
                        intent.putExtra("title",j.getName());
                        mContext.startActivity(intent);
                    }else{
                        final PhonesDialog dlg = new PhonesDialog(mContext,activity);
                        Jbxx j = (Jbxx) mData.get(getLayoutPosition());
                        dlg.show(j);
                    }
                }
            });
        }
    }
}
