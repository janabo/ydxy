package com.dk.mp.core.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dk.mp.core.R;
import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.http.okhttp.OkHttp3Stack;
import com.dk.mp.core.http.request.GsonRequest;
import com.dk.mp.core.http.request.GsonRequestJson;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.http.request.HttpRequest;
import com.dk.mp.core.http.request.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.OkHttpClient;


/**
 * 作者：janabo on 2016/12/19 17:22
 * Volley 网络请求util
 */
public class HttpUtil {
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

//    /**
//     * post请求
//     * @param listener
//     */
//    public void post (String url, final Map< String, String > param, final HttpUtil.RequestListener listener ) {
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest ( Request.Method.POST, url, new Response.Listener<JSONObject> ( ) {
//            @Override
//            public void onResponse ( JSONObject response ) {
//                listener.onResponse ( response );
//            }
//        }, new Response.ErrorListener ( ) {
//            @Override
//            public void onErrorResponse ( VolleyError error ) {
//                listener.onError ( error );
//            }
//        } ){
//            @Override
//            protected Map< String, String > getParams ( ) throws AuthFailureError {
//                return param;
//            }
//        };
//        mRequestQueue.add ( jsonObjectRequest );
//    }

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
        HttpRequest httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.POST).addParam(param).build();
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
        HttpRequest httpRequest = new HttpRequest.Builder(getUrl(url)).setMethod(Request.Method.POST).addParam(param).build();
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



}
