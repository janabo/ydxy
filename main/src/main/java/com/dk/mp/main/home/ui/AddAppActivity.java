package com.dk.mp.main.home.ui;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.dk.mp.core.db.AppManager;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.main.R;
import com.dk.mp.main.home.adapter.AddAppAdapter;

public class AddAppActivity extends MyActivity implements OnClickListener {

	private RecyclerView addRecyclerView;
	private AddAppAdapter adapter;

	@Override
	protected int getLayoutID() {
		return R.layout.addapp;
	}


	@Override
	protected void initialize(){
		super.initialize();
		setTitle("显示应用");
		setRightText("确定", Color.rgb(148,196,248), this);
		addRecyclerView = (RecyclerView)findViewById(R.id.addview);
		GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
		//设置布局管理器
		addRecyclerView.setLayoutManager(layoutManager);
		//设置为垂直布局，这也是默认的
		layoutManager.setOrientation(OrientationHelper.VERTICAL);
		adapter = new AddAppAdapter();
		adapter.setOnItemClickListener(new AddAppAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, App data) {
				ImageView add = (ImageView) view.findViewById(R.id.gragItemViewAdd);
				add.setVisibility(add.getVisibility() == View.GONE? View.VISIBLE: View.GONE);
				data.setSelected(!data.isSelected());

				boolean b = false;
				for(App app:adapter.getList()){
					if(app.isSelected()){
						b = true;
						break;
					}
				}
				if(b){
					setRightTextColor(Color.WHITE);
				}else{
					setRightTextColor(Color.rgb(148,196,248));
				}
			}
		});
		addRecyclerView.setAdapter(adapter);
		addRecyclerView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.VERTICAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        addRecyclerView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
	}

	@Override
	public void onClick(View v) {
		boolean b = false;
		for(App app:adapter.getList()){
			if(app.isSelected()){
				b = true;
				break;
			}
		}
		if(!b) return;
		for(App app:adapter.getList()){
			if(app.isSelected()){
				app.setSelected(false);
				AppManager.updateNotin(AddAppActivity.this, app.getId(), false);
			}
		}
		finish();
	}
}
