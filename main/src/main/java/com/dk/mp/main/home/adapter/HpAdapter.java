package com.dk.mp.main.home.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.db.AppManager;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.AppUtil;
import com.dk.mp.core.util.DrawableUtils;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.main.R;
import com.dk.mp.main.home.data.AbstractDataProvider;
import com.dk.mp.main.home.ui.HeaderView;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

/**
 * Created by dongqs on 16/7/23.
 */
public class HpAdapter extends RecyclerView.Adapter<HpAdapter.MyViewHolder> implements DraggableItemAdapter<HpAdapter.MyViewHolder> {

    private static final String TAG = "MyDraggableItemAdapter";
    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;
    private Vibrator mVibrator;
    private Context mContext;

    private int index = -1;
    private int onclickPosition = -1;

    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }

    private AbstractDataProvider mProvider;

    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public FrameLayout mContainer;
        public ImageView imageView;
        public TextView mTextView;
        public ImageButton mImageButton;
        public Button deleteButton;

        public String text;
        public String id;
        public String action;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            imageView = (ImageView) v.findViewById(R.id.item_image);
            mTextView = (TextView) v.findViewById(R.id.item_text);
            mImageButton = (ImageButton) v.findViewById(R.id.gragItemViewDelete);
            deleteButton = (Button) v.findViewById(R.id.deleteButton);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public HpAdapter(AbstractDataProvider dataProvider, Context context) {
        mProvider = dataProvider;
        mContext = context;
        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    public void setItemMoveMode(int itemMoveMode) {
        mItemMoveMode = itemMoveMode;
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v;
        if(viewType == 0){
            v = inflater.inflate(R.layout.gridview_item, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index != -1){
                        index = -1;
                        notifyDataSetChanged();
                    } else {
                        new AppUtil(mContext).checkApp(new App(
                                "",
                                "",
                                "",
                                mProvider.getItem(onclickPosition).getText(),
                                String.valueOf(mProvider.getItem(onclickPosition).getId()),
                                "",
                                "",
                                mProvider.getItem(onclickPosition).getAction()));
                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    return true;
                }
            });
        }else{
            v = inflater.inflate(R.layout.gridview_hearder, parent, false);
            HeaderView headerView = new HeaderView();
            headerView.init(v,mContext);
        }
        return new MyViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AbstractDataProvider.Data item = mProvider.getItem(position);
        if (getItemViewType(position) == 0){
            holder.mTextView.setText(item.getText());
            holder.imageView.setImageResource(ImageUtil.getResource(item.getIcon()));
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppManager.updateNotin(mContext,mProvider.getItem(position).getAppid(),true);
                    mProvider.removeItem(position);
                    if(!mProvider.getItem(mProvider.getCount()-1).getText().equals("显示应用")){
                        mProvider.addItem(MyApplication.app);
                    }
                    index = -1;
                    notifyDataSetChanged();
                }
            });
            holder.setAction(mProvider.getItem(position).getAction());
            holder.setText(mProvider.getItem(position).getText());
            holder.setId(String.valueOf(mProvider.getItem(position).getId()));
            // set background resource (target view ID: container)
            final int dragState = holder.getDragStateFlags();
            if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
                if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                    mVibrator.vibrate(50); //震动一下
                    // need to clear drawable state here to get correct appearance of the dragging item.
                    DrawableUtils.clearState(holder.mContainer.getForeground());

                    index = position;
                    holder.mContainer.setBackgroundColor(Color.rgb(239,239,244));

                } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                    holder.mImageButton.setVisibility(View.GONE);
                    holder.deleteButton.setVisibility(View.GONE);
                    holder.mContainer.setBackgroundResource(R.drawable.bg_item_dragging_state);
                } else {
                    if(index == position){
                        holder.mImageButton.setVisibility(View.VISIBLE);
                        holder.deleteButton.setVisibility(View.VISIBLE);
                    }else{
                        holder.mImageButton.setVisibility(View.GONE);
                        holder.deleteButton.setVisibility(View.GONE);
                    }
                    holder.mContainer.setBackgroundResource(R.drawable.bg_item_normal_state);
                }
            } else {
                if (index == -1){
                    holder.mImageButton.setVisibility(View.GONE);
                    holder.deleteButton.setVisibility(View.GONE);
                }else{
                    if(index == position){
                        holder.mImageButton.setVisibility(View.VISIBLE);
                        holder.deleteButton.setVisibility(View.VISIBLE);
                    }else{
                        holder.mImageButton.setVisibility(View.GONE);
                        holder.deleteButton.setVisibility(View.GONE);
                    }
                }
            }
        }else{

        }
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        index = -1;
        if (mItemMoveMode == RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT) {
            mProvider.moveItem(fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        } else {
            mProvider.swapItem(fromPosition, toPosition);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        onclickPosition = position;
        if(holder.getItemViewType() == 1) return false;
        if(mProvider.getItem(position).getText().equals("显示应用")) return false;
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        if(mProvider.getItem(mProvider.getCount()-1).getText().equals("显示应用")){
            return new ItemDraggableRange(1,mProvider.getCount() == 2 ? 1 : mProvider.getCount()-2);
        }
        return new ItemDraggableRange(1,mProvider.getCount()-1);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    //合并第一行
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0 ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
}
