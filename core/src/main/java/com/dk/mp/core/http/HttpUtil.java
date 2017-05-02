package com.dk.mp.core.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dk.mp.core.R;
import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.okhttp.OkHttp3Stack;
import com.dk.mp.core.http.request.GsonRequest;
import com.dk.mp.core.http.request.GsonRequestJson;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.http.request.HttpRequest;
import com.dk.mp.core.http.request.JsonObjectRequest;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.Logger;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * 作者：janabo on 2016/12/19 17:22
 * Volley 网络请求util
 */
public class HttpUtil {
    public static final String TYPE = "application/octet-stream";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("text/html");
    public final String TAG = this.getClass ( ).getSimpleName ( );
    public static Context mContext = MyApplication.getContext();
    private static HttpUtil httpUtil;
    private final RequestQueue mRequestQueue;
    OkHttpClient okHttpClient;

    private HttpUtil ( ) {
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        mRequestQueue = Volley.newRequestQueue(mContext,
                new OkHttp3Stack(okHttpClient));
    }
    public static HttpUtil getInstance ( ) {
        if ( httpUtil == null ) {
            synchronized ( HttpUtil.class ) {
                if ( httpUtil == null )
                    httpUtil = new HttpUtil ( );
            }
        }
        return httpUtil;
    }

    /**
     * gson 请求获取data外层的数据
     * @param url
     * @param listener
     * @param <T>
     */
    public <T> void getGsonRequestJson(Class<T> tClass, String url, HttpListener<T> listener) {
        HttpRequest httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.GET).build();
        GsonRequestJson<T> request = new GsonRequestJson<T>(tClass,httpRequest,listener);
        mRequestQueue.add (request);
    }

    /**
     * gson 请求获取data里面的数据
     * @param typeToken
     * @param url
     * @param param
     * @param listener
     * @param <T>
     */
    public <T> void gsonRequest(TypeToken<T> typeToken, String url, Map<String,Object> param, HttpListener<T> listener) {
        HttpRequest httpRequest;
        if(param == null ||param.isEmpty()) {
            param = new HashMap<>();
        }
        LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
        if (loginMsg != null&&!"login".equals(url)) {
            param.put("uid", loginMsg.getUid());
            param.put("pwd", loginMsg.getPsw());
        }
        if(param.isEmpty()){
            httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.GET).build();
        }else{
            httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.POST).addParam(param).build();
        }
        GsonRequest<T> request = new GsonRequest<T>(typeToken,httpRequest,listener);
        mRequestQueue.add (request);
    }

    /**
     * gson 请求获取data外层的数据
     * @param url
     * @param param
     * @param listener
     * @param <T>
     */
    public <T> void gsonRequestJson(Class<T> tClass, String url, Map<String,Object> param, HttpListener<T> listener) {
        HttpRequest httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.POST).addParam(param).build();
        GsonRequestJson<T> request = new GsonRequestJson<T>(tClass,httpRequest,listener);
        mRequestQueue.add (request);
    }

    public void postJsonObjectRequest(String url, Map<String,Object> param,HttpListener<JSONObject> listener) {
        HttpRequest httpRequest;
        Log.e("-----","---------"+getUrl(url));
        if(param == null ||param.isEmpty()) {
            param = new HashMap<>();
        }
        LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
        if (loginMsg != null&&!"login".equals(url)) {
            param.put("uid", loginMsg.getUid());
            param.put("pwd", loginMsg.getPsw());
            httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.POST).addParam(param).build();
        }else if("login".equals(url)){
                httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.POST).addParam(param).build();
        }else {
                httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.GET).build();
        }
        JsonObjectRequest request = new JsonObjectRequest(httpRequest, listener);
        mRequestQueue.add (request);
    }

    /**
     * 处理url
     * @param url
     * @return
     */
    private static String getUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return mContext.getResources().getString(R.string.rootUrl) + url;
        }
    }

    /**
     * 上传图片
     */
    public void uploadImg(String url,List<File> files,okhttp3.Callback callback){
        Logger.info("############ uploadImg Url = "+url);
        // mImgUrls为存放图片的url集合
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        for (int i = 0; i <files.size() ; i++) {
//            File f= files.get(i);
//            if (f!=null) {
//                builder.addFormDataPart("fileName", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
//            }
//        }
//        // 创建RequestBody
//        RequestBody body = builder.build();
        if(files.size()<=0){
            return;
        }

        //创建RequestBody
        RequestBody fileBody  = RequestBody.create(MediaType.parse(TYPE), files.get(0));
        RequestBody body = new MultipartBody.Builder().addFormDataPart("filename", files.get(0).getName(), fileBody).build();

        //构建请求
        final okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(body).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 文件下载
     */
    public void downloadFile(String url,okhttp3.Callback callback){
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
