package com.dk.mp.newoa.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.Detail;
import com.dk.mp.newoa.entity.Jbxx;
import com.dk.mp.newoa.entity.NewDoc;
import com.dk.mp.newoa.entity.OASubmit;
import com.dk.mp.newoa.entity.SeriMap;
import com.dk.mp.newoa.http.Manager;
import com.dk.mp.newoa.widget.OADetailView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * oa详情
 * 作者：janabo on 2017/1/5 10:13
 */
public class OADetailActivity extends MyActivity implements View.OnClickListener{
    private ErrorLayout mError;
    private Map<String,Object> map;
    private OADetailView content;
    private LinearLayout footer;
    private Detail detail;
    private int w;
    public int avgWidth = 0;
    public static OADetailActivity instance;
    private LinearLayout lin_footer;
    private String isgzzb="false";//是否是工作周报，true是

    @Override
    protected int getLayoutID() {
        return R.layout.oa_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        instance=OADetailActivity.this;
        Bundle bundle = getIntent().getExtras();
        NewDoc doc = bundle.getParcelable("doc");
        SeriMap sMap = (SeriMap) bundle.getSerializable("map");
        map = sMap.getMap();
        setTitle(getIntent().getStringExtra("typename"));
        isgzzb = getIntent().getStringExtra("isgzzb");
        findView();
        getData();
    }

    private void findView(){
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        content = (OADetailView)findViewById(R.id.content);
        footer = (LinearLayout) findViewById(R.id.footer);
        lin_footer = (LinearLayout) findViewById(R.id.lin_footer);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        w = wm.getDefaultDisplay().getWidth();
        ViewCompat.setTransitionName(content, "detail_element");
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            getOaDetail();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    /**
     * 获取oa详情数据
     */
    private void getOaDetail(){
        String url;
        if("true".equals(isgzzb)){
            url = "apps/gzzb/detail";
        }else{
            url = "apps/office/detail";
        }
        HttpUtil.getInstance().postJsonObjectRequest(url, map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null && result.getInt("code") == 200){
                        detail = Manager.getOaDetail(result.getJSONObject("data"));
                        if(detail != null)
                            mHandler.sendEmptyMessage(0);
                        else
                            mHandler.sendEmptyMessage(-1);
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }

            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    String html = detail.getHtml();
                    if ("Android 4.4.2".equals(DeviceUtil.getOsType()) && html.contains("<meta name='viewport' content='initial-scale=1.0, maximum-scale=1.0, user-scalable=no' />")) {
                        html = html.replace("<meta name='viewport' content='initial-scale=1.0, maximum-scale=1.0, user-scalable=no' />", "");
                    }
                    Logger.info("##############################" + html);
                    Logger.info("手机系统版本号" + DeviceUtil.getOsType());
                    content.setText(html);
                    List<Jbxx> list = detail.getJbxxs();
                    Logger.info("msg=" + list.size());
                    if (list.size() > 0) {
                        avgWidth = w / list.size();
                        lin_footer.setVisibility(View.VISIBLE);
                    } else {
                        avgWidth = w;
                        lin_footer.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        Jbxx j = list.get(i);
                        if (i > 0) {
                            addSplictLine();
                        }
                        addView(j.getValue(), j.getKey());
                    }
                    break;
                case -1:
                    mError.setErrorType(ErrorLayout.NODATA);
                    break;
            }
        };
    };

    private void addView(final String value,final String key){
        TextView tx = new TextView(mContext);
        tx.setText(value);
        tx.setWidth(avgWidth);
        tx.setHeight(StringUtils.dip2px(mContext, 42));
        tx.setGravity(Gravity.CENTER);
        tx.setBackgroundResource(R.drawable.ripple_bg_while);
        tx.setTextAppearance(mContext, R.style.oa_detail_footer_textview);
        footer.addView(tx);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLis(key,value);
            }
        });
    }

    private void addSplictLine(){
        ImageView v = new ImageView(mContext);
        v.setBackgroundColor(getResources().getColor(R.color.oa_detail_footer_line));
        v.setLayoutParams(new LinearLayout.LayoutParams(1, StringUtils.dip2px(mContext, 15)));
        footer.addView(v);
    }

    private void onClickLis(String submitType,String submitName){
        Logger.info("12311313:"+submitType);
        Intent intent;
        if("true".equals(isgzzb)){
            if("hide".equals(detail.getSubmit().getSuggestion()) && ("hide".equals(detail.getSubmit().getUsers()) || !"zb".equals(submitType))){//意见和选人都是hide时直接在当前页面处理
//                gzzb(submitType);
            }else{
//                intent = new Intent(OADetailActivity.this,SubmitActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                intent.putExtra("title", submitName);
//                if("true".equals(detail.getSubmit().getUsers()) && "zb".equals(submitType)){//
//                    intent.putExtra("choose", "-1");
//                }else{
//                    intent.putExtra("choose", "0");
//                }
//                zb(intent, submitType, submitName);
            }
        }else{
//            if("nextStep".equals(submitType)){
//                intent = new Intent(OADetailActivity.this,OperationActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                gw(intent, submitType, submitName);
//            }else if("tg".equals(submitType)||"btg".equals(submitType)){
//                intent = new Intent(OADetailActivity.this,HcglSubmitActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                intent.putExtra("title", submitName);
//                gw(intent, submitType, submitName);
//            } else if ("shouyue".equals(submitType)) {
//                shouyue(submitType);
//            } else if ("chuanyue".equals(submitType)) {
//                intent = new Intent(OADetailActivity.this, SubmitActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                gwcy(intent, submitType);
//            } else if ("banjie".equals(submitType)) {
//                intent = new Intent(OADetailActivity.this, SubmitActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                gwcybj(intent, submitType);
//            } else if ("addUser_false".equals(submitType)) {
//                showMessage("不能添加部门负责人");
//            } else if ("addUser".equals(submitType)) {
//                intent = new Intent(OADetailActivity.this, SubmitActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                addUser(intent, "flowSign");
//            }else{
//                intent = new Intent(OADetailActivity.this,SubmitActivity.class);
//                intent.putExtra("isgzzb", isgzzb);
//                intent.putExtra("choose","0");
//                intent.putExtra("title", submitName);
//                gw(intent, submitType, submitName);
//            }
        }
    }

    private void gw(Intent intent, String submitType, String submitName) {
        Bundle bundle = new Bundle();
        SeriMap map = new SeriMap();
        map.setMap(detail.getParams());
        bundle.putSerializable("map", map);
        bundle.putParcelable("submit", detail.getSubmit());
        intent.putExtras(bundle);
        intent.putExtra("submitType", submitType);
        if (!"nextStep".equals(submitType)) {
            intent.putExtra("choose", "0");
            intent.putExtra("title", submitName);
        }
        startActivity(intent);
    }

    /**
     * 工作周报转办
     * @param intent
     * @param submitType
     * @param submitName
     */
    private void zb(Intent intent, String submitType, String submitName) {
        Bundle bundle = new Bundle();
        SeriMap map = new SeriMap();
        map.setMap(detail.getParams());
        bundle.putSerializable("map", map);
        bundle.putParcelable("submit", detail.getSubmit());
        intent.putExtras(bundle);
        intent.putExtra("submitType", submitType);
        startActivity(intent);
    }

//    private void shouyue(String submitType) {
//        if (DeviceUtil.checkNet(this)) {
//            Map<String, String> params = detail.getParams();
//            params.put("type", submitType);
//            showProgressDialog();
//            for (String key : params.keySet()) {
//                System.out.println(key + "=" + params.get(key));
//            }
//            HttpClientUtil.post("apps/office/subOpinion", params, new RequestCallBack<String>() {
//                @Override
//                public void onSuccess(ResponseInfo<String> responseInfo) {
//                    hideProgressDialog();
//                    Result str = NewHttpUtil.submit(responseInfo);
//                    if (str.isResult()) {
//                        showMessage(str.getMsg());
//                        BroadcastUtil.sendBroadcast(context, "com.test.action.refresh");
//                        finish();
//                    } else {
//                        showMessage(str.getMsg());
//                    }
//                }
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    showMessage(getString(R.string.server_failure));
//                    hideProgressDialog();
//                }
//            });
//        }
//    }

    private void gwcy(Intent intent, String submitType) {
        Bundle bundle = new Bundle();
        SeriMap map = new SeriMap();
        map.setMap(detail.getParams());
        bundle.putSerializable("map", map);
        OASubmit submit = new OASubmit();
        submit.setNgSuggestion("false");
        submit.setSuggestion("false");//长沙医学院
        submit.setSxNgSuggestion("false");
        submit.setSxSuggestion("false");
        bundle.putParcelable("submit", submit);

        intent.putExtras(bundle);
        intent.putExtra("submitType", submitType);
        intent.putExtra("choose", "2");
        intent.putExtra("title", "传阅");
        startActivity(intent);

    }

    private void gwcybj(Intent intent, String submitType) {
        Bundle bundle = new Bundle();
        SeriMap map = new SeriMap();
        map.setMap(detail.getParams());
        bundle.putSerializable("map", map);

        OASubmit submit = new OASubmit();
        submit.setNgSuggestion("false");
        submit.setSuggestion("true");
        submit.setSxNgSuggestion("false");
        submit.setSxSuggestion("false");
        bundle.putParcelable("submit", submit);
        intent.putExtras(bundle);
        intent.putExtra("submitType", submitType);
        intent.putExtra("choose", "0");
        intent.putExtra("title", "办结");
        startActivity(intent);

    }

    private void addUser(Intent intent, String submitType) {
        Bundle bundle = new Bundle();
        SeriMap map = new SeriMap();
        map.setMap(detail.getParams());
        bundle.putSerializable("map", map);

        OASubmit submit = new OASubmit();
        submit.setNgSuggestion("false");
        submit.setSuggestion("hide");
        submit.setSxNgSuggestion("false");
        submit.setSxSuggestion("false");
        bundle.putParcelable("submit", submit);
        intent.putExtras(bundle);
        intent.putExtra("submitType", submitType);
        intent.putExtra("choose", "2");
        intent.putExtra("title", "部门负责人");
        intent.putExtra("nodeId", detail.getParams().get("currentNodeId"));
        startActivity(intent);
    }


    /**
     * 工作周报 办结，通过，不通过
     * @param submitType
     */
//    private void gzzb(String submitType) {
//        if (DeviceUtil.checkNet(this)) {
//            Map<String, String> params = detail.getParams();
//            params.put("type", submitType);
//            showProgressDialog();
//            for (String key : params.keySet()) {
//                System.out.println(key + "=" + params.get(key));
//            }
//            HttpClientUtil.post("apps/gzzb/subOpinion", params, new RequestCallBack<String>() {
//                @Override
//                public void onSuccess(ResponseInfo<String> responseInfo) {
//                    hideProgressDialog();
//                    Result str = NewHttpUtil.submit(responseInfo);
//                    if (str.isResult()) {
//                        showMessage(str.getMsg());
//                        BroadcastUtil.sendBroadcast(context, "com.test.action.refresh");
//                        finish();
//                    } else {
//                        showMessage(str.getMsg());
//                    }
//                }
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    showMessage(getString(R.string.server_failure));
//                    hideProgressDialog();
//                }
//            });
//        }
//    }
}
