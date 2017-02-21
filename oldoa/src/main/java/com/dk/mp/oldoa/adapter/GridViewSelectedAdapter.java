package com.dk.mp.oldoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.entity.Person;

import java.util.List;

public class GridViewSelectedAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Person> list;

	public GridViewSelectedAdapter(Context context, List<Person> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Person getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.oa_gridview_person_selected, null);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String name = list.get(position).getName();
		holder.txv = (TextView) convertView.findViewById(R.id.txv_person_selected);
		holder.img_delete_person = (ImageView) convertView.findViewById(R.id.img_delete_person);

		holder.txv.setText(name);
		if (list.get(position).isCheck()) {
			holder.img_delete_person.setVisibility(View.VISIBLE);
		} else {
			holder.img_delete_person.setVisibility(View.INVISIBLE);
		}
		convertView.setTag(holder);

		return convertView;
	}

	private class Holder {
		private TextView txv;
		private ImageView img_delete_person;
	}

}
