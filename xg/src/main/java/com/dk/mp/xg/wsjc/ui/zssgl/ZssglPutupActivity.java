package com.dk.mp.xg.wsjc.ui.zssgl;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzWjrqPickActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.dk.mp.xg.R.id.xjrq;

/**
 * 住宿
 * 作者：janabo on 2017/2/22 15:18
 */
public class ZssglPutupActivity extends MyActivity{
    private TextView back,title,ok;
    private TextView xjrq_pick;//到校入住日期
    private String detailid;
    private EditText bz;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssgl_putup;
    }

    @Override
    protected void initView() {
        super.initView();
        back = (TextView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        ok = (TextView) findViewById(R.id.ok);
        xjrq_pick = (TextView) findViewById(R.id.xjrq_pick);
        bz = (EditText) findViewById(R.id.bz);
        title.setText("住宿");
        ok.setEnabled(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
        detailid = getIntent().getStringExtra("detailid");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", detailid);
                map.put("dxrzrq", xjrq_pick.getText().toString());
                map.put("bz", bz.getText().toString());
                HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/tjrz", map, new HttpListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                            if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                                showErrorMsg(jd.getMsg());
                                BroadcastUtil.sendBroadcast(mContext, "zssgl_refresh");
                                if(ZssglDetailActivity.instance != null){
                                    finish();
                                }
                                back();
                            } else {
                                showErrorMsg(jd.getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showErrorMsg("提交失败");
                        }
                    }
                    @Override
                    public void onError(VolleyError error) {
                        showErrorMsg("提交失败");
                    }
                });
            }
        });
    }

    /**
     * 选择到校入住日期
     * @param v
     */
    public void toPickDxrzrq(View v){
        Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
        startActivityForResult(intent, 3);
        intent.putExtra("date",xjrq);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 3:
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    xjrq_pick.setText(mWjrq);
                    ok.setEnabled(true);
                    ok.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                break;
        }
    }

}
