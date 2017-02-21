package com.dk.mp.oldoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.WeekEventListEntity;

import java.util.List;


public class listViewAdapter extends BaseAdapter {
	private Context context;
	private List<WeekEventListEntity> list;
	private LayoutInflater inflater;

	public listViewAdapter(Context context, List<WeekEventListEntity> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public WeekEventListEntity getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyView my;
		if (convertView == null) {
			my = new MyView();
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.week_list_item, null);
			convertView.setTag(my);
		} else {
			my = (MyView) convertView.getTag();
		}

		my.time = (TextView) convertView.findViewById(R.id.week_time);
		my.place = (TextView) convertView.findViewById(R.id.week_place);
		my.content = (TextView) convertView.findViewById(R.id.week_content);

		WeekEventListEntity wel = list.get(position);
		my.time.setText(wel.getTime());
		my.place.setText(wel.getAddress());
		my.content.setText(wel.getContent());
		return convertView;
	}

	private static class MyView {
		private TextView time, place, content;
	}

}
