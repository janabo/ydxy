package com.dk.mp.newoa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.adapter.OperationAdapter;
import com.dk.mp.newoa.entity.NewOperation;
import com.dk.mp.newoa.entity.OASubmit;
import com.dk.mp.newoa.entity.SeriMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/3/22 14:59
 */
public class OperationActivity extends MyActivity implements AdapterView.OnItemClickListener {
    public static final String ACTION_REFRESH = "com.test.action.refresh";
    private ListView mListView;
    private OperationAdapter mAdapter;
    private OASubmit submit;
    private Map<String,String> params;
    private String submitType;
    private String isgzzb = "false";
    public static OperationActivity instance;
    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.oa_operetion_list;
    }

    @Override
    protected void initView() {
        super.initView();
        instance=OperationActivity.this;
        submitType=getIntent().getStringExtra("submitType");
        isgzzb = getIntent().getStringExtra("isgzzb");
        Bundle bundle = getIntent().getExtras();
        submit = bundle.getParcelable("submit");
        SeriMap sMap = (SeriMap) bundle.getSerializable("map");
        params = sMap.getMap();
        setTitle("下一步");
        mContext = OperationActivity.this;
        mListView = (ListView) findViewById(R.id.listView);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mListView.setOnItemClickListener(this);
        if(DeviceUtil.checkNet()){
            getOperationList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        NewOperation doc = mAdapter.getItem(position);
        Intent intent = new Intent(mContext, SubmitActivity.class);
        intent.putExtra("nodeId", doc.getNodeId());
        intent.putExtra("choose", doc.getChoose());
        intent.putExtra("title", doc.getNodeName());
        intent.putExtra("submitType",submitType);

        Bundle bundle = new Bundle();
        bundle.putParcelable("submit", submit);
        SeriMap map = new SeriMap();
        map.setMap(params);
        bundle.putSerializable("map", map);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 获取操作流程
     */
    public void getOperationList() {
        String url;
        if("true".equals(isgzzb))
            url = "apps/gzzb/queryOperation";
        else
            url = "apps/office/queryOperation";

        Map<String,Object> mObject = new HashMap<>();
        for (String key : params.keySet()) {
            System.out.println(key + "=" + params.get(key));
            mObject.put(key,params.get(key));
        }

        HttpUtil.getInstance().postJsonObjectRequest(url,mObject, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result != null){
                    List<NewOperation> mData = new ArrayList<NewOperation>();
                    try {
                        JSONArray array = result.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            NewOperation d = new NewOperation();
                            d.setNodeId(jo.getString("nodeId"));
                            d.setNodeName(jo.getString("nodeName"));
                            d.setChoose(jo.getString("choose"));
                            mData.add(d);
                        }
                        if(mData.size()>0){
                            mAdapter = new OperationAdapter(mContext, mData);
                            mListView.setAdapter(mAdapter);
                        }else{
                            mError.setErrorType(ErrorLayout.NODATA);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                }else{
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }
}
