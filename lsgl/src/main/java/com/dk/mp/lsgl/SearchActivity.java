package com.dk.mp.lsgl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;

/**
 * 宿舍打分记录搜索
 * 作者：janabo on 2017/1/12 17:09
 */
public class SearchActivity extends MyActivity {
    private EditText mKeywords;//搜索关键字
    private TextView cancle;//
    private LinearLayout layout_search;
    private LsglSearchListFragment fragment;

    @Override
    protected int getLayoutID() {
        return R.layout.app_lsgl_search;
    }

    @Override
    protected void initialize() {
        super.initialize();
        findView();
    }

    /**
     * 初始化界面
     */
    private void findView(){
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        mKeywords = (EditText) findViewById(R.id.search_Keywords);
        cancle = (TextView) findViewById(R.id.cancle_search);

        fragment = new LsglSearchListFragment();
        fragment.setType(getIntent().getStringExtra("type"));
        fragment.setRole(getIntent().getStringExtra("role"));
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = mKeywords.getText().toString();
                    if (StringUtils.isNotEmpty(keywords)) {
                        fragment.reloadDatas(keywords);
                    } else {
                        SnackBarUtil.showShort(layout_search,"请输入关键字");
                    }
                }
                return false;
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

}
