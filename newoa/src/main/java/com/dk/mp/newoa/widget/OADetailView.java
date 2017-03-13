package com.dk.mp.newoa.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.util.FileUtil;
import com.dk.mp.core.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import okhttp3.Call;
import okhttp3.Response;

@SuppressLint("NewApi") public class OADetailView extends LinearLayout {
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE};
	private String mFilepath = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";
	WebView w;
	Context context;
	Activity activity;
	private String  filename;

	public OADetailView(Context context,AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		w = new WebView(context);
		w.setInitialScale(100);
		WebSettings webSetting = w.getSettings();
		//设置js可用  
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setBuiltInZoomControls(false);
		webSetting.setSupportZoom(false);

		w.setWebViewClient(new HttpWebViewClient());
		w.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				verifyStoragePermissions(url);
			}
		});
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	//web视图客户端
	public class HttpWebViewClient extends WebViewClient {
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		//开始加载  
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		//结束加载  
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

	private void download(String url, final String path) {
		Logger.info("path==="+path);
		if (new File(path).exists()) {
			Logger.info(path+"exists============   ");
			context.startActivity(FileUtil.openFile(path));
		}else{
			HttpUtil.getInstance().downloadFile(url,new okhttp3.Callback(){
				@Override
				public void onFailure(Call call, IOException e) {
					call.cancel();// 上传失败取消请求释放内存
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					download(response,path);
					call.cancel();// 上传失败取消请求释放内存
				}
			});
		}
	}

	public void download(Response response,String path){
		InputStream is = null;
		byte[] buf = new byte[2048];
		int len = 0;
		FileOutputStream fos = null;
		try {
			is = response.body().byteStream();
			File file = new File(path);
			fos = new FileOutputStream(file);
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			context.startActivity(FileUtil.openFile(filename));
			Logger.info("下载成功");
		}catch (Exception e){
			e.printStackTrace();
			Logger.info("下载失败");
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				Logger.info("下载失败");
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
	}
	}


	/**
	 * 文件下载
	 */
	public void filename(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL u = new URL(url);
					HttpURLConnection httpConnection = (HttpURLConnection) u.openConnection();
					String str = httpConnection.getHeaderField("Content-Disposition");
					filename = str.split("filename=")[1].replace("\"", "");
					filename = mFilepath + URLDecoder.decode(filename, "UTF-8");
					Logger.info("filename==="+filename);
					Message msg = new Message();
					msg.what=1;
					msg.obj=url;
					Logger.info("url==="+url);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	// 适配器
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					String url=msg.obj.toString();
					download(url, filename);
					break;
				case 2:

					break;
				default:
					break;
			}
		};
	};

	public OADetailView(Context context) {
		this(context, null);
	}

	public void setOnTouchListener(final GestureDetector detector) {
		w.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				return false;
			}
		});
	}

	/**
	 * 内容.
	 * @param html 富文本字符串
	 */
	public void setText(String html) {
		removeAllViews();
		w.clearCache(true);
		StringBuffer s = new StringBuffer();
		s.append(html);
		w.isHardwareAccelerated();
		w.loadDataWithBaseURL(null, s.toString(), "text/html", "utf-8", null);
		this.addView(w);
	}


	/**
	 * 请求读写权限
	 * @param
	 */
	public void verifyStoragePermissions(String url) {
		int permission = ActivityCompat.checkSelfPermission(activity,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int rePermission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED || rePermission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE);
		}else{
			filename(url);
		}
	}

}
