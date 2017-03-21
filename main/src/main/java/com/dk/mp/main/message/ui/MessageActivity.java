package com.dk.mp.main.message.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.lsgl.entity.PersonEntity;
import com.dk.mp.main.R;
import com.dk.mp.main.message.entity.Message;
import com.dk.mp.newoa.entity.NewDoc;
import com.dk.mp.newoa.entity.SeriMap;
import com.dk.mp.newoa.ui.OADetailActivity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dk.mp.core.application.MyApplication.getContext;

;

/**
 * @since 
 * @version 2013-2-18
 * @author wwang
 */
public class MessageActivity extends MyActivity {
	
	public Context mContext;
	public MyListView mListView;
	public List<Message> mList = new ArrayList<Message>();
	private CoreSharedPreferencesHelper helper;
	private Message brithdayMess = null;

	@Override
	protected int getLayoutID() {
		return R.layout.mp_message_main;
	}

	@Override
	protected void initialize() {
		super.initialize();
		setTitle("消息");
		initViews();
	}

	private void initViews(){
		mContext = MessageActivity.this;
		mListView = (MyListView) findViewById(R.id.listView);
		helper = new CoreSharedPreferencesHelper(this);
		mListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		mListView.setAdapterInterface(mList,adapterInterface);

	}

	private AdapterInterface adapterInterface = new AdapterInterface(){
		@Override
		public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(getContext()).inflate(R.layout.mp_message_item, parent, false);
			return new MyView(view);
		}
		@Override
		public void setItemValue(RecyclerView.ViewHolder holder, final int position) {
			final Message examInfo = mList.get(position);
			((MyView)holder).time.setText(examInfo.getTime());
			//创建一个 SpannableString对象
			SpannableString sp1 = new SpannableString(examInfo.getTitle());
			sp1.setSpan(new ForegroundColorSpan(
							Color.rgb(33, 33, 33)),
					examInfo.getTitle().indexOf("：")+1,
					examInfo.getTitle().length(),
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			((MyView)holder).title.setText(sp1);
			//创建一个 SpannableString对象
			SpannableString sp = new SpannableString(examInfo.getContent());
			sp.setSpan(new ForegroundColorSpan(
							Color.rgb(33, 33, 33)),
					examInfo.getContent().indexOf("：")+1,
					examInfo.getContent().length(),
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			((MyView)holder).content.setText(sp);

			((MyView)holder).type.setText(examInfo.getMoudel());
			//标题颜色
			if(examInfo.getAction().equals("fw")){
				((MyView)holder).type.setBackgroundColor(Color.rgb(255, 41, 42));
			}else if(examInfo.getAction().equals("sw")){
				((MyView)holder).type.setBackgroundColor(Color.rgb(250, 184, 31));
			}else if(examInfo.getAction().equals("qsbg")){
				((MyView)holder).type.setBackgroundColor(Color.rgb(0, 188, 184));
			}else if(examInfo.getAction().equals("brithday")){
				((MyView)holder).type.setBackgroundColor(Color.rgb(55,199,190));
			}else{
				((MyView)holder).type.setBackgroundColor(Color.rgb(0, 170, 235));
			}
			((MyView)holder).getInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String par = examInfo.getParam();
					if(par.startsWith("{")){
						par = par.substring(1, par.length()-1);
					}
					String[] param = par.split(",");
					Map<String, String> m = new HashMap<String, String>();
					for(String p:param){
						m.put(p.split(":")[0].replace("\"", ""), p.split(":")[1].replace("\"", ""));
					}
					Intent intent;
					if(examInfo.getAction().equals("fw") || examInfo.getAction().equals("sw") || examInfo.getAction().equals("qsbg")){
						intent = new Intent(mContext, OADetailActivity.class);
						NewDoc doc = new NewDoc();
						doc.setType(examInfo.getMoudel());
						SeriMap map = new SeriMap();
						map.setMap(m);
						Bundle bundle = new Bundle();
						bundle.putParcelable("doc", doc);
						bundle.putSerializable("map", map);
						intent.putExtras(bundle);
					} else if (examInfo.getAction().equals("brithday")) {
						intent = new Intent(mContext, BrithdayMessageActivity.class);
					}else{
						intent = null;
//						Malfunction malfunction = new Malfunction();
//						malfunction.setName(m.get("userName"));
//						malfunction.setTitle(m.get("userName"));
//						malfunction.setStatusname(m.get("zt"));
//						malfunction.setAddress(m.get("dd"));
//						malfunction.setDevice(m.get("sb"));
//						malfunction.setDes(m.get("wtms"));
//						malfunction.setId(m.get("id"));
//						Bundle bundle = new Bundle();
//						bundle.putSerializable("malfunction", malfunction);
//						bundle.putString("type", "wait");
//						intent = new Intent(mContext, PushProcessActivity.class);
//						intent.putExtras(bundle);
//						intent.putExtra("item", getGson().toJson(mList.get(position)));
					}
					mContext.startActivity(intent);
				}
			});
		}
		@Override
		public void loadDatas() {
			initDatas();
		}
	};

	private class MyView extends RecyclerView.ViewHolder{
		private TextView time;
		private TextView title;
		private TextView content;
		private TextView type;
		private LinearLayout getInfo;
		public MyView(View itemView) {
			super(itemView);
			time = (TextView) itemView.findViewById(R.id.time);// 取得实例
			title = (TextView) itemView.findViewById(R.id.title);// 取得实例
			content = (TextView) itemView.findViewById(R.id.content);// 取得实例
			type = (TextView) itemView.findViewById(R.id.type);// 取得实例
			getInfo = (LinearLayout) itemView.findViewById(R.id.getInfo);// 取得实例
		}
	}

	private void initDatas() {
		if (brithdayMess != null) {
			mList.add(brithdayMess);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", mListView.pageNo);
		HttpUtil.getInstance().postJsonObjectRequest("apps/message/list", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				if (result.optInt("code") == 200){//成功返回数据
					try {
						List<Message> persionList = getGson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<PersonEntity>>(){}.getType());
						if (persionList == null){
							mListView.error(MyListView.Error.OnError);
						} else if (persionList.size() == 0) {
							if (brithdayMess == null) {
								mListView.error(MyListView.Error.NoDatas);
							} else {
								mListView.flish();
							}
						} else {
							if (mListView.pageNo == 1) {
								mList.clear();
								if (brithdayMess != null) {
									mList.add(brithdayMess);
								}
							}
							mList.addAll(persionList);
							mListView.flish();
						}
					} catch (JSONException e) {
						mListView.error(MyListView.Error.OnError);
					}
				}
			}

			@Override
			public void onError(VolleyError error) {
				mListView.error(MyListView.Error.OnError);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();    
		if(mList.size() != 0){
			helper.setValue("messages", getGson().toJson(mList.subList(0, mList.size()<20?mList.size():20)));//保存缓存数据
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		mListView.reLoadDatas();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initBrithDayTheme();
	}

	public void initBrithDayTheme(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
		String today = dateFormat.format(now);

		boolean isBrithDay;
		if(helper.getUser() == null){
			isBrithDay = false;
		}else if(StringUtils.isNotEmpty(helper.getUser().getBirthday())){
			String brithday = helper.getUser().getBirthday();
//			if(helper.getUser().getUserId().equals("portal")) {
//				brithday = "2017-03-21";
//			}
			if(brithday.length() == 10){
				isBrithDay = today.substring(5,today.length()).equals(brithday.substring(5,brithday.length()));
			}else{
				isBrithDay = today.substring(6,today.length()).equals(brithday.substring(5,brithday.length()));
			}
		}else{
			isBrithDay = false;
		}
		if (isBrithDay) {
			showBrithdayTheme();
			brithdayMess = new Message();
			brithdayMess.setTitle("标题：生日贺卡");
			brithdayMess.setContent("内容："+helper.getUser().getUserName()+"生日快乐");
			brithdayMess.setTime(today.substring(5,7)+"月"+today.substring(8,10)+"日"+" 00 : 00");
			brithdayMess.setMoudel("生日提醒");
			brithdayMess.setAction("brithday");
			brithdayMess.setParam("param:pp,sdf:pp,sd:oo");
			brithdayMess.setId("brithday");
		}else{
			hideBrithdayTheme();
		}
	}
}
