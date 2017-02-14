package com.dk.mp.xg.wsjc.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.Logger;
import com.dk.mp.xg.R;

/**
 * 作者：janabo on 2017/1/13 16:19
 */
public class WsjcTjFragment extends BaseFragment{
//    private ErrorLayout mError;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    @Override
    protected int getLayoutId() {
        return R.layout.app_wsjctj_fragment_detail;
    }

    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mWebView = (WebView) view.findViewById(R.id.mWebview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_new_detail);
        setWebView();
    }

    public void setMUrl(String url){
        url = getUrl(url);
        Logger.info("##########murl="+url);
        mWebView.removeAllViews();
        mWebView.clearCache(true);
        mWebView.loadUrl (url);
        mWebView.reload();
    }

    private void setWebView ( ) {
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient ( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient ( mProgressBar ) );
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
            return getActivity().getString(R.string.rootUrl)+url;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }
}
