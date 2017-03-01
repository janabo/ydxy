package com.dk.mp.xg.wsjc.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.encrypt.Base64Utils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;

/**
 * 宿舍卫生打分详情
 * 作者：janabo on 2017/3/1 13:51
 */
public class WsjcRecordDetailActivity extends MyActivity{
    private ErrorLayout mError;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    LoginMsg loginMsg;

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjc_record_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        loginMsg = getSharedPreferences().getLoginMsg();
        mWebView = (WebView) findViewById(R.id.content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_new_detail);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("宿舍卫生检查");
        String id = getIntent().getStringExtra("id");
        setWebView ( );
        String mUrl = getUrl("apps/sswzdf/detail?id="+id);
        Logger.info("url="+mUrl);
        mWebView.loadUrl(mUrl);
    }

    private void setWebView ( ) {
        mError.setErrorType(ErrorLayout.LOADDATA);
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient( mProgressBar ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_NO_CACHE );
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
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
            if(newProgress>=100){
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
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
        if(loginMsg != null){
            url += "&uid="+loginMsg.getUid()+"&pwd="+ Base64Utils.getBase64(loginMsg.getPsw());
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return mContext.getString(R.string.rootUrl)+url;
        }
    }
}
