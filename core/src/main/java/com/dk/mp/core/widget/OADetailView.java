package com.dk.mp.core.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * 作者：janabo on 2017/1/16 15:23
 */
public class OADetailView extends LinearLayout {
    WebView w;
    Context context;

    public OADetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        w = new WebView(context);
        w.setInitialScale(100);
        WebSettings webSetting = w.getSettings();
        //设置js可用
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setSupportZoom(false);

        w.setWebViewClient(new HttpWebViewClient());
        w.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
            }
        });
    }

    //web视图客户端
    public class HttpWebViewClient extends WebViewClient {
        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        //结束加载
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    public OADetailView(Context context) {
        this(context, null);
    }

    public void setOnTouchListener(final GestureDetector detector) {
        w.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });
    }

    /**
     * 内容.
     * @param html 富文本字符串
     */
    public void setText(String html) {
        removeAllViews();
        w.clearCache(true);
        StringBuffer s = new StringBuffer();
        s.append(html);
        w.isHardwareAccelerated();
        w.loadDataWithBaseURL(null, s.toString(), "text/html", "utf-8", null);
        this.addView(w);
    }
}
