package com.dk.mp.oldoa.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.ShouWenDoc;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.Constant;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class ShouWenDetailActivity extends DetailActivity {

	private TextView laiwUnit;// 来文单位
	private TextView laiwZH;// 来文字号
	private TextView shouwWH;// 收文文号
	private TextView shouwTime;// 收文时间
	private TextView jinjCD;// 紧急程度
	private ShouWenDoc shouWenD;// 收文实体
	private String dealState;// 处理状态
	private TextView ngr_txt;//拟稿人
	String id;
	String title;
	CoreSharedPreferencesHelper shareHelper;
	private ErrorLayout mError;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				shouWenD.setTitle(title);
				shouWenD.setType("OA_SW");
				shouWenD.setDocumentState(dealState);
				fillUi(shouWenD);
				fillPublicData(shouWenD, ShouWenDetailActivity.this);
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				break;

			default:
				break;
			}

		};

	};


	@Override
	protected int getLayoutID() {
		return R.layout.oa_shou_wen_view;
	}

	@Override
	protected void initView() {
		super.initView();
		shareHelper = new CoreSharedPreferencesHelper(this);
		dealState = getIntent().getStringExtra("dealState");
		id = getIntent().getStringExtra(Constant.TYPE_URL);
		title = getIntent().getStringExtra("title");
		findUi();
//		showProgressDialog();
		mError.setErrorType(ErrorLayout.LOADDATA);
		getList();
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.oa_shou_wen_view);
//		shareHelper = new CoreSharedPreferencesHelper(this);
//		dealState = getIntent().getStringExtra("dealState");
//		id = getIntent().getStringExtra(Constant.TYPE_URL);
//		title = getIntent().getStringExtra("title");
//		findUi();
////		showProgressDialog();
//		mError.setErrorType(ErrorLayout.LOADDATA);
//		getList();
//	}

	/**
	 * 获取发文数据
	 */
	public void getList() {
//		showProgressDialog();
		mError.setErrorType(ErrorLayout.LOADDATA);
		if (DeviceUtil.checkNet()) {
			HttpClientUtil.post("apps/oa/getSWDetail?" + id, null, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					shouWenD = OAManager.getIntence().getOASWInfos(responseInfo);
					mHandler.sendEmptyMessage(0);
				}
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					mHandler.sendEmptyMessage(0);
				}
			});
		} 
	}

	/**
	 * 加载控件
	 */
	private void findUi() {
		mError = (ErrorLayout) findViewById(R.id.error_layout);
		laiwUnit = (TextView) findViewById(R.id.lai_wen_nuit_txt);
		laiwZH = (TextView) findViewById(R.id.lai_wen_zh_txt);
		shouwWH = (TextView) findViewById(R.id.shou_wen_wh_txt);
		shouwTime = (TextView) findViewById(R.id.shou_wen_time_txt);
		jinjCD = (TextView) findViewById(R.id.shou_wen_jjcd_txt);
		ngr_txt = (TextView) findViewById(R.id.ngr_txt);

	}

	/**
	 * 填充控件
	 */
	private void fillUi(ShouWenDoc doc) {
		if (StringUtils.isNotEmpty(doc.getLaiwUnit())) {

			laiwUnit.setText(doc.getLaiwUnit());
		} else {

			laiwUnit.setText("无");
		}
		if (StringUtils.isNotEmpty(doc.getLaiwZH())) {

			laiwZH.setText(doc.getLaiwZH());
		} else {

			laiwZH.setText("无");
		}

		if (StringUtils.isNotEmpty(doc.getShouwWH())) {

			shouwWH.setText(doc.getShouwWH());
		} else {

			shouwWH.setText("无");
		}
		Logger.info(doc.getShouwTime()+"+++++++++++++++++++++++++++++++++++++++");
		if (StringUtils.isNotEmpty(doc.getShouwTime())) {
			
			shouwTime.setText(TimeUtils.getFormatTime(doc.getShouwTime()));
		} else {

			shouwTime.setText("无");
		}
		if (StringUtils.isNotEmpty(doc.getJinjCD())) {

			jinjCD.setText(doc.getJinjCD());
		} else {

			jinjCD.setText("无");
		}
		if (StringUtils.isNotEmpty(doc.getNgr())) {
			ngr_txt.setText(doc.getNgr());
		} else {

			ngr_txt.setText("无");
		}

	}
}
