package com.dk.mp.xg.wsjc.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：janabo on 2017/9/18 10:04
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = 0;
        outRect.bottom = space;
        outRect.top = 0;
        outRect.left = space;
    }
}
