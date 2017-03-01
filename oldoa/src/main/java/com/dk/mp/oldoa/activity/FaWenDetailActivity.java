package com.dk.mp.oldoa.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.FaWenDoc;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.Constant;
import com.dk.mp.oldoa.utils.CoreConstants;
import com.dk.mp.oldoa.utils.FileUtil;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;

public class FaWenDetailActivity extends DetailActivity implements OnClickListener {
	private TextView fwFileType;// 文件类型
	private TextView fwJGwz;// 机关代字
	private TextView fwNum;// 发文编号
	private TextView jinjCD;// 紧急程度
	private TextView zhengWen;// 正文
	private TextView zhengWenEmpty;// 正文为空
	private TextView ngr_txt;//拟稿人
	private FaWenDoc faWenD;// 收文实体
	private String dealState;// 处理状态
	String id;
	String title;
	String bzid;//id
	CoreSharedPreferencesHelper shareHelper;
	private ErrorLayout mError;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
//			hideProgressDialog();
			mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
			switch (msg.what) {
			case 0:
//				hideProgressDialog();
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				startActivity(FileUtil.openFile(CoreConstants.DOWNLOADPATH + fileName(bzid, faWenD.getTitle()+".docx")));
				break;
			case 1:
//				hideProgressDialog();
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				showErrorMsg("下载失败");
				break;
			case 3:
				faWenD.setType("OA_FW");
				faWenD.setTitle(title);
				faWenD.setDocumentState(dealState);
				fillUi(faWenD);
				fillPublicData(faWenD, FaWenDetailActivity.this);
				mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
				break;
			default:
				break;
			}

		};

	};

	@Override
	protected int getLayoutID() {
		return R.layout.oa_fa_wen_view;
	}

	@Override
	protected void initView() {
		super.initView();
		shareHelper = new CoreSharedPreferencesHelper(this);
		dealState = getIntent().getStringExtra("dealState");
		id = getIntent().getStringExtra(Constant.TYPE_URL);
		title = getIntent().getStringExtra("title");
		bzid = getIntent().getStringExtra("bzid");
		findUi();
		getList();
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.oa_fa_wen_view);
//		shareHelper = new CoreSharedPreferencesHelper(this);
//		dealState = getIntent().getStringExtra("dealState");
//		id = getIntent().getStringExtra(Constant.TYPE_URL);
//		title = getIntent().getStringExtra("title");
//		bzid = getIntent().getStringExtra("bzid");
//		findUi();
//		getList();
//	}

	/**
	 * 获取发文数据
	 */
	public void getList() {
		if (DeviceUtil.checkNet()) {
			HttpClientUtil.post("apps/oa/getFWDetail?" + id, null, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					faWenD = OAManager.getIntence().getOAFWInfos(responseInfo);
					mHandler.sendEmptyMessage(3);
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					mHandler.sendEmptyMessage(3);
				}
			});
			
			
			
		} 
	}

	/**
	 * 加载控件
	 */
	private void findUi() {
		mError = (ErrorLayout) findViewById(R.id.error_layout);
		fwFileType = (TextView) findViewById(R.id.file_type_txt);
		fwJGwz = (TextView) findViewById(R.id.jg_wz_txt);
		fwNum = (TextView) findViewById(R.id.fw_num_txt);
		jinjCD = (TextView) findViewById(R.id.jinj_cd_txt);
		zhengWen = (TextView) findViewById(R.id.zheng_wen_txt);
		zhengWenEmpty = (TextView) findViewById(R.id.zheng_wen_no_txt);
		zhengWen.setOnClickListener(this);
		ngr_txt = (TextView) findViewById(R.id.ngr_txt);
	}

	/**
	 * 填充控件
	 */
	private void fillUi(FaWenDoc doc) {

		if (StringUtils.isNotEmpty(doc.getFwFileType())) {

			fwFileType.setText(doc.getFwFileType());
		} else {

			fwFileType.setText("无");
		}
		if (StringUtils.isNotEmpty(doc.getFwJGwz())) {

			fwJGwz.setText(doc.getFwJGwz());
		} else {

			fwJGwz.setText("无");
		}
		if (StringUtils.isNotEmpty(doc.getZhengwen())) {

			zhengWen.setText(doc.getZhengwen());
		} else {

			zhengWen.setText("无");
		}

		if (StringUtils.isNotEmpty(doc.getFwNum())) {

			fwNum.setText(doc.getFwNum());
		} else {

			fwNum.setText("无");
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
		
		if (StringUtils.isNotEmpty(doc.getZhengwen())) {
			zhengWenEmpty.setVisibility(View.GONE);
			zhengWen.setVisibility(View.VISIBLE);
			zhengWen.setText("查看详情");
		} else {
			zhengWenEmpty.setVisibility(View.VISIBLE);
			zhengWen.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.zheng_wen_txt) {
			if (isFileExits(bzid, faWenD.getTitle()+".docx")) {
				startActivity(FileUtil.openFile(CoreConstants.DOWNLOADPATH + fileName(bzid, faWenD.getTitle()+".docx")));
			} else {
				if (DeviceUtil.checkNet()) {
					try {
//						showProgressDialog();
						mError.setErrorType(ErrorLayout.LOADDATA);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									FileUtil.downFile(faWenD.getZhengwen(), fileName(bzid, faWenD.getTitle()+".docx"), mHandler);
								} catch (IOException e) {
									e.printStackTrace();
//									hideProgressDialog();
									mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
								}
							}
						}).start();
					} catch (Exception e) {
						e.printStackTrace();
//						hideProgressDialog();
						mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
					}
				}
			}
		}

	}

	/**
	 * 判断本地是否存在下载文件
	 * 
	 * @return
	 */
	public boolean isFileExits(String id, String title) {
		boolean isE = false;
		if (!new File(CoreConstants.DOWNLOADPATH + fileName(id, title)).exists()) {
			isE = false;
		} else {
			isE = true;
		}
		return isE;
	}

	/**
	 * 过滤文件名字
	 * @param id
	 * @return
	 */
	public String fileName(String id, String title) {
		String fileN;
		fileN = id + title;
		return fileN;
	}
}
