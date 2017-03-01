package com.dk.mp.oldoa.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.BaoGaoDoc;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.Constant;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.dk.mp.oldoa.view.DetailView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @since
 * @version 2014-8-5
 * @author wangwei
 */
public class BaoGaoDetailActivity extends DetailActivity {
	private TextView bgNum;// 编号
	private TextView ngTime;// 拟稿时间
//	private LinearLayout contant;// 内容
	private TextView jinjCD;// 紧急程度
	private DetailView nr_txt;//内容
	private TextView qsdw_txt;//请示单位
	private TextView ngr_txt;//拟稿人
	private BaoGaoDoc faWenD;// 收文实体
	private String dealState;// 处理状态
	private ErrorLayout mError;
	String id;
	String title;
	CoreSharedPreferencesHelper shareHelper;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				faWenD.setTitle(title);
				faWenD.setType("OA_BG");
				faWenD.setDocumentState(dealState);
				fillUi(faWenD);
				fillPublicData(faWenD, BaoGaoDetailActivity.this);
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				break;

			default:
				break;
			}

		};

	};

	@Override
	protected int getLayoutID() {
		return R.layout.oa_bao_gao_view;
	}

	@Override
	protected void initView() {
		super.initView();
		shareHelper = new CoreSharedPreferencesHelper(this);
		dealState = getIntent().getStringExtra("dealState");
		id = getIntent().getStringExtra(Constant.TYPE_URL);
		title = getIntent().getStringExtra("title");
		findUi();
		getList();
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.oa_bao_gao_view);
//		shareHelper = new CoreSharedPreferencesHelper(this);
//		dealState = getIntent().getStringExtra("dealState");
//		id = getIntent().getStringExtra(Constant.TYPE_URL);
//		title = getIntent().getStringExtra("title");
//		findUi();
//		getList();
//	}

	/**
	 * 获取发文数据
	 */
	public void getList() {
		if (DeviceUtil.checkNet()) {
			HttpClientUtil.post("apps/oa/getQSBGDetail?"+id, null, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					faWenD = OAManager.getIntence().getOABGInfos(arg0);
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
		bgNum = (TextView) findViewById(R.id.bh_txt);
		ngTime = (TextView) findViewById(R.id.ng_time_txt);
//		contant = (LinearLayout) findViewById(R.id.nr);
		jinjCD = (TextView) findViewById(R.id.jin_jcd_txt);
		nr_txt = (DetailView) findViewById(R.id.nr_txt);
		ngr_txt = (TextView) findViewById(R.id.ngr_txt);

//		contant.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(BaoGaoDetailActivity.this,
//						ContentActivity.class);
//				intent.putExtra("title", title);
//				intent.putExtra("nr", faWenD.getZw());
//				startActivity(intent);
//			}
//		});
	}

	/**
	 * 填充控件
	 */
	private void fillUi(BaoGaoDoc doc) {
		if (StringUtils.isNotEmpty(doc.getBgNum())) {

			bgNum.setText(doc.getBgNum());
		} else {

			bgNum.setText("无");
		}
		if (StringUtils.isNotEmpty(doc.getTime())) {
			ngTime.setText(TimeUtils.getFormatTime(doc.getTime()));
		} else {

			ngTime.setText("无");
		}
//		if (StringUtils.isNotEmpty(doc.getZw())) {
//			contant.setVisibility(View.VISIBLE);
//		} else {
//			contant.setVisibility(View.GONE);
//		}

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
		if (StringUtils.isNotEmpty(doc.getZw())) {
			nr_txt.setText(doc.getZw());
		} else {
			nr_txt.setText("无");
		}
		
	}
}
