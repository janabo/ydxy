package com.dk.mp.oldoa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.adapter.DocAdapter;
import com.dk.mp.oldoa.entity.Doc;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.Constant;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.dk.mp.oldoa.view.XListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListActivity extends MyActivity implements XListView.IXListViewListener {
	List<Doc> list;
	XListView listview;
	Activity context;
	DocAdapter docAdapter;
	String state;//0代表待处理，1代表已处理
	CoreSharedPreferencesHelper shareHelper;
	String interfaceUri;
	private int curPage = 1;
	private int countPage =1;
	private ErrorLayout mError;
	private LinearLayout mRootView;

//	@Override
//	protected int getLayoutID() {
//		return R.layout.oa_list;
//	}
//
//	@Override
//	protected void initView() {
//		super.initView();
//		shareHelper = new CoreSharedPreferencesHelper(this);
//		context = ListActivity.this;
//		state = getIntent().getStringExtra("state");
//		interfaceUri = getIntent().getStringExtra("interface");
//		mError = (ErrorLayout) findViewById(R.id.error_layout);
//		mRootView = (LinearLayout) findViewById(R.id.mRootView);
//		listview = (XListView) findViewById(R.id.listView);
//		listview.setPullLoadEnable(true);
//		listview.setXListViewListener(this);
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
//				Doc doc = (Doc) adapter.getItemAtPosition(position);
//				Intent intentActivity = new Intent();
//				if ("OA_SW".equals(doc.getType())) {
//					intentActivity.setClass(context, ShouWenDetailActivity.class);
//					intentActivity.putExtra("dealState", state);
//					intentActivity.putExtra("title", doc.getTitle());
//					intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
//					startActivity(intentActivity);
//				} else if ("OA_FW".equals(doc.getType())) {
//					intentActivity.setClass(context, FaWenDetailActivity.class);
//					intentActivity.putExtra("dealState", state);
//					intentActivity.putExtra("title", doc.getTitle());
//					intentActivity.putExtra("bzid", doc.getId());
//					intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
//					startActivity(intentActivity);
//				} else {
//					intentActivity.setClass(context, BaoGaoDetailActivity.class);
//					intentActivity.putExtra("dealState", state);
//					intentActivity.putExtra("title", doc.getTitle());
//					intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
//					startActivity(intentActivity);
//				}
//
//			}
//		});
//		//模拟登陆 TODO
////		simulateLogin();
//		if (DeviceUtil.checkNet()) {
//			getListData();
//		}
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_list);
		shareHelper = new CoreSharedPreferencesHelper(this);
		context = ListActivity.this;
		state = getIntent().getStringExtra("state");
		interfaceUri = getIntent().getStringExtra("interface");
		mError = (ErrorLayout) findViewById(R.id.error_layout);
		mRootView = (LinearLayout) findViewById(R.id.mRootView);
		listview = (XListView) findViewById(R.id.listView);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
				Doc doc = (Doc) adapter.getItemAtPosition(position);
				Intent intentActivity = new Intent();
				if ("OA_SW".equals(doc.getType())) {
					intentActivity.setClass(context, ShouWenDetailActivity.class);
					intentActivity.putExtra("dealState", state);
					intentActivity.putExtra("title", doc.getTitle());
					intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
					startActivity(intentActivity);
				} else if ("OA_FW".equals(doc.getType())) {
					intentActivity.setClass(context, FaWenDetailActivity.class);
					intentActivity.putExtra("dealState", state);
					intentActivity.putExtra("title", doc.getTitle());
					intentActivity.putExtra("bzid", doc.getId());
					intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
					startActivity(intentActivity);
				} else {
					intentActivity.setClass(context, BaoGaoDetailActivity.class);
					intentActivity.putExtra("dealState", state);
					intentActivity.putExtra("title", doc.getTitle());
					intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
					startActivity(intentActivity);
				}

			}
		});
		//模拟登陆 TODO
//		simulateLogin();
		if (DeviceUtil.checkNet()) {
			getListData();
		}
	}

	public void getListData() {
//		showProgressDialog();
		Map<String,String> map = new HashMap<String, String>();
		map.put("pageNo", curPage+"");
		HttpClientUtil.post(interfaceUri, map, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				handler.sendEmptyMessage(-1);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				PageMsg page = OAManager.getIntence().getOAListInfos(arg0);
				list = page.getList();
				countPage = (int) page.getTotalPages();
				if(list.size()>0){
					handler.sendEmptyMessage(0);
				}else{
					handler.sendEmptyMessage(1);
				}
			}
		});
	}

	// 定义一个Handler，用来异步处理数据
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("0".equals(state)) {
				Intent intent = new Intent(Constant.UPDATE_COUNT_UI);
				intent.putExtra("count", list !=null ?String.valueOf(list.size()):0);
				sendBroadcast(intent);
			}
			switch (msg.what) {
			case 0:
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				if (docAdapter == null) {
					docAdapter = new DocAdapter(context, list);
					listview.setAdapter(docAdapter);
				} else {
					docAdapter.setList(list);
					docAdapter.notifyDataSetChanged();
				}
				if (curPage >= countPage) {
					listview.hideFooter();
				} else {
					listview.showFooter();
				}
				listview.stopRefresh();
				listview.stopLoadMore();
				break;
			case 1:
				mError.setErrorType(ErrorLayout.NODATA);
				break;
			case 2:
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				showErrorMsg(mRootView,"没有获取到更多数据");
				break;
			case -1:
//				showMessage("获取数据失败");
				mError.setErrorType(ErrorLayout.DATAFAIL);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onRefresh() {
		if (DeviceUtil.checkNet()) {
			curPage = 1;
			getListData();
		} else {
			mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
			listview.stopRefresh();
		}
	}

	@Override
	public void onLoadMore() {
		if (DeviceUtil.checkNet()) {
			curPage++;
			getDataLoadMore();
		}
	}

	@Override
	public void stopLoad() {
		
	}

	@Override
	public void getList() {
		
	}

	public void getDataLoadMore() {
		if (DeviceUtil.checkNet()) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("pageNo", curPage+"");
			HttpClientUtil.post(interfaceUri, map, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					handler.sendEmptyMessage(-1);
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					PageMsg page = OAManager.getIntence().getOAListInfos(arg0);
					countPage = (int) page.getTotalPages();
					if(page.getList().size()>0){
						list.addAll(page.getList());
						handler.sendEmptyMessage(0);
					}else{
						handler.sendEmptyMessage(2);
					}
				}
			});
		} 
	}

	/**
	 * 模拟登陆 调试用 正式发布前注掉
	 */
	public void simulateLogin(){
		HttpClientUtil.get("http://ydoa.czlgj.com/mp/login?user=10000060&psw=luixiy123", new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
			}
		});
	}
	
}
