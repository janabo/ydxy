package com.dk.mp.xg.wsjc.ui.mispersoninfomation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;

/**
 * 作者：janabo on 2017/5/25 18:17
 */
public class MissingPersonActivity extends MyActivity{
    WebView mWebview;
    private ErrorLayout mError;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayoutID() {
        return R.layout.core_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mWebview = (WebView) findViewById(R.id.webview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        String url = getIntent().getStringExtra("url");
        mError.setErrorType(ErrorLayout.LOADDATA);
        if(DeviceUtil.checkNet()){
            loadurl(url);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

    }

   private void loadurl(String url){
        Logger.info("url:" + url);
        WebSettings webSetting = mWebview.getSettings();
        //设置js可用
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setLightTouchEnabled(true);
        webSetting.setSupportZoom(false);
       mWebview.setHapticFeedbackEnabled(false);
            //载入具体的web地址
       mWebview.setVisibility(View.VISIBLE);
       mWebview.requestFocus();
       mWebview.loadUrl(url);
       mWebview.setWebViewClient(new HttpWebViewClient());
       mWebview.setWebChromeClient(new WebChromeClient());
       mWebview.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
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
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress>=100){
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
        }
    }
}
