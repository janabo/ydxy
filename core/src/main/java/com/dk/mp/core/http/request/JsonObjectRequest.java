package com.dk.mp.core.http.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <b>Project:</b> com.yong.volleyok.request <br>
 * <b>Create Date:</b> 2016/4/23 <br>
 * <b>Author:</b> qingyong <br>
 * <b>Description:</b> JsonObject Request <br>
 */
public class JsonObjectRequest extends RequestWrapper<JSONObject> {

    public JsonObjectRequest(HttpRequest httpRequest, HttpListener<JSONObject> listener) {
        super(httpRequest, listener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String result = getResponseString(response);
            JSONObject jsonObject = new JSONObject(result);
            if(result.startsWith("{\"json\"")){
                jsonObject = jsonObject.optJSONObject("json");
            }
            if(result.startsWith("{\"jsonp\"")){
                jsonObject = jsonObject.optJSONObject("jsonp");
            }
            result = jsonObject.toString();
            if (result.equals(PARSEERROR)) {
                return Response.error(new ParseError());
            }

            return Response.success(new JSONObject(result), HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
