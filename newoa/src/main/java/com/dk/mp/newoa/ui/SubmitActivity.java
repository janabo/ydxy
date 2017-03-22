package com.dk.mp.newoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.OASubmit;
import com.dk.mp.newoa.entity.SeriMap;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2017/3/22 14:18
 */
public class SubmitActivity extends MyActivity implements View.OnClickListener {
    private RelativeLayout spyj_view, ngyj_view, spyj_sx_show, ngyj_sx_show, tjlxr_view;
    private EditText spyj_edit, ngyj_edit;
    private ImageView spyj_sx_add, ngyj_sx_add, spyj_sx_sc, ngyj_sx_sc, tjlxr;
    private TextView lxr;
    private Button submit;
    private Map<String, String> params;
    private OASubmit oaSubmit;//填写审批意见(true必填)从上一个界面传过来
    private String userIds="";
    private String choose;//从上一个界面传过来   ( 0:无需选人 1：选一个人  2：选多个人)
    private String submitType;//提交类型从上一个界面传过来  (flowRepeal:流程撤销,flowWithDraw:流程取回,giveBack:打回,flowEnd:办结,pass:通过,notPass:不通过,next:下一步)
    private String targetNodeId;
    private String operP="";
    private TextView tjlxr_bt_tip;
    private String isgzzb="false";//是否是工作周报，true是

    @Override
    protected int getLayoutID() {
        return R.layout.oa_submit;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        isgzzb = getIntent().getStringExtra("isgzzb");
        mContext = SubmitActivity.this;
        findView();
        setUI();
    }

    private void findView() {
        spyj_view = (RelativeLayout) findViewById(R.id.spyj_view);
        ngyj_view = (RelativeLayout) findViewById(R.id.ngyj_view);
        spyj_sx_show = (RelativeLayout) findViewById(R.id.spyj_sx_show);
        ngyj_sx_show = (RelativeLayout) findViewById(R.id.ngyj_sx_show);
        tjlxr_view = (RelativeLayout) findViewById(R.id.tjlxr_view);
        tjlxr_bt_tip = (TextView) findViewById(R.id.tjlxr_bt_tip);

        spyj_edit = (EditText) findViewById(R.id.spyj_edit);
        ngyj_edit = (EditText) findViewById(R.id.ngyj_edit);

        spyj_sx_add = (ImageView) findViewById(R.id.spyj_sx_add);
        ngyj_sx_add = (ImageView) findViewById(R.id.ngyj_sx_add);
        spyj_sx_sc = (ImageView) findViewById(R.id.spyj_sx_sc);
        ngyj_sx_sc = (ImageView) findViewById(R.id.ngyj_sx_sc);
        tjlxr = (ImageView) findViewById(R.id.tjlxr);
        spyj_sx_add.setOnClickListener(this);
        ngyj_sx_add.setOnClickListener(this);
        spyj_sx_sc.setOnClickListener(this);
        ngyj_sx_sc.setOnClickListener(this);
        tjlxr.setOnClickListener(this);

        lxr = (TextView) findViewById(R.id.lxr);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void setUI() {
        Bundle bundle = getIntent().getExtras();
        oaSubmit = bundle.getParcelable("submit");
        targetNodeId=getIntent().getStringExtra("nodeId");
        choose = getIntent().getStringExtra("choose");
        submitType = getIntent().getStringExtra("submitType");
        SeriMap sMap = (SeriMap) bundle.getSerializable("map");
        params = sMap.getMap();


        if("hide".equals(oaSubmit.getSuggestion())){
            spyj_view.setVisibility(View.GONE);
        }else{
            spyj_view.setVisibility(View.VISIBLE);
            if ("true".equals(oaSubmit.getSuggestion())) {
                if ("banjie".equals(submitType)) {
                    spyj_edit.setHint("意见(必填)");
                } else {
                    spyj_edit.setHint("审批意见(必填)");
                }
            } else {
                if ("banjie".equals(submitType)) {
                    spyj_edit.setHint("意见(选填)");
                } else {
                    spyj_edit.setHint("审批意见(选填)");
                }
            }
        }
        if("banjie".equals(submitType)){
            spyj_edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        }else{
            spyj_edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        }

        //判断审批手写意见
        if ("true".equals(oaSubmit.getSxSuggestion())) {
            spyj_sx_show.setVisibility(View.VISIBLE);
        } else {
            spyj_sx_show.setVisibility(View.GONE);
        }

        //判断拟稿意见
        if ("true".equals(oaSubmit.getNgSuggestion())) {
            ngyj_view.setVisibility(View.VISIBLE);
            spyj_view.setVisibility(View.GONE);
        } else {
            ngyj_view.setVisibility(View.GONE);
        }

        //判断拟稿手写意见
        if ("true".equals(oaSubmit.getSxNgSuggestion())) {
            ngyj_sx_show.setVisibility(View.VISIBLE);
        } else {
            ngyj_sx_show.setVisibility(View.GONE);
        }

        //判断提交类型
        if (!"0".equals(choose)) {
            tjlxr_view.setVisibility(View.VISIBLE);
        } else {
            tjlxr_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.spyj_sx_add) {//待定

        } else if (v.getId() == R.id.ngyj_sx_add) {//待定

        } else if (v.getId() == R.id.tjlxr) {
            Intent intent = new Intent(mContext, PeopleActivity.class);
            intent.putExtra("choose", "1");//单选还是多选
            intent.putExtra("operP", operP);
            intent.putExtra("isgzzb", isgzzb);
            Bundle bundle = new Bundle();
            SeriMap sMap = new SeriMap();
            sMap.setMap(params);
            bundle.putSerializable("map", sMap);
            intent.putExtras(bundle);
            startActivityForResult(intent, 6);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        } else if (v.getId() == R.id.submit) {
            if (spyj_view.getVisibility() == View.VISIBLE && "true".equals(oaSubmit.getSuggestion())
                    && spyj_edit.getText().length() == 0) {
                if ("banjie".equals(submitType)) {
                    showErrorMsg("请先填写意见，再提交");
                } else {
                    showErrorMsg("请先填写审批意见，再提交");
                }
            } else if (spyj_edit.getText().length() > 200 && !"banjie".equals(submitType)) {
                showErrorMsg("审批意见不能大于200字");
            } else if (spyj_edit.getText().length() > 100 && "banjie".equals(submitType)) {
                showErrorMsg("意见不能大于100字");
            } else if (ngyj_view.getVisibility() == View.VISIBLE && ngyj_edit.getText().length() == 0) {
                showErrorMsg("请填写拟办意见");
            } else if (ngyj_edit.getText().length() > 200) {
                showErrorMsg("拟办意见不能大于200字");
            } else if (!"0".equals(choose) && !StringUtils.isNotEmpty(userIds)) {
                showErrorMsg("请选择人员");
            } else {
                if (DeviceUtil.checkNet()) {
                    if("flowSign".equals(submitType)){
                        flowSign();
                    }else{
                        submit();
                    }
                }else{
                    showErrorMsg(getReString(R.string.net_no2));
                }
            }
        }
    }

    private void flowSign() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("currentNodeId", params.get("currentNodeId"));
        map.put("flowInstanceId", params.get("flowInstanceId"));
        map.put("userIdString", userIds);
        map.put("flowId",  params.get("flowId"));

        HttpUtil.getInstance().postJsonObjectRequest("apps/office/checkUser", map, new HttpListener<JSONObject>()  {
            @Override
            public void onSuccess(JSONObject result) {
                if(result != null){
                    try {
                        boolean bool = result.optBoolean("data");
                        String msg = result.optString("msg");
                        if(bool){
                            submit();
                        }else{
                            showErrorMsg(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                showErrorMsg(getReString(R.string.data_fail));
            }
        });

    }

    /**
     * 提交
     */
    public void submit() {
        params.put("userIdString", userIds);
        params.put("suggestion", spyj_edit.getText().toString());
        params.put("ngSuggestion", ngyj_edit.getText().toString());
        params.put("type", submitType);
        if(targetNodeId!=null){
            params.put("targetNodeId", targetNodeId);
        }
        String url;
        if("true".equals(isgzzb))
            url = "apps/gzzb/subOpinion";
        else
            url = "apps/office/subOpinion";

        Map<String,Object> mObject = new HashMap<>();
        for (String key : params.keySet()) {
            System.out.println(key + "=" + params.get(key));
            mObject.put(key,params.get(key));
        }
        HttpUtil.getInstance().postJsonObjectRequest(url, mObject, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result != null){
                    try {
                        boolean bool = result.optBoolean("data");
                        String msg = result.optString("msg");
                        if(bool){
                            showErrorMsg(msg);
                            BroadcastUtil.sendBroadcast(mContext, "com.test.action.refresh");
                            OADetailActivity.instance.finish();
                            if (OperationActivity.instance != null) {
                                OperationActivity.instance.finish();
                            }
                            finish();
                        }else{
                            showErrorMsg(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                showErrorMsg(getReString(R.string.data_fail));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://相册选取 剪切

                break;
            case 2://拍照 剪切

                break;
            case 3://剪切后保存

                break;
            case 4://拍照 不剪切

                break;
            case 5://相册选取不 剪切

                break;
            case 6://选择联系人
                if (resultCode == RESULT_OK) {
                    userIds = data.getStringExtra("userIds");
                    lxr.setText(data.getStringExtra("userNames"));
                    operP = data.getStringExtra("operP");
                    tjlxr_bt_tip.setVisibility(View.GONE);
                }

                break;
            default:
                break;
        }
    }
}
