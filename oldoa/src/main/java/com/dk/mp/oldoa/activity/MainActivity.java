package com.dk.mp.oldoa.activity;

import android.animation.Animator;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.dk.mp.core.application.MyApplication;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.utils.Constant;

/**
 * 作者：janabo on 2017/2/20 14:19
 */
public class MainActivity extends TabActivity {

    private TabHost tabHost;
    private RadioGroup radioGroup;
    private boolean bool;
    RadioButton daiban, yiban;
    private TextView title;
    private int x;
    private int y;
    private LinearLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oa_tab);
        x = getIntent().getIntExtra("x",-10);
        y = getIntent().getIntExtra("y",-10);
        findView();
        init();
    }

    private void findView() {
        tabHost = getTabHost();
        frameLayout = (LinearLayout) findViewById(R.id.frameLayout);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        daiban = (RadioButton) findViewById(R.id.daiban);
        yiban = (RadioButton) findViewById(R.id.yiban);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        title = (TextView) findViewById(R.id.title);
        title.setText("公文管理");
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                back();
            }
        });
    }

    private void init() {
        Intent intent1 = new Intent(MainActivity.this, ListActivity.class).putExtra("tag", "readnot")
                .putExtra("state", "0").putExtra("interface", Constant.DAI_BAN_INTERFACE)
                .putExtra("x",x).putExtra("y",y);

        Intent intent2 = new Intent(MainActivity.this, ListActivity.class).putExtra("tag", "readed")
                .putExtra("state", "1").putExtra("interface", Constant.YI_BAN_INTERFACE)
                .putExtra("x",x).putExtra("y",y);

        tabHost.addTab(tabHost.newTabSpec("daiban").setIndicator("daiban").setContent(intent1));
        tabHost.addTab(tabHost.newTabSpec("yiban").setIndicator("yiban").setContent(intent2));
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.daiban) {
                tabHost.setCurrentTabByTag("daiban");
                daiban.setTextColor(getResources().getColor(R.color.old_tab_oa_selected));
                daiban.setBackgroundResource(R.drawable.slide_left_selected);
                yiban.setTextColor(getResources().getColor(R.color.old_tab_oa_unselected));
                yiban.setBackgroundResource(R.drawable.slide_right_normal);
            } else if (checkedId == R.id.yiban) {
                tabHost.setCurrentTabByTag("yiban");

                daiban.setTextColor(getResources().getColor(R.color.old_tab_oa_unselected));
                daiban.setBackgroundResource(R.drawable.slide_left_normal);

                yiban.setTextColor(getResources().getColor(R.color.old_tab_oa_selected));
                yiban.setBackgroundResource(R.drawable.slide_right_selected);
            }
        }
    };

    /**
     * 更新代办数量
     * @param count
     */
    private void updatedaiBanCount(final String count) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                daiban.setText(Html.fromHtml("待办"));
            }
        });
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.UPDATE_COUNT_UI)) {
                String count = intent.getStringExtra("count");
                updatedaiBanCount(count);
            }
        }
    };

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UPDATE_COUNT_UI);
        this.registerReceiver(mRefreshBroadcastReceiver, filter);
        super.onResume();

    };

    @Override
    public void onDestroy() {
        this.unregisterReceiver(mRefreshBroadcastReceiver);
        super.onDestroy();
    }

    /**
     * 公共手机返回按钮事件.
     * @param keyCode keyCode
     * @param event  KeyEvent
     * @return  boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 返回
     */
    public void back() {
        if (x != -10) {
            frameLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Animator animator = MyApplication.createRevealAnimator(true,x,y,frameLayout, com.dk.mp.oldoa.activity.MainActivity.this);
                        animator.start();
                    }else{
                        finish();
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                onBackPressed();
            }else{
                finish();
            }
        }
    }
}
