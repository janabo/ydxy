package com.dk.mp.main.setting.ui;

import android.view.View;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.main.R;

/**
 * 关于软件
 * 作者：janabo on 2017/2/17 14:40
 */
public class AboutActivity extends MyActivity{
    private TextView version,title;
    @Override
    protected int getLayoutID() {
        return R.layout.mp_about;
    }

    /**
     * 检查更新信息
     * @param v
     */
    public void toUpdate(View v){

    }
}
