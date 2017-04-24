package com.dk.mp.oldoa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.Doc;
import com.dk.mp.oldoa.entity.UiEntity;
import com.dk.mp.oldoa.http.HttpUtil;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.CoreConstants;
import com.dk.mp.oldoa.utils.FileUtil;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


@SuppressLint("ResourceAsColor")
public class DetailActivity extends MyActivity implements EasyPermissions.PermissionCallbacks{
	private static final int WRITE_RERD = 1;
	private List<UiEntity> listUpdateUi = new ArrayList<UiEntity>();// 存储附件中
	private String message;
	private List<View> viewLists = new ArrayList<View>();
	private int updateIndex = 0;
	private String interfaces = "";
	private String code = "0";// 0代表流程，1代表下一步，2代表打回，3代表结束
	private String url;
	private String opinions;
	private String result;// 处理的结果状态
	private String flowend;// 处理的结果状态dealState
	private boolean needInput;
	private EditText opinionsEdit;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUiSuccess(listUpdateUi, updateIndex);
				break;
			case 1:
				updateUiFaile(listUpdateUi, updateIndex);
				showErrorMsg("下载失败");
				break;
			default:
				break;
			}

		};

	};
	// 定义一个Handler，用来异步处理数据
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				hideProgressDialog();
				Bundle b = msg.getData();
				int codes = Integer.parseInt(b.getString("code"));
				if (codes != 1) {
					showErrorMsg(message);
				}
				switch (codes) {
				case 1:
					Intent intent = new Intent(DetailActivity.this,
							NextStepActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("opinions", opinions);
					startActivity(intent);
					// finish();
					break;
				case 2:
					if ("操作成功".equals(message) || "流程已经结束".equals(message)
							|| "数据已经失效".equals(message)) {

						Intent intentMain = new Intent(DetailActivity.this,
								MainActivity.class);
						intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intentMain);
						finish();
					}

					break;
				case 3:
					Intent intentMain = new Intent(DetailActivity.this,
							MainActivity.class);
					intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentMain);
					finish();

				default:
					break;
				}

				break;
			default:
				break;
			}
		};
	};

	/**
	 * 加载控件以及填充顶部数据
	 */
	private void fillTopData(Doc doc) {
		ImageView detailTopIcon = (ImageView) findViewById(R.id.img);// 详情图标
		TextView detailTopTitle = (TextView) findViewById(R.id.titles);// 详情标题
		TextView detailTopDepartment = (TextView) findViewById(R.id.bumen);// 部门
		TextView detailToptxt = (TextView) findViewById(R.id.shijian_title);// 部门
		TextView detailTopPublishTime = (TextView) findViewById(R.id.shijian);// 发布时间

		if ("OA_SW".equals(doc.getType())) {
			detailTopIcon.setImageResource(R.mipmap.shouwen);
			if (StringUtils.isNotEmpty(doc.getTime())) {
				detailToptxt.setText("收文日期:");
			} else {
				detailToptxt.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(doc.getDepartment()))
				detailTopDepartment.setText("拟稿部门:"+doc.getDepartment());
			else
				detailTopDepartment.setVisibility(View.GONE);

		} else if ("OA_FW".equals(doc.getType())) {
			detailTopIcon.setImageResource(R.mipmap.fawen);
			if (StringUtils.isNotEmpty(doc.getTime())) {
				detailToptxt.setText("拟稿日期:");
			} else {
				detailToptxt.setVisibility(View.GONE);
			}
			if(StringUtils.isNotEmpty(doc.getDepartment()))
				detailTopDepartment.setText("拟稿部门:"+doc.getDepartment());
			else
				detailTopDepartment.setVisibility(View.GONE);
		} else {
			detailTopIcon.setImageResource(R.mipmap.baogao);
			detailTopPublishTime.setVisibility(View.GONE);
			detailToptxt.setVisibility(View.GONE);
			if(StringUtils.isNotEmpty(doc.getDepartment()))
				detailTopDepartment.setText("请示单位:"+doc.getDepartment());
			else
				detailTopDepartment.setVisibility(View.GONE);
			
		}

		detailTopTitle.setText(doc.getTitle());
		
		
		
		if (StringUtils.isNotEmpty(doc.getTime())) {

			detailTopPublishTime.setText(doc.getTime());
		} else {
			detailTopPublishTime.setVisibility(View.GONE);
		}
	}

	/**
	 * 加载公共布局
	 */
	public void fillPublicData(Doc doc, Context mContext) {
		setTitle("公文详情");
		try {
			Button back = (Button) findViewById(R.id.back);
//			type = getIntent().getIntExtra(ANIM, AnimUtil.ANIM_RIGHT_TO_LEFT);
			back.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
//					Logger.info("back:" + type);
					back();
//					startAnim(type);
				}
			});
		} catch (Exception e) {
		}
		if (doc != null) {
			fillTopData(doc);
			fillCenterAttachment(doc, mContext);
			fillComment(doc, mContext);
		}
	}

	/**
	 * 加载评论布局
	 */
	private void fillComment(final Doc doc, final Context mContext) {
		LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.next);
		opinionsEdit = (EditText) findViewById(R.id.opinions_edit);
		Button nextBtn = (Button) findViewById(R.id.next_btn);
		Button flowendBut = (Button) findViewById(R.id.flowend);

		if (StringUtils.isNotEmpty(doc.getNext())) {
			result = doc.getNext();
			flowend = doc.getFlowend();
			Logger.info(result + flowend
					+ "???????????????????????????????????????????????????");
			String retrieveNumber = doc.getRetrieveNumber();
			if (!"true".equals(retrieveNumber)) {
				if (StringUtils.isNotEmpty(doc.getNeedOptiona())) {
					needInput = Boolean.parseBoolean(doc.getNeedOptiona());
				}
				if ("1".equals(doc.getDocumentState())) {
					bottomLayout.setVisibility(View.GONE);
				} else if ("0".equals(doc.getDocumentState())) {
					url = doc.getUrl();
					if (result.equals("true")) {// 有下一步按钮;
						nextBtn.setText("下一步");
						code = "1";
						nextBtn.setVisibility(View.VISIBLE);
					} else if (result.equals("submit")) {// 提交
						nextBtn.setText("提交");
						interfaces = "apps/oa/flowNextStep?";
						code = "3";
						nextBtn.setVisibility(View.VISIBLE);
					} else {
						nextBtn.setVisibility(View.GONE);
					}

					if ("true".equals(flowend)) {// 办结按钮;
						flowendBut.setText("办结");
						interfaces = "apps/oa/flowEnd?";
						flowendBut.setVisibility(View.VISIBLE);
					} else {
						flowendBut.setVisibility(View.GONE);
					}

					bottomLayout.setVisibility(View.VISIBLE);
					opinionsEdit.setVisibility(View.VISIBLE);
					nextBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String opinionsTxt = opinionsEdit.getText()
									.toString().trim();
							opinions = opinionsTxt;
							if (needInput
									&& !StringUtils.isNotEmpty(opinionsTxt)) {
								Toast.makeText(mContext, "请输入您的意见",
										Toast.LENGTH_SHORT).show();
								return;
							}
							if ("1".equals(code)) {
								Message msg = new Message();
								Bundle bundle = new Bundle();
								msg.what = 1;
								bundle.putString("code", code);
								msg.setData(bundle);
								handler.sendMessage(msg);
							} else if ("3".equals(code)) {
								if(DeviceUtil.checkNet()){
									UploadCommit(url);
								}
							}

						}
					});

					flowendBut.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String opinionsTxt = opinionsEdit.getText()
									.toString().trim();
							opinions = opinionsTxt;
							if (needInput
									&& !StringUtils.isNotEmpty(opinionsTxt)) {
								Toast.makeText(mContext, "请输入您的意见",
										Toast.LENGTH_SHORT).show();
								return;
							}
							if(DeviceUtil.checkNet()){
								sendOpinions(mContext, opinionsTxt, doc,
									interfaces, "2");
							}
						}
					});
				}
			} else {
				showErrorMsg("本条公文暂没有在手机端操作权限");
				bottomLayout.setVisibility(View.GONE);
			}
		} else {

			bottomLayout.setVisibility(View.GONE);
			// showMessage("本条公文暂没有在手机端操作权限");

		}

	}

	/**
	 * 发表意见
	 */
	private void sendOpinions(final Context mContext, String opinionsTxt,
			final Doc doc, final String interfaces, final String code) {
//		showProgressDialog();
		Map<String,String> map = doc.getMap();
		map.put("suggestion", opinionsTxt);
		Logger.info("-----------map--------" + map);
		HttpClientUtil.post(interfaces, map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				message = OAManager.getIntence().sendOpinions(arg0);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.what = 1;
				bundle.putString("code", code);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				hideProgressDialog();
				showErrorMsg(getReString(R.string.data_fail));
			}
		});

	}

	private void UploadCommit(final String childurl) {
		Logger.info(childurl
				+ "+++++++++++++++++++++++++++++++++++++++++++++++++");
//		showProgressDialog();
		Map<String, String> map = OAManager.getIntence().getMap(childurl);
		map.put("userIdString", "");
		map.put("suggestion", opinionsEdit.getText().toString());
		
		HttpClientUtil.post("apps/oa/flowNextStep", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				message = HttpUtil.UploadCommit(responseInfo);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				msg.what = 1;
				bundle.putString("code", code);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				hideProgressDialog();
				showErrorMsg("操作失败");
			}
		});
		
	}

	/**
	 * 附件
	 * 
	 * @param doc
	 * @param mContext
	 */
	private void fillCenterAttachment(Doc doc, final Context mContext) {
		// 附件布局
		viewLists.clear();
		LinearLayout layoutAttachment = (LinearLayout) findViewById(R.id.layout_attachment);
		LinearLayout attachment = (LinearLayout) findViewById(R.id.attachment_layouts);
		if (StringUtils.isNotEmpty(doc.getFujian() + "")) {
			if (doc.getFujian().size() > 0) {

				attachment.setVisibility(View.VISIBLE);
			} else {
				attachment.setVisibility(View.GONE);

			}

			for (int i = 0; i < doc.getFujian().size(); i++) {
				View attachmentView = LayoutInflater.from(mContext).inflate(
						R.layout.oa_detail_fujian, null);

				RelativeLayout downLoadLayout = (RelativeLayout) attachmentView
						.findViewById(R.id.download_file_layout);// 附件标题
				final int index = i;
				TextView attachmentTitle = (TextView) attachmentView
						.findViewById(R.id.attachment_title);// 附件标题
				// TextView attachmenDWstate = (TextView)
				// attachmentView.findViewById(R.id.attachment_down_load_state);//
				// 附件下载状态
				final ImageView attachmenImage = (ImageView) attachmentView
						.findViewById(R.id.attachment_img);// 附件下载按钮
				final ProgressBar attachmenProgress = (ProgressBar) attachmentView
						.findViewById(R.id.attachment_progress);// 附件下载圆形进度
				final String fileUrl = doc.getFujian().get(i).getUrl();
				final String fileTitle = doc.getFujian().get(i).getTitle();
				final String fileId = doc.getFujian().get(i).getId();
				attachmentTitle.setText(doc.getFujian().get(i).getTitle());
				attachmenImage.setTag(fileUrl);
				UiEntity ui = new UiEntity();
				ui.setProgressB(attachmenProgress);
				// ui.setTextV(attachmenDWstate);
				ui.setImageView(attachmenImage);
				listUpdateUi.add(ui);
				if (isFileExits(doc.getFujian().get(i).getId(), fileTitle)) {
					attachmenProgress.setVisibility(View.GONE);
					attachmenImage.setVisibility(View.INVISIBLE);	// attachmenDWstate.setText("已下载");


				} else {
					// attachmenDWstate.setText("未下载");
					// attachmenDWstate.setTextColor(R.color.attachmen_txt_color);
					attachmenImage.setVisibility(View.VISIBLE);

				}
				downLoadLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startReadWi(fileId,fileTitle,attachmenProgress,attachmenImage,index,fileUrl);
					}
				});
				layoutAttachment.addView(attachmentView);
			}
		}
		// 当前状态布局
		LinearLayout layoutCurrent = (LinearLayout) findViewById(R.id.current_state_layout);
		TextView currentBZ = (TextView) findViewById(R.id.current_bz);
		TextView dealState = (TextView) findViewById(R.id.deal_state);
		TextView alreadDeal = (TextView) findViewById(R.id.alread_deal_txt);
		// if ("0".equals(doc.getDocumentState())) {// 0是待处理。1是已处理
		currentBZ.setVisibility(View.VISIBLE);
		alreadDeal.setVisibility(View.VISIBLE);
		dealState.setVisibility(View.VISIBLE);
		Logger.info("当前步骤=============" + doc.getDqbz());
		if (StringUtils.isNotEmpty(doc.getDqbz())
				|| StringUtils.isNotEmpty(doc.getDclry())
				|| StringUtils.isNotEmpty(doc.getYclry())) {

			layoutCurrent.setVisibility(View.VISIBLE);
		} else {

			layoutCurrent.setVisibility(View.GONE);
		}
		if (StringUtils.isNotEmpty(doc.getDqbz())) {

			currentBZ.setText(doc.getDqbz());

		}
		if (StringUtils.isNotEmpty(doc.getDclry())) {

			dealState.setText(doc.getDclry());

		}

		if (StringUtils.isNotEmpty(doc.getYclry())) {

			alreadDeal.setText(doc.getYclry());

		}

		LinearLayout layoutOpinions = (LinearLayout) findViewById(R.id.layout_opinions);
		LinearLayout Opinions = (LinearLayout) findViewById(R.id.poinions_layout);
		if (doc.getBoss() != null
				&& doc.getOpinions()!=null) {

			if (doc.getBoss().size() > 0 || doc.getOpinions().size() > 0) {
				Opinions.setVisibility(View.VISIBLE);

			} else {
				Opinions.setVisibility(View.GONE);

			}
			// 领导意见布局
			if (doc.getBoss().size() > 0) {
				for (int i = 0; i < doc.getBoss().size(); i++) {
					View opiniosView = LayoutInflater.from(mContext).inflate(
							R.layout.oa_detail_yijian, null);
					TextView opinionsName = (TextView) opiniosView
							.findViewById(R.id.yijian_name);// 意见人名字
					TextView opinionsTime = (TextView) opiniosView
							.findViewById(R.id.yijian_time);// 意见时间
					TextView opinionsContent = (TextView) opiniosView
							.findViewById(R.id.yijian_content);// 意见内容
					opinionsName.setText(doc.getBoss().get(i).getName());
					opinionsName.setTag(doc.getBoss().get(i).getName());
					opinionsTime.setText(doc.getBoss().get(i).getTime());
					opinionsTime.setTag(doc.getBoss().get(i).getTime());
					opinionsContent.setText(Html.fromHtml(doc.getBoss().get(i)
							.getContent()));
					opinionsContent.setTag(doc.getBoss().get(i).getContent());
					layoutOpinions.addView(opiniosView);
					layoutOpinions.setBackgroundColor(getResources().getColor(
							R.color.master_bg_color));
				}

			}

			// 意见布局
			if (doc.getOpinions().size() > 0) {
				for (int i = 0; i < doc.getOpinions().size(); i++) {
					View opiniosView = LayoutInflater.from(mContext).inflate(
							R.layout.oa_detail_yijian, null);
					TextView opinionsName = (TextView) opiniosView
							.findViewById(R.id.yijian_name);// 意见人名字
					TextView opinionsTime = (TextView) opiniosView
							.findViewById(R.id.yijian_time);// 意见时间
					TextView opinionsContent = (TextView) opiniosView
							.findViewById(R.id.yijian_content);// 意见内容
					opinionsName.setText(doc.getOpinions().get(i).getName());
					opinionsName.setTag(doc.getOpinions().get(i).getName());
					opinionsTime.setText(doc.getOpinions().get(i).getTime());
					opinionsTime.setTag(doc.getOpinions().get(i).getTime());
					opinionsContent.setText(Html.fromHtml(doc.getOpinions()
							.get(i).getContent()));
					opinionsContent.setTag(doc.getOpinions().get(i)
							.getContent());
					layoutOpinions.addView(opiniosView);
					layoutOpinions.setBackgroundColor(getResources().getColor(
							R.color.teacher_bg_color));
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
	 * 更新控件
	 * 
	 * @param uiList
	 */
	private void updateUiSuccess(final List<UiEntity> uiList,
			final int updateIndex) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// uiList.get(updateIndex).getTextV().setText("已下载");
				uiList.get(updateIndex).getProgressB().setVisibility(View.GONE);
				uiList.get(updateIndex).getImageView()
						.setVisibility(View.INVISIBLE);
			}
		});
	}

	/**
	 * 下载失败
	 * 
	 * @param uiList
	 * @param updateIndex
	 */
	private void updateUiFaile(final List<UiEntity> uiList,
			final int updateIndex) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// uiList.get(updateIndex).getTextV().setText("未下载");
				uiList.get(updateIndex).getProgressB().setVisibility(View.GONE);
				uiList.get(updateIndex).getImageView()
						.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 过滤文件名字
	 * 
	 * @param id
	 * @return
	 */
	public String fileName(String id, String title) {
		String fileN;
		int index = title.lastIndexOf(".");
		fileN = id + title.substring(index);
		return fileN;
	}

	@Override
	protected int getLayoutID() {
		return 0;
	}

	/**
	 * 公共手机返回按钮事件.
	 * @param keyCode keyCode
	 * @param event  KeyEvent
	 * @return  boolean
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		showErrorMsg("请正确授权");
	}

	@AfterPermissionGranted(WRITE_RERD)
	public void startReadWi(final String fileId, final String fileTitle, ProgressBar attachmenProgress, ImageView attachmenImage, final int index, final String fileUrl){
		String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
		if (EasyPermissions.hasPermissions(mContext, perms)) {
			try {
				downloadFile(fileId,fileTitle,attachmenProgress,attachmenImage,index,fileUrl);
			} catch (Exception e) {
				showErrorMsg("请正确授权");
			}
		} else {
			EasyPermissions.requestPermissions(this,
					"请求读写权限",
					WRITE_RERD, perms);
		}
	}

	public void downloadFile(final String fileId, final String fileTitle, ProgressBar attachmenProgress, ImageView attachmenImage, final int index, final String fileUrl){
		if (isFileExits(fileId, fileTitle)) {
			startActivity(FileUtil
					.openFile(CoreConstants.DOWNLOADPATH
							+ fileName(fileId, fileTitle)));
		} else {
			if (DeviceUtil.checkNet()) {
				attachmenProgress.setVisibility(View.VISIBLE);
				attachmenImage.setVisibility(View.INVISIBLE);
				try {

					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								updateIndex = index;
								FileUtil.downFile(
										fileUrl,
										fileName(fileId,
												fileTitle),
										mHandler);
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
