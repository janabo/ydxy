package com.dk.mp.oldoa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.adapter.GridViewSelectedAdapter;
import com.dk.mp.oldoa.entity.Department;
import com.dk.mp.oldoa.entity.Operate;
import com.dk.mp.oldoa.entity.Person;
import com.dk.mp.oldoa.entity.RadioButtonEntity;
import com.dk.mp.oldoa.http.HttpUtil;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.dk.mp.oldoa.widget.ScrollGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 
 * @version 2014-8-5
 * @author wangwei
 */
public class NextStepActivity extends MyActivity {
	private GridView gd_selected;
	private Context context = NextStepActivity.this;
	private ListViewCanSelectAdapter ladapter;
	private GridViewSelectedAdapter sAdapter;
	private GridViewCanSelectAdapter csAdapter;
	private LinearLayout rg_operate;
	private Button confirm_commit;
	private ListView listview;
	//	private Map<String, Person> map = new HashMap<String, Person>();
	private List<Department> listcs;
	private List<Person> lists = new ArrayList<Person>();
	private List<RadioButtonEntity> list = new ArrayList<RadioButtonEntity>();
	private List<Operate> operatelist;
	private Map<String, String> parentIdMap = new HashMap<String, String>();
	private String url;
	private String childurl;
	private String message;
	private String suggestion;

    private ErrorLayout errorLayout;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				ladapter = new ListViewCanSelectAdapter(listcs, context, sAdapter);
				listview.setAdapter(ladapter);
				setListViewHeightTo(listview);
				gd_selected.setAdapter(sAdapter);
				lists.clear();
				sAdapter.notifyDataSetChanged();
				ladapter.notifyDataSetChanged();
				break;

			case 2:
				showMessage(message);
				if ("操作成功".equals(message) || "流程已经结束".equals(message) || "数据已经失效".equals(message)) {
					Intent intent = new Intent(NextStepActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
				break;

			case 3:
				showMessage("没有选择提交人员");
				break;

			case 4:
				ladapter = new ListViewCanSelectAdapter(listcs, context, sAdapter);
				listview.setAdapter(ladapter);
				setListViewHeightTo(listview);
				gd_selected.setAdapter(sAdapter);
				lists.clear();
				sAdapter.notifyDataSetChanged();
				ladapter.notifyDataSetChanged();
				break;

			case 5:
				ladapter = new ListViewCanSelectAdapter(listcs, context, sAdapter);
				listview.setAdapter(ladapter);
				setListViewHeightTo(listview);
				gd_selected.setAdapter(sAdapter);
				lists.clear();
				sAdapter.notifyDataSetChanged();
				ladapter.notifyDataSetChanged();
				break;

			case 6:

				for (int i = 0; i < operatelist.size(); i++) {
					LinearLayout ll = (LinearLayout) LayoutInflater.from(context)
							.inflate(R.layout.oa_radiobutton, null);
					Button btn = (Button) ll.findViewById(R.id.operate_btn);

					btn.setText(operatelist.get(i).getName());
					btn.setTag(operatelist.get(i));
					RadioButtonEntity rbe = new RadioButtonEntity();
					rbe.setRb(btn);
					list.add(rbe);

					final int index = i;

					if (i == 0) {
						childurl = operatelist.get(0).getUrl();
						Logger.info(childurl + "))))))))))))))))))))))))))))))))))))))))))");
					}

					btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Operate op = (Operate) v.getTag();
							RadioButtonChange(index);
							childurl = op.getUrl();
							if (childurl != null) {
								if(DeviceUtil.checkNet()){
									getData(childurl);
								}
							}
						}
					});
					rg_operate.addView(ll);
				}

				if (operatelist.size() > 0) {
					RadioButtonChange(0);
					if(DeviceUtil.checkNet()){
						getData(operatelist.get(0).getUrl());
					}
				}
				break;

			default:
				break;
			}
//			hideProgressDialog();
			errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
		}

	};

	public void RadioButtonChange(int index) {
		for (int i = 0; i < list.size(); i++) {
			if (i == index) {
				list.get(i).getRb().setBackgroundResource(R.mipmap.operate_press);
				list.get(i).getRb().setTextColor(getResources().getColor(R.color.white));
			} else {
				list.get(i).getRb().setBackgroundResource(R.mipmap.operate_normal);
				list.get(i).getRb().setTextColor(getResources().getColor(R.color.blue));
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_nextstep);
		setTitle("下一步");
		url = getIntent().getStringExtra("url");
		suggestion = getIntent().getStringExtra("opinions");

		init();
		Logger.info(url);
		if(DeviceUtil.checkNet()){
			getOperate();
		}

	}

	private void init() {
        errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

        listview = (ListView) this.findViewById(R.id.person_can_select_listview);
		listview.setDividerHeight(1);
		listview.setDivider(getResources().getDrawable(R.color.transparent));
		rg_operate = (LinearLayout) this.findViewById(R.id.rg_operate);
		confirm_commit = (Button) this.findViewById(R.id.confirm_commit);
		gd_selected = (GridView) this.findViewById(R.id.gridview_selected_person);
		sAdapter = new GridViewSelectedAdapter(context, lists);
		gd_selected.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gd_selected.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
				Person personE = (Person) adapter.getItemAtPosition(position);
				deletePerson(personE, position);
			}
		});

		confirm_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadCommit();
			}
		});

	}

	public void getOperate() {
//		showProgressDialog();
        errorLayout.setErrorType(ErrorLayout.LOADDATA);
		Map<String, String> map = OAManager.getIntence().getMap(url);
		HttpClientUtil.post("apps/oa/queryOperations", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				operatelist = HttpUtil.getOperate(arg0);
				handler.sendEmptyMessage(6);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				hideProgressDialog();
                errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				showMessage(R.string.data_fail);
			}
		});
	}

	/**
	 * 删除
	 * @param position
	 */
	private void deletePerson(Person per, int position) {
		parentIdMap.remove(listcs.get(per.getParentPosition()).getParentId());
		listcs.get(per.getParentPosition()).getList().get(per.getCsIndex()).setCheck(false);
		lists.remove(per);
		csAdapter.notifyDataSetChanged();
		sAdapter.notifyDataSetChanged();
		ladapter.notifyDataSetChanged();

	}

	/**
	 * 添加/1单选0多选
	 */
	private void addPerson(Person per, int position) {

		if (!per.isMoreSelect()) {
			if (per.isCheck()) {
				lists.remove(per);
				listcs.get(per.getParentPosition()).getList().get(per.getCsIndex()).setCheck(false);
				parentIdMap.remove(listcs.get(per.getParentPosition()).getParentId());
			} else {
				if (lists.size() == 0) {
					per.setCsIndex(position);
					lists.add(per);
					lists.get(lists.size() - 1).setCheck(true);
					listcs.get(per.getParentPosition()).getList().get(position).setCheck(true);
					parentIdMap.put(listcs.get(per.getParentPosition()).getParentId(),
							listcs.get(per.getParentPosition()).getParentId());
				} else {

					if (!singleSelect(parentIdMap, listcs.get(per.getParentPosition()).getParentId())) {
						per.setCsIndex(position);
						lists.add(per);
						lists.get(lists.size() - 1).setCheck(true);
						listcs.get(per.getParentPosition()).getList().get(position).setCheck(true);
						parentIdMap.put(listcs.get(per.getParentPosition()).getParentId(),
								listcs.get(per.getParentPosition()).getParentId());
					}
				}

			}

		} else {
			if (per.isCheck()) {
				lists.remove(per);
				listcs.get(per.getParentPosition()).getList().get(per.getCsIndex()).setCheck(false);

			} else {
				per.setCsIndex(position);
				lists.add(per);
				lists.get(lists.size() - 1).setCheck(true);
				listcs.get(per.getParentPosition()).getList().get(position).setCheck(true);
			}

		}

		sAdapter.notifyDataSetChanged();
		csAdapter.notifyDataSetChanged();
		ladapter.notifyDataSetChanged();
	}

	/**
	 * 单选
	 */
	private boolean singleSelect(Map<String, String> map, String parentId) {
		boolean isSingle = false;

		Logger.error("---parentId---" + parentId);
		if (map.containsKey(parentId)) {
			Logger.error("---containsKey---" + parentId);
			isSingle = true;
		}
		return isSingle;

	}

	/**
	 * 获取人员列表的数据
	 */
	private void getData(final String childurl) {
		Logger.info(childurl + "+++++++++++++++++++++++++");
//		showProgressDialog();
        errorLayout.setErrorType(ErrorLayout.LOADDATA);
		Map<String, String> map = OAManager.getIntence().getMap(childurl);
		HttpClientUtil.post("apps/oa/queryNodeUsers", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				listcs = HttpUtil.getPersonCanSelect(arg0);
				handler.sendEmptyMessage(1);
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				hideProgressDialog();
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				showMessage(R.string.data_fail);
			}
		});
	}

	/**
	 * 上传提交的内容
	 */
	private void UploadCommit() {
//		showProgressDialog();
        errorLayout.setErrorType(ErrorLayout.LOADDATA);
		if (lists.size() > 0) {
			StringBuffer userIdString = new StringBuffer();
			for (int i = 0; i < lists.size(); i++) {
				if (i == 0) {
					userIdString.append(lists.get(i).getId());
				} else {
					userIdString.append(",").append(lists.get(i).getId());
				}
			}
			String userId = userIdString.toString();
			Map<String, String> map = OAManager.getIntence().getMap(childurl);
			map.put("userIdString", userId);
			map.put("suggestion", suggestion);
			
			Logger.info("*******************************************");
			for (String key : map.keySet()) {
				Logger.info(key + ":" + map.get(key));
			}
			Logger.info("************************************");
			
			
			HttpClientUtil.post("apps/oa/flowNextStep", map, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					message = HttpUtil.UploadCommit(arg0);
					handler.sendEmptyMessage(2);
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
//					hideProgressDialog();
					errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
					showMessage(R.string.data_fail);
				}
			});
		}else{
//			hideProgressDialog();
			errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			handler.sendEmptyMessage(3);
		}
	}

	/**
	 * 动态计算listview的高度（注意listview的item布局必须为线性布局）
	 * @param listView
	 */
	public void setListViewHeightTo(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 可选列表adapter
	 * @since 
	 * @version 2014-7-11
	 * @author lj.zhang
	 */
	public class ListViewCanSelectAdapter extends BaseAdapter {

		Context context;
		private LayoutInflater inflater;
		private List<Department> listcs;
		private GridViewSelectedAdapter sAdapter;

		public ListViewCanSelectAdapter(List<Department> listcs, Context context, GridViewSelectedAdapter sAdapter) {
			this.listcs = listcs;
			this.sAdapter = sAdapter;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return listcs.size();
		}

		@Override
		public Object getItem(int position) {
			return listcs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.oa_person_can_select_listview_item, null);
				holder.txv_title = (TextView) convertView.findViewById(R.id.title_can_select);
				holder.sgv = (ScrollGridView) convertView.findViewById(R.id.gridview_can_select);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			csAdapter = new GridViewCanSelectAdapter(context, listcs.get(position).getList(), position);
			holder.sgv.setAdapter(csAdapter);
			holder.sgv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View arg1, int arg2, long arg3) {
					Person personE = (Person) adapter.getItemAtPosition(arg2);
					addPerson(personE, arg2);
				}
			});
			holder.txv_title.setText(listcs.get(position).getName());
			return convertView;
		}

		private class Holder {
			private TextView txv_title;
			private ScrollGridView sgv;
		}

	}

	/**
	 * 已选列表adapter
	 * @since 
	 * @version 2014-7-11
	 * @author lj.zhang
	 */
	public class GridViewCanSelectAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		private List<Person> list;
		int ParentPosition;

		public GridViewCanSelectAdapter(Context context, List<Person> list, int PPosition) {
			this.context = context;
			this.list = list;
			this.ParentPosition = PPosition;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Person getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				holder = new Holder();
				inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.oa_gridview_person_can_select, null);
				holder.txv = (TextView) convertView.findViewById(R.id.txv_person_can_select);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			String name = list.get(position).getName();
			if (list.get(position).isCheck()) {
				holder.txv.setBackgroundResource(R.color.blue);
				holder.txv.setTextColor(context.getResources().getColor(R.color.white));
			} else {
				holder.txv.setBackgroundResource(R.color.white);
				holder.txv.setTextColor(context.getResources().getColor(R.color.gray));
			}
			holder.txv.setText(name);
			holder.txv.setTag(ParentPosition);
			return convertView;
		}

		private class Holder {
			private TextView txv;
		}

	}

}
