package com.dk.mp.core.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by dongqs on 16/8/12.
 */
public class DraweeView extends SimpleDraweeView {
    public DraweeView(Context context) {
        super(context);
    }

    public DraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    // looks like overwrite this method can fix this issue
// but still don't figure out why
    public void animateTransform(Matrix matrix) {
        invalidate();
    }
}
