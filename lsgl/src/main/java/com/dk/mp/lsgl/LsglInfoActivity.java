package com.dk.mp.lsgl;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.ui.SubmitButton;
import com.dk.mp.core.ui.SubmitInterface;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.lsgl.entity.DetailsEntity;
import com.dk.mp.lsgl.entity.FkEntity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongqs on 2017/2/6.
 */

public class LsglInfoActivity extends MyActivity implements View.OnClickListener{

    private LinearLayout detailslinelayout;
    private LinearLayout bzline;
    private LinearLayout fkline;
    private LinearLayout context;
    private LinearLayout rootview;
    private List<FkEntity> fklist;
    private TextView fktext;
    private EditText bztext;
    private SubmitButton submitButton;
    private boolean canBz;
    private boolean canFk;
    private ErrorLayout error_layout;

    @Override
    protected int getLayoutID() {
        return R.layout.app_lsgl_info;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initialize() {
        super.initialize();
        setTitle("留宿学生管理");

//        postponeEnterTransition();

        initViews();

        initDatas();
    }

    private void initViews(){
        canBz = getIntent().getBooleanExtra("canBz",false);
        canFk = getIntent().getBooleanExtra("canFk",false);

        submitButton = (SubmitButton) findViewById(R.id.submit);
        bzline = (LinearLayout) findViewById(R.id.bzline);
        fkline = (LinearLayout) findViewById(R.id.fkline);
        fktext = (TextView) findViewById(R.id.fktext);
        bztext = (EditText) findViewById(R.id.bztext);
        detailslinelayout = (LinearLayout) findViewById(R.id.detailslinelayout);
        error_layout = (ErrorLayout) findViewById(R.id.error_layout);
        context = (LinearLayout) findViewById(R.id.context);
        rootview = (LinearLayout) findViewById(R.id.rootview);

        bztext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });



        fkline.setOnClickListener(this);
        submitButton.setSubmitInterface(new SubmitInterface() {
            @Override
            public boolean beforeLoad() {
                String bz = bztext.getText().toString();
                Object fk = fktext.getTag();
                if (canFk){if (fk == null) {showErrorMsg("请填写反馈信息");return false;}}
                if (canBz){if (bz.length() == 0) {showErrorMsg("请填写备注内容");return false;}}
                return true;
            }
            @Override
            public String setUrl() {
                return "apps/lsxsgl/tj";
            }
            @Override
            public Map<String, Object> setMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bz",bztext.getText().toString());
                map.put("fkxx",fktext.getTag());
                map.put("id",getIntent().getStringExtra("id"));
                return map;
            }
        });
    }

    private void initDatas() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",getIntent().getStringExtra("id"));
        map.put("type",getIntent().getStringExtra("type"));
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/detail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    int margin = StringUtils.dip2px(LsglInfoActivity.this,10);
                    try {
                        DetailsEntity details = getGson().fromJson(result.getJSONObject("data").toString(), DetailsEntity.class);
                        for (String detail : details.getDetail()) {

                            detail = detail.replace("<font color='#9c9c9c'>","").replace("</font>","").replace("<font color='#212121'>","");

                            LinearLayout linearLayout = new LinearLayout(LsglInfoActivity.this);
                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            layoutParams.setMargins(margin, margin, margin, 0);
                            linearLayout.setLayoutParams(layoutParams);
                            TextView tv = new TextView(LsglInfoActivity.this);
                            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                            tv.setLayoutParams(tvParams);
                            tv.setTextSize(13);
                            SpannableStringBuilder builder = new SpannableStringBuilder(detail);
                            ForegroundColorSpan title = new ForegroundColorSpan(Color.rgb(156,156,156));
                            ForegroundColorSpan context = new ForegroundColorSpan(Color.rgb(33,33,33));
                            int length = detail.split("：")[0].length();
                            builder.setSpan(title, 0, length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            builder.setSpan(context, length+1, detail.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            tv.setText(builder);
                            linearLayout.addView(tv);
                            detailslinelayout.addView(linearLayout);
                        }

                        getFklist();

//                        error_layout.setVisibility(View.GONE);
//                        context.setVisibility(View.VISIBLE);
//                        bzline.setVisibility(canBz ? View.VISIBLE:View.GONE);
//                        fkline.setVisibility(canFk ? View.VISIBLE:View.GONE);
//                        submitButton.setVisibility( !(canBz && canFk) ? View.GONE : View.VISIBLE );
                    } catch (Exception e) {
                        error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == fkline){
//            HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/fkxxList",null,new HttpListener<JSONObject>(){
//                @Override
//                public void onSuccess(JSONObject result) {
//                    try {
//                        fklist = getGson().fromJson(result.getJSONArray("data").toString(), new TypeToken<List<FkEntity>>(){}.getType());
//                        String[] title = new String[fklist.size()];
//                        for (int i =0;i<fklist.size();i++){
//                            title[i] = fklist.get(i).getName();
//                        }
//                        setTheme(R.style.ActionSheetStyleiOS7);
//                        ActionSheet.createBuilder(LsglInfoActivity.this, getSupportFragmentManager())
//                                .setCancelButtonTitle("取消")
//                                .setOtherButtonTitles(title)
//                                .setCancelableOnTouchOutside(true)
//                                .setListener(new ActionSheet.ActionSheetListener() {
//                                    @Override
//                                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {}
//                                    @Override
//                                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//                                        fktext.setText(fklist.get(index).getName());
//                                        fktext.setTag(fklist.get(index).getId());
//                                    }
//                                }).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                @Override
//                public void onError(VolleyError error) {
//                }
//            });
            showActionSheet();
        }
    }



    public void getFklist() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/fkxxList",null,new HttpListener<JSONObject>(){
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    fklist = getGson().fromJson(result.getJSONArray("data").toString(), new TypeToken<List<FkEntity>>(){}.getType());
//                    String[] title = new String[fklist.size()];
//                    for (int i =0;i<fklist.size();i++){
//                        title[i] = fklist.get(i).getName();
//                    }
//                    setTheme(R.style.ActionSheetStyleiOS7);
//                    ActionSheet.createBuilder(LsglInfoActivity.this, getSupportFragmentManager())
//                            .setCancelButtonTitle("取消")
//                            .setOtherButtonTitles(title)
//                            .setCancelableOnTouchOutside(true)
//                            .setListener(new ActionSheet.ActionSheetListener() {
//                                @Override
//                                public void onDismiss(ActionSheet actionSheet, boolean isCancel) {}
//                                @Override
//                                public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//                                    fktext.setText(fklist.get(index).getName());
//                                    fktext.setTag(fklist.get(index).getId());
//                                }
//                            }).show();
                    error_layout.setVisibility(View.GONE);
                    context.setVisibility(View.VISIBLE);
                    bzline.setVisibility(canBz ? View.VISIBLE:View.GONE);
                    fkline.setVisibility(canFk ? View.VISIBLE:View.GONE);
                    submitButton.setVisibility( !(canBz && canFk) ? View.GONE : View.VISIBLE );
                } catch (JSONException e) {
                    error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
                }
            }
            @Override
            public void onError(VolleyError error) {
                error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        });
    }

    private void showActionSheet(){
        String[] title = new String[fklist.size()];
        for (int i =0;i<fklist.size();i++){
            title[i] = fklist.get(i).getName();
        }
        setTheme(R.style.ActionSheetStyleiOS7);
        ActionSheet.createBuilder(LsglInfoActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles(title)
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {}
                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        fktext.setText(fklist.get(index).getName());
                        fktext.setTag(fklist.get(index).getId());
                    }
                }).show();
    }
}
