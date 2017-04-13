package com.dk.mp.xxxw.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dk.mp.core.entity.News;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xxxw.R;

/**
 * 学校新闻详情
 * 作者：janabo on 2016/12/20 15:01
 */
public class NewsDetailActivity extends MyActivity implements View.OnClickListener{
    Context context = NewsDetailActivity.this;
    ImageView mImageViewTop;
    ProgressBar mProgressBar;
    WebView mWebView;
    News news;
    Toolbar mToolbar;
//    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.app_news_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        mImageViewTop = (ImageView) findViewById(R.id.iv_new_detail_top);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_new_detail);
        mWebView = (WebView) findViewById(R.id.webview_new_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mError = (ErrorLayout) findViewById(R.id.error_layout);
        ViewCompat.setTransitionName(mImageViewTop, "detail_element");
        initData();
    }

    public void initData(){
        news = (News) getIntent().getSerializableExtra("news");
        Glide.with(mContext).load(news.getImage()).fitCenter().into(mImageViewTop);
        setWebView ( );
        if ( mToolbar != null ) {
            mToolbar.setTitle ( "学校新闻" );
            setSupportActionBar ( mToolbar );
            getSupportActionBar ( ).setHomeButtonEnabled ( true );
            getSupportActionBar ( ).setDisplayHomeAsUpEnabled ( true );
        }

        setNavigationClick ( );
        ErrorLayout layout = (ErrorLayout)findViewById(R.id.errorlayout);
        if (news.getUrl() == null) {
            layout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            layout.setErrorType(ErrorLayout.NODATA);
        } else {
            String url = getUrl(news.getUrl());
            if(DeviceUtil.checkNet()) {
                layout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(url);
            }else{
                layout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                layout.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        }
    }

    private void setNavigationClick ( ) {
        mToolbar.setNavigationOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    onBackPressed ( );
                }
            }
        } );
    }

    private void setWebView ( ) {
//        mError.setErrorType(ErrorLayout.LOADDATA);
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient ( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient ( mProgressBar ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        settings.setAppCacheEnabled ( true );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
    }

    @Override
    public void onClick(View view) {

    }


    public class MyWebViewClient extends WebViewClient {
        ProgressBar mProgressBar;
        public MyWebViewClient ( ProgressBar progressBar ) {
            super ( );
            mProgressBar = progressBar;
        }

        @Override
        public void onPageStarted ( WebView view, String url, Bitmap favicon ) {
            super.onPageStarted ( view, url, favicon );
            mProgressBar.setVisibility ( View.VISIBLE );
        }

        @Override
        public void onPageFinished ( WebView webView, String url ) {
            super.onPageFinished ( webView, url );
            mProgressBar.setVisibility ( View.INVISIBLE );
//            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        ProgressBar mWebProgressBar;

        public MyWebChromeClient ( ProgressBar mWebProgressBar ) {
            this.mWebProgressBar = mWebProgressBar;
        }

        @Override
        public void onProgressChanged ( WebView view, int newProgress ) {
            mWebProgressBar.setProgress ( newProgress );
            Logger.info("##########newProgress="+newProgress);
//            if(newProgress>=100){
//                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
//            }
        }

        @Override
        public void onReceivedTitle ( WebView view, String title ) {
            super.onReceivedTitle ( view, title );
        }
    }


    /**
     * 处理url
     * @param url
     * @return
     */
    private String getUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return context.getString(R.string.rootUrl)+url;
        }
    }


    @Override
    protected void onPause ( ) {
        super.onPause ( );
        mWebView.onPause ();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            onBackPressed ( );
        }
        return super.onKeyDown(keyCode, event);
    }

}
