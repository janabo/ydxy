package com.dk.mp.xg.wsjc.ui.zssgl;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;

/**
 * 退宿申请
 * 作者：janabo on 2017/1/19 16:27
 */
public class ZssglAddTuiSuActivity extends MyActivity{
    private ScrollView mRootView;
    private ImageView wjxs_img;
    private LinearLayout wjxs_lin,wskfyx,ok;//退宿原因
    private TextView wjxs_x,wjxs_name,dfxx_pick,ok_text;//退宿原因
    private EditText sqly,bz,shyj;//申请理由,备注
    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;


    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_add_tuisu;
    }

    @Override
    protected void initView() {
        super.initView();
        mRootView = (ScrollView) findViewById(R.id.mRootView);
        wjxs_img = (ImageView) findViewById(R.id.wjxs_img);
        wjxs_lin = (LinearLayout) findViewById(R.id.wjxs_lin);
        wskfyx = (LinearLayout) findViewById(R.id.wskfyx);
        ok = (LinearLayout) findViewById(R.id.ok);
        wjxs_x = (TextView) findViewById(R.id.wjxs_x);
        wjxs_name = (TextView) findViewById(R.id.wjxs_name);
        dfxx_pick = (TextView) findViewById(R.id.dfxx_pick);
        ok_text = (TextView) findViewById(R.id.ok_text);
        sqly = (EditText) findViewById(R.id.sqly);
        bz = (EditText) findViewById(R.id.bz);
        shyj = (EditText) findViewById(R.id.shyj);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("学生退宿申请");
    }

    /**
     * 提交退宿申请
     * @param v
     */
    public void submitTuisu(View v){

    }

}
