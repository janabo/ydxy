package com.dk.mp.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.R;
import com.dk.mp.core.view.LoadingView;

/**
 * 作者：janabo on 2016/12/16 16:11
 */
public class ErrorLayout extends LinearLayout implements
        View.OnClickListener {
    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;//网络错误
    public static final int DATAFAIL = 2;//获取失败
    public static final int NODATA = 3;//没有数据
    public static final int LOADDATA = 5;//加载数据
    public static final int SEARCHNODATA = 6;//搜索无数据
    public static final int SEARCHNODATA2 = 7; //背景不是白色
    private boolean clickEnable = true;//是否可以点击
    private final Context context;
    public ImageView img;//错误图片
    public LoadingView loadview;//加载
    private OnClickListener listener;//点击事件
    private int mErrorState; //错误状态
    private TextView tv;//错误提示

    public ErrorLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ErrorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.core_loadview, null);
        loadview = (LoadingView) view.findViewById(R.id.loadview);
        img = (ImageView) view.findViewById(R.id.zwsj_icon);
        tv = (TextView) view.findViewById(R.id.zwsj_text);
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        addView(view);
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            if (listener != null)
                listener.onClick(v);
        }
    }

    public int getErrorState() {
        return mErrorState;
    }

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    public void setErrorImag(int imgResource) {
        try {
            img.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    //设置图片
    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                loadview.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                img.setImageResource(R.mipmap.nonet);
                tv.setText(R.string.net_no2);
                clickEnable = true;
                break;
            case NODATA:
                mErrorState = NODATA;
                loadview.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                img.setImageResource(R.mipmap.nodata_n);
                tv.setText(R.string.nodata);
                clickEnable = true;
                break;
            case SEARCHNODATA:
                mErrorState = SEARCHNODATA;
                loadview.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                img.setImageResource(R.mipmap.searchnodata);
                tv.setText(R.string.searchnodata);
                clickEnable = true;
                break;
            case DATAFAIL:
                mErrorState = DATAFAIL;
                loadview.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                img.setImageResource(R.mipmap.errorserver);
                tv.setText(R.string.data_fail);
                clickEnable = true;
                break;
            case LOADDATA:
                loadview.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                clickEnable = false;
                break;
            case SEARCHNODATA2:
                mErrorState = SEARCHNODATA;
                loadview.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                img.setImageResource(R.mipmap.searchnodata);
                tv.setText(R.string.searchnodata);
                tv.setTextColor(getResources().getColor(R.color.white));
                clickEnable = true;
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void setTextColor(int color){
        tv.setTextColor(color);
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
