package com.dk.mp.core.http.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Response.success;

/**
 * 获取最外层的json对象
 * 作者：janabo on 2016/12/21 15:49
 */
public class GsonRequestJson<T> extends RequestWrapper<T> {

    private static Gson mGson = new Gson();
    private Class<T> mClass;
    private TypeToken<T> mTypeToken;

    public GsonRequestJson(Class<T> tClass, HttpRequest httpRequest, HttpListener<T> listener) {
        super(httpRequest, listener);
        mClass = tClass;
    }

    public GsonRequestJson(TypeToken<T> typeToken,
                       HttpRequest httpRequest, HttpListener<T> listener) {
        super(httpRequest, listener);
        mTypeToken = typeToken;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String result = getResponseString(response);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(result.startsWith("{\"json\"")){
                jsonObject = jsonObject.optJSONObject("json");
            }
            if(result.startsWith("{\"jsonp\"")){
                jsonObject = jsonObject.optJSONObject("jsonp");
            }
            if (result.equals(PARSEERROR) || jsonObject == null) {
                return Response.error(new ParseError());
            }
            result = jsonObject.toString();
            if (mTypeToken == null) {
                return success(mGson.fromJson(result, mClass), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                Response<T> tResponse = (Response<T>) Response.success(mGson.fromJson(result, mTypeToken.getType()), HttpHeaderParser.parseCacheHeaders(response));
                return tResponse;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError());
        }
    }
}
