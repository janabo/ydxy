package com.dk.mp.lsgl;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
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
        layout_search.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator animator = createRevealAnimator(false, getIntent().getIntExtra("x",0), getIntent().getIntExtra("y",0));
                    animator.start();
                }
            }
        });
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Animator createRevealAnimator(boolean reversed, int x, int y) {
        float hypot = (float) Math.hypot(layout_search.getHeight(), layout_search.getWidth());
        float startRadius = reversed ? hypot : 0;
        float endRadius = reversed ? 0 : hypot;

        Animator animator = ViewAnimationUtils.createCircularReveal(
                layout_search, x, y,
                startRadius,
                endRadius);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (reversed)
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    layout_search.setVisibility(View.INVISIBLE);
                    finish();
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        return animator;
    }

    @Override
    public void back() {
        layout_search.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator animator = createRevealAnimator(true, getIntent().getIntExtra("x",0), getIntent().getIntExtra("y",0));
                    animator.start();
                }
            }
        });
    }

}
