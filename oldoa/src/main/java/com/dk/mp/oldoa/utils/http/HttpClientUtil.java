package com.dk.mp.oldoa.utils.http;

import android.content.Context;

import com.dk.mp.core.R;
import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.oldoa.dialog.MsgDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @since
 * @version 2012-11-21
 * @author wangw
 */
public class HttpClientUtil {
	private static HttpUtils httpUtils = null;
	private static final int SO_TIMEOUT = 60 * 1000; // 设置等待数据超时时间10秒钟
	/**
	 * get请求获取json数据.
	 * @param url 请求链接
	 */
	public static void post(String url, Map<String, String> map, RequestCallBack<String> callBack) {
		Context context = MyApplication.getContext();
		
		if (httpUtils == null) {
			httpUtils= new HttpUtils();
			httpUtils.configTimeout(SO_TIMEOUT);
			httpUtils.configRequestThreadPoolSize(10);
			httpUtils.configSoTimeout(SO_TIMEOUT);
		}
		RequestParams params = new RequestParams();
		LoginMsg loginMsg = new CoreSharedPreferencesHelper(context).getLoginMsg();
		if (loginMsg != null&&!"login".equals(url)) {
			params.addBodyParameter("uid", loginMsg.getUid());
			params.addBodyParameter("pwd", loginMsg.getPsw());
			Logger.info("POST 请求参数:" + ("uid=" + loginMsg.getUid()));
			Logger.info("POST 请求参数:" + ("pwd=" + loginMsg.getPsw()));
		}
		if (map != null) {
			
			for (Map.Entry<String, String> entry :  map.entrySet()) {
				Logger.info("POST 请求参数:" + (entry.getKey() + "=" + entry.getValue()));
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		Logger.info("POST 请求连接=======" + getUrl(context, url));
		httpUtils.send(HttpRequest.HttpMethod.POST, getUrl(context, url), params, callBack);
	}

	
	/**
	 * get请求获取json数据.
	 * @param url 请求链接
	 */
	public static void post(String url, Map<String, String> map,final int what,final HttpClientCallBack callBack) {
		final Context context = MyApplication.getContext();
		if (httpUtils == null) {
			httpUtils= new HttpUtils();
			httpUtils.configTimeout(SO_TIMEOUT);
			httpUtils.configRequestThreadPoolSize(10);
			httpUtils.configSoTimeout(SO_TIMEOUT);
		}
		
		RequestParams params = new RequestParams();
		LoginMsg loginMsg = new CoreSharedPreferencesHelper(context).getLoginMsg();
		if (loginMsg != null&&!"login".equals(url)) {
			params.addBodyParameter("uid", loginMsg.getUid());
			params.addBodyParameter("pwd", loginMsg.getPsw());
		}
		
		if (map != null) {
			for (Map.Entry<String, String> entry :  map.entrySet()) {
				Logger.info("POST 请求参数:" + (entry.getKey() + "=" + entry.getValue()));
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		Logger.info("POST 请求连接=======" + getUrl(context, url));
		httpUtils.send(HttpRequest.HttpMethod.POST, getUrl(context, url), params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				callBack.success(what,getJSONObject(responseInfo));
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MsgDialog.show(context, "服务发送错误，请稍后重试");
				callBack.fail(what);
			}
		});
	}
	
	
	
	public static void upload(String url, List<String> filePths, RequestCallBack<String> callBack) {
		Context context =   MyApplication.getContext();
		try {
			RequestParams params = new RequestParams();
			MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
			for(int i = 0;i<filePths.size();i++){
				ContentBody cbFile = new FileBody(new File(filePths.get(i)));
				mpEntity.addPart("file" +i, cbFile); // <input type="file"
			}
			params.setBodyEntity(mpEntity);
			HttpUtils http = new HttpUtils();
			Logger.info("POST 请求连接=======" + getUrl(context, url));
			http.send(HttpRequest.HttpMethod.POST, getUrl(context, url), params, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * json转换.
	 * 
	 *            HttpResponse
	 * @return JSONObject
	 */
	public static JSONObject getJSONObject(ResponseInfo<String> responseInfo) {
		try {
			Logger.info("返回原始数据:\n" + responseInfo.result);
			if (StringUtils.isNotEmpty(responseInfo.result)) {
				if (responseInfo.result.toLowerCase().startsWith("<html")) {
					return null;
				} else {
					JSONObject json = new JSONObject(responseInfo.result);
					return json;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 处理请求链接.
	 * 
	 *            请求链接
	 * @return 处理后的请求链接
	 */
	private static String getUrl(Context context, String url) {
		if (url.startsWith("http://")) {
			return url;
		} else {
			return context.getResources().getString(R.string.rootUrl) + url;
		}
	}
	
	
	/**
	 * get请求获取json数据.
	 * @param url 请求链接
	 */
	public static void get(String url, RequestCallBack<String> callBack) {
		Context context =  MyApplication.getContext();
		if (httpUtils == null) {
			httpUtils= new HttpUtils();
			httpUtils.configTimeout(SO_TIMEOUT);
			httpUtils.configRequestThreadPoolSize(10);
			httpUtils.configSoTimeout(SO_TIMEOUT);
		}
		Logger.info("POST 请求连接=======" + getUrl(context, url));
		httpUtils.send(HttpRequest.HttpMethod.GET, getUrl(context, url), null, callBack);
	}

}
