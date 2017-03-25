package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;

/**
 * 住宿生管理详情
 * 作者：janabo on 2017/2/6 17:51
 */
public class ZssglDetailActivity extends MyActivity implements View.OnClickListener{
    WebView mWebView;
    Button pass,notpass,untread;//通过，不通过，退回
    String detailid,lmlb,mType,sfksh,xb,rzzt;
    private ErrorLayout mError;
    private LinearLayout lin_footer,footer2,footer;
    public static ZssglDetailActivity instance;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("在校生住宿管理");
        mWebView = (WebView) findViewById(R.id.content);
        pass = (Button) findViewById(R.id.pass);
        notpass = (Button) findViewById(R.id.notpass);
        untread = (Button) findViewById(R.id.untread);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        lin_footer = (LinearLayout) findViewById(R.id.lin_footer);
        footer2 = (LinearLayout) findViewById(R.id.footer2);
        footer = (LinearLayout) findViewById(R.id.footer);
        mError.setOnLayoutClickListener(this);
        instance = ZssglDetailActivity.this;
    }

    @Override
    protected void initialize() {
        super.initialize();
        detailid = getIntent().getStringExtra("detailid");
        lmlb = getIntent().getStringExtra("lmlb");
        mType = getIntent().getStringExtra("mType");
        sfksh = getIntent().getStringExtra("sfksh");
        rzzt = getIntent().getStringExtra("rzzt");
        xb = getIntent().getStringExtra("xb");
        if("true".equals(sfksh)) {//可以审核
            if ("3".equals(mType)) {
                lin_footer.setVisibility(View.VISIBLE);
                footer2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            } else if ("4".equals(mType) && "1".equals(lmlb) && "true".equals(rzzt)) {
                lin_footer.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                footer2.setVisibility(View.VISIBLE);
            } else {
                lin_footer.setVisibility(View.GONE);
            }
        }else{
            if ("4".equals(mType) && "1".equals(lmlb) && "true".equals(rzzt)) {
                lin_footer.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                footer2.setVisibility(View.VISIBLE);
            }else {
                lin_footer.setVisibility(View.GONE);
            }
        }
        getData();
    }

    /**
     * 获取数据
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            LoginMsg loginMsg = getSharedPreferences().getLoginMsg();
            String mUrl = "apps/zxzssgl/detail?id="+detailid+"&lmlb="+lmlb+"&uid=";
            if(loginMsg != null){
                mUrl += loginMsg.getUid()+"&pwd="+ loginMsg.getPsw();
            }
            setMUrl(mUrl);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            lin_footer.setVisibility(View.GONE);
        }
    }

    /**
     * lmlb：1（停宿）、2(调宿)、3(退宿)
     * 通过
     * @param v
     */
    public void toPass(View v){
        if("1".equals(lmlb)){
            toSubmit("2","通过","apps/zxzssgl/tingsush");
        }else if("2".equals(lmlb)){
            toTiaosuSubmit("2");
        }else if("3".equals(lmlb)){
            toSubmit("2","通过","apps/zxzssgl/tuisush");
        }

    }

    /**
     * 不通过
     * @param v
     */
    public void toNotPass(View v){
        if("1".equals(lmlb)){
            toSubmit("3","不通过","apps/zxzssgl/tingsush");
        }else if("2".equals(lmlb)){
            toTiaosuSubmit("3");
        }else if("3".equals(lmlb)){
            toSubmit("3","不通过","apps/zxzssgl/tuisush");
        }
    }

    /**
     * 不通过
     * @param v
     */
    public void toUntread(View v){
        if("1".equals(lmlb)){
            toSubmit("4","退回","apps/zxzssgl/tingsush");
        }else if("2".equals(lmlb)){
            toTiaosuSubmit("4");
        }else if("3".equals(lmlb)){
            toSubmit("4","退回","apps/zxzssgl/tuisush");
        }
    }

    /**
     * 住宿
     * @param v
     */
    public void toPutup(View v){
        Intent intent = new Intent(mContext,ZssglPutupActivity.class);
        intent.putExtra("detailid",detailid);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    public void setMUrl(String url){
        setWebView();
        url = getUrl(url);
        Logger.info("##########murl="+url);
        mWebView.removeAllViews();
        mWebView.clearCache(true);
        mWebView.loadUrl (url);
    }

    private void setWebView ( ) {
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient() );
        mWebView.setWebChromeClient ( new MyWebChromeClient( ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_NO_CACHE );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
    }

    @Override
    public void onClick(View view) {
        getData();
    }


    public class MyWebViewClient extends WebViewClient {
        public MyWebViewClient ( ) {
            super ( );
        }

        @Override
        public void onPageStarted ( WebView view, String url, Bitmap favicon ) {
            super.onPageStarted ( view, url, favicon );
        }

        @Override
        public void onPageFinished ( WebView webView, String url ) {
            super.onPageFinished ( webView, url );
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        public MyWebChromeClient () {
        }

        @Override
        public void onProgressChanged ( WebView view, int newProgress ) {
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
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return mContext.getString(com.dk.mp.core.R.string.rootUrl)+url;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    private void toSubmit(String flag,String flagName,String url){
        Intent intent = new Intent(mContext,ZssglSubmitActivity.class);
        intent.putExtra("detailid",detailid);
        intent.putExtra("flag",flag);
        intent.putExtra("flagName",flagName);
        intent.putExtra("url",url);
        startActivity(intent);
//        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        overridePendingTransition(R.anim.push_up_in, 0);
    }

    private void toTiaosuSubmit(String flag){
        Intent intent = new Intent(mContext,ZssglTiaoSuSubmitActivity.class);
        intent.putExtra("detailid",detailid);
        intent.putExtra("flag",flag);
        intent.putExtra("lmlb",lmlb);
        intent.putExtra("xb",xb);
        startActivity(intent);
    }

}
