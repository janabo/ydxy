package com.dk.mp.xg.wsjc.ui.Sswz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.ui.ImagePreviewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/1/17 09:28
 */
public class SswzRecordDetailActivity extends MyActivity{
    private ErrorLayout mError;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    LoginMsg loginMsg;

    @Override
    protected int getLayoutID() {
        return R.layout.app_ssws_record_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        loginMsg = getSharedPreferences().getLoginMsg();
        mWebView = (WebView) findViewById(R.id.content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_new_detail);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("宿舍违纪登记");
        String id = getIntent().getStringExtra("id");
        setWebView ( );
        String mUrl = getUrl("apps/sswzdj/detail?id="+id);
        Logger.info("url =" +mUrl);
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
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                List<String> data = new ArrayList<String>();
                data.add(url);
                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                intent.putExtra("index", 0);
                intent.putStringArrayListExtra("list", (ArrayList<String>) data);
                mContext.startActivity(intent);
            }
        });
    }


    public class MyWebViewClient extends WebViewClient {
        ProgressBar mProgressBar;
        public MyWebViewClient ( ProgressBar progressBar ) {
            super ( );
            mProgressBar = progressBar;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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
            url += "&uid="+loginMsg.getUid()+"&pwd="+ loginMsg.getPsw();
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return mContext.getString(R.string.rootUrl)+url;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            back();
        }
        return super.onKeyDown(keyCode, event);
    }

}
