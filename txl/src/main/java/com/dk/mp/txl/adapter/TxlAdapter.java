package com.dk.mp.txl.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.core.entity.Department;
import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.core.util.BitmapUtil;
import com.dk.mp.core.util.anni.TransitionHelper;
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
                        ViewCompat.setTransitionName(view, "detail_element");
                        ViewCompat.setTransitionName(activity.findViewById(R.id.fab), "fab");

                        ActivityOptionsCompat options = TransitionHelper.makeOptionsCompat(activity,
                                Pair.create(view,"detail_element"),Pair.create(activity.findViewById(R.id.fab), "fab"));

                        Intent intent = new Intent(mContext, TxlPersonsActivity.class);
                        Department j = (Department) mData.get(getLayoutPosition());
                        intent.putExtra("id",j.getId());
                        intent.putExtra("title",j.getName());
                        View backgroundView = activity.findViewById(R.id.bm_recycle);
                        if (backgroundView != null) BitmapUtil.storeBitmapInIntent(BitmapUtil.createBitmap(backgroundView), intent);

                        ActivityCompat.startActivity(activity,intent,options.toBundle());
                       activity.overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
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
