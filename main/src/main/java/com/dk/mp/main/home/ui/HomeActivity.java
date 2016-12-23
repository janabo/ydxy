package com.dk.mp.main.home.ui;

import android.content.Intent;
import android.view.View;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.main.R;
import com.dk.mp.main.setting.ui.SettingActivity;
import com.dk.mp.txl.ui.TxlDepartActivity;

/**
 * 作者：janabo on 2016/12/14 15:25
 */
public class HomeActivity extends MyActivity{

    @Override
    protected int getLayoutID() {
        return R.layout.mp_main;
    }

    @Override
    protected void initialize() {
        super.initialize();

    }

    /**
     * 设置
     * @param v
     */
    public void tosetting(View v){
        Intent intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转学校新闻
     * @param v
     */
    public void toxxxw(View v){
//        Intent intent = new Intent(mContext, NewsListActivity.class);
//        intent.putExtra("title","校园新闻");
//        startActivity(intent);

        Intent intent = new Intent(mContext, TxlDepartActivity.class);
        intent.putExtra("title","通讯录");
        startActivity(intent);

    }

}
