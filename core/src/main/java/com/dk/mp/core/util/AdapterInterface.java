package com.dk.mp.core.util;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by dongqs on 2016/11/2.
 */

public interface AdapterInterface {

    /**
     * 加载layout
     * @return
     */
    RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType);

    /**
     * item赋值
     */
    void setItemValue(RecyclerView.ViewHolder holder, int position);

    /**
     * 数据加载
     */
    void loadDatas();
}
