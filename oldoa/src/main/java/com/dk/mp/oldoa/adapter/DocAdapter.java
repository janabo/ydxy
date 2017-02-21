package com.dk.mp.oldoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.Doc;

import java.util.List;


public class DocAdapter extends BaseAdapter {
	private Context context;
	private List<Doc> list;
	private LayoutInflater lif;

	public List<Doc> getList() {
		return list;
	}

	public void setList(List<Doc> list) {
		this.list = list;
	}

	public DocAdapter(Context context, List<Doc> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Doc getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);
			convertView = lif.inflate(R.layout.oa_list_item, null);
		} else {
			mv = (MyView) convertView.getTag();
		}
		mv.title = (TextView) convertView.findViewById(R.id.title);
		mv.img = (ImageView) convertView.findViewById(R.id.img);
		mv.bumen = (TextView) convertView.findViewById(R.id.bumen);
		mv.shijian = (TextView) convertView.findViewById(R.id.shijian);
		if ("OA_SW".equals(list.get(position).getType())) {
			mv.img.setImageResource(R.mipmap.shouwen);
		} else if ("OA_FW".equals(list.get(position).getType())) {
			mv.img.setImageResource(R.mipmap.fawen);
		} else {
			mv.img.setImageResource(R.mipmap.baogao);
		}
		mv.title.setText(list.get(position).getTitle());
		mv.bumen.setText(list.get(position).getDepartment());
		mv.shijian.setText(list.get(position).getTime());
		convertView.setTag(mv);
		return convertView;
	}

	/**
	 * @version 2013-3-21
	 * @author wangw
	 */
	private static class MyView {
		private TextView title, bumen, shijian;
		ImageView img;
	}
}
