package com.dk.mp.oldoa.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.dk.mp.core.util.Logger;
import com.dk.mp.oldoa.utils.CoreConstants;
import com.dk.mp.oldoa.utils.FileUtil;
import com.dk.mp.oldoa.utils.ProgressDialogUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class DetailView extends LinearLayout {
	WebView w;
	Context context;
	private String  filename;

	public DetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		w = new WebView(context);
		w.setInitialScale(100);
		WebSettings webSetting = w.getSettings();
		//设置js可用  
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setSupportZoom(true);

		//不显示webview缩放按钮
		webSetting.setDisplayZoomControls(false);

		w.setWebViewClient(new HttpWebViewClient());
		w.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				filename(url);
			}
		});
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

	private void download(String url,String path) {
		HttpUtils http = new HttpUtils();

		Logger.info("path==="+path);
		if (new File(path).exists()) {
			Logger.info(path+"exists============   ");
			context.startActivity(FileUtil.openFile(path));
			ProgressDialogUtil.getIntence(context).dismissDialog();
		} else {
			HttpHandler handler = http.download(url, path, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
					false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
					new RequestCallBack<File>() {

						@Override
						public void onStart() {
							Logger.info("conn...");
						}

						@Override
						public void onLoading(long total, long current, boolean isUploading) {
							Logger.info(current + "/" + total);
						}

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							ProgressDialogUtil.getIntence(context).dismissDialog();
							Logger.info("下载成功");
							context.startActivity(FileUtil.openFile(filename));
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							ProgressDialogUtil.getIntence(context).dismissDialog();
						}
					});
		}
	}
	
	
	/**
	 * 鑾峰彇鏂伴椈璇︽儏.
	 */
	public void filename(final String url) {
		ProgressDialogUtil.getIntence(context).onLoading("");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL u = new URL(url);
					HttpURLConnection httpConnection = (HttpURLConnection) u.openConnection();
					String str = httpConnection.getHeaderField("Content-Disposition");
					 filename = str.split("filename=")[1].replace("\"", "");
					filename = CoreConstants.BASEPICPATH +URLDecoder.decode(filename, "UTF-8");
					Logger.info("filename==="+filename);
					Message msg = new Message();
					msg.what=1;
					msg.obj=url;
					Logger.info("url==="+url);
					handler.sendMessage(msg);
				} catch (Exception e) {
					ProgressDialogUtil.getIntence(context).dismissDialog();
					e.printStackTrace();
				}
			
			}
		}).start();
	}
	
	// 瀹氫箟涓�釜Handler锛岀敤鏉ュ紓姝ュ鐞嗘暟鎹�
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String url=msg.obj.toString();
				download(url, filename);
				break;
			default:
				break;
			}
		};
	};


	public DetailView(Context context) {
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
		w.clearHistory();
		StringBuffer s = new StringBuffer();
		s.append(html);
		w.isHardwareAccelerated();
		w.loadDataWithBaseURL(null, s.toString(), "text/html", "utf-8", null);
		this.addView(w);
	}
}
