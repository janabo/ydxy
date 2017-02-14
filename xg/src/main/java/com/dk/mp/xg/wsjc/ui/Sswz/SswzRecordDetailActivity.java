package com.dk.mp.xg.wsjc.ui.Sswz;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.xg.R;

/**
 * 作者：janabo on 2017/1/17 09:28
 */
public class SswzRecordDetailActivity extends MyActivity{
    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayoutID() {
        return R.layout.app_ssws_record_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        mWebView = (WebView) findViewById(R.id.content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_new_detail);

    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("宿舍违章登记");
        String id = getIntent().getStringExtra("id");
        setWebView ( );
        mWebView.loadUrl(getUrl("apps/sswzdj/detail?id="+id));
    }

    private void setWebView ( ) {
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient( mProgressBar ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        settings.setAppCacheEnabled ( true );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
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
            return mContext.getString(R.string.rootUrl)+url;
        }
    }


}
