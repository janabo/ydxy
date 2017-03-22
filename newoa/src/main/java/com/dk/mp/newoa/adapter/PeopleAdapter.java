package com.dk.mp.newoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.util.Logger;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.People;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleAdapter extends BaseAdapter {
	private Context context;
	private List<People> list;
	private LayoutInflater lif;
	private Map<String,String> operP;
	private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

	public List<People> getList() {
		return list;
	}

	public void setList(List<People> list) {
		this.list = list;
	}
	
	public Map<String, String> getOperP() {
		return operP;
	}

	public void setOperP(Map<String, String> operP) {
		this.operP = operP;
	}

	public PeopleAdapter(Context context, List<People> list,Map<String,String> operP) {
		this.context = context;
		this.list = list;
		this.operP = operP;
		// 初始化数据
		initDate(false);
	}

	public void notify(List<People> list) {
		this.list = list;
		notifyDataSetChanged();
		isSelected.clear();
		initDate(false);
	}

	public void clean() {
		isSelected.clear();
		initDate(false);
	}

	// 初始化isSelected的数据
	private void initDate(boolean bool) {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, bool);
		}
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.oa_people_item, null);// 设置要转化的layout文件
			mv.name = (TextView) convertView.findViewById(R.id.name);
			mv.bm = (TextView) convertView.findViewById(R.id.bm);
			mv.checkbox = (LinearLayout) convertView.findViewById(R.id.checkbox);
			mv.check = (CheckBox) convertView.findViewById(R.id.check);
		} else {
			mv = (MyView) convertView.getTag();
		}

		mv.bm.setText(list.get(position).getTitle());
		mv.name.setText(list.get(position).getName());
		String key = list.get(position).getTitle()+"&&"+list.get(position).getId();
		if(operP.get(key)!=null){
			getIsSelected().put(position,true);
		}
		mv.check.setChecked(getIsSelected().get(position));
		convertView.setTag(mv);
		return convertView;
	}

	public static class MyView {
		private TextView bm, name;
		private LinearLayout checkbox;
		private CheckBox check;

		public CheckBox getCheckBox() {
			return check;
		}
	}

	public HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}

	public List<Integer> getChecked() {
		List<Integer> checkList = new ArrayList<Integer>();
		if (isSelected.size() > 0) {
			Object s[] = isSelected.keySet().toArray();
			for (int i = 0; i < s.length; i++) {
				if (isSelected.get(s[i])) {
					checkList.add((Integer) s[i]);
					Logger.info("checkList:" + i);
				}
			}
		}
		return checkList;
	}

}
