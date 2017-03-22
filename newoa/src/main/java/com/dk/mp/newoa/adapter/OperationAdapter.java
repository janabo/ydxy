package com.dk.mp.newoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.NewOperation;

import java.util.List;

public class OperationAdapter extends BaseAdapter {
	private Context context;
	private List<NewOperation> list;

	public List<NewOperation> getList() {
		return list;
	}

	public void setList(List<NewOperation> list) {
		this.list = list;
	}

	public OperationAdapter(Context context, List<NewOperation> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public NewOperation getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.oa_operation_item, parent, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title = (TextView) convertView.findViewById(R.id.title);
		holder.no = (TextView) convertView.findViewById(R.id.no);
		String title = list.get(position).getNodeName();
		holder.title.setText(title);
		if(title.length()>0){
			title = title.substring(0, 1);
		}
		holder.no.setText(title);
		setNo(holder.no, position);
		
		return convertView;
	}
	
	private void setNo(TextView text,int position){
		switch (position) {
		case 0:
			text.setBackgroundResource(R.drawable.operation_icon_1);
			break;
		case 1:
			text.setBackgroundResource(R.drawable.operation_icon_2);
			break;
		case 2:
			text.setBackgroundResource(R.drawable.operation_icon_3);
			break;
		case 3:
			text.setBackgroundResource(R.drawable.operation_icon_4);
			break;
		case 4:
			text.setBackgroundResource(R.drawable.operation_icon_5);
			break;
		default:
			break;
		}
	}

	/**
	 * @version 2013-3-21
	 * @author wangw
	 */
	private static class ViewHolder {
		TextView title, no;
	}
}
