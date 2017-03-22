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
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.adapter.PeopleAdapter;
import com.dk.mp.newoa.entity.People;
import com.dk.mp.newoa.entity.SeriMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/3/22 14:34
 */
public class PeopleActivity extends MyActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private List<People> mList;
    private PeopleAdapter mAdapter;
    private String userIds = "", userNames = "",operP ="";
    private String choose;
    private Map<String, String> params;
    private Map<String,String> operMap = new HashMap<String,String>();
    private String isgzzb="false";//是否是工作周报，true是
    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.oa_people;
    }

    @Override
    protected void initView() {
        super.initView();
        isgzzb = getIntent().getStringExtra("isgzzb");
        Bundle bundle = getIntent().getExtras();
        choose = getIntent().getStringExtra("choose");
        operP = getIntent().getStringExtra("operP");
        if(StringUtils.isNotEmpty(operP)){
            String[] str = operP.split(",");
            for(int i = 0;i<str.length;i++){
                operMap.put(str[i], str[i]);
            }
        }
        SeriMap sMap = (SeriMap) bundle.getSerializable("map");
        params = sMap.getMap();
        if(params !=null && StringUtils.isNotEmpty(params.get("choose"))){//判断联系人是多选还是单选
            choose = params.get("choose");
        }
        setTitle("联系人");
        mContext = PeopleActivity.this;
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        if(DeviceUtil.checkNet()){
            getOperationList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
        setRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> list = mAdapter.getChecked();
                if(list.size()>0){
                    operP = "";
                    for (int i = 0; i < list.size(); i++) {
                        People people = mList.get(list.get(i));
                        if (i == 0) {
                            userIds += people.getId();
                            userNames += people.getName();
                            operP += people.getTitle()+"&&"+people.getId();
                        } else {
                            userIds += "," + people.getId();
                            userNames += "," + people.getName();
                            operP += "," + people.getTitle()+"&&"+people.getId();
                        }
                    }
                    Intent in = new Intent();
                    in.putExtra("userIds", userIds);
                    in.putExtra("userNames", userNames);
                    in.putExtra("operP", operP);
                    setResult(RESULT_OK, in);
                    back();
                }else{
                    showErrorMsg("请选择联系人");
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Logger.info("position=" + position);
        mAdapter.getOperP().clear();
        PeopleAdapter.MyView holder = (PeopleAdapter.MyView) view.getTag();
        if ("1".equals(choose)) {
            mAdapter.clean();
        }
        if (holder.getCheckBox().isChecked()) {
            holder.getCheckBox().setChecked(false);
        } else {
            holder.getCheckBox().setChecked(true);
        }
        mAdapter.getIsSelected().put(position, holder.getCheckBox().isChecked());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取用户
     */
    public void getOperationList() {
        String url;
        if("true".equals(isgzzb)){
            url = "apps/gzzb/queryUser";
        }else{
            url = "apps/office/queryUser";
        }
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
                        mList = new ArrayList<People>();
                        JSONArray array = result.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONArray jo = array.getJSONObject(i).getJSONArray("user");
                            String departname = array.getJSONObject(i).getString("depart");
                            for (int j = 0; j < jo.length(); j++) {
                                JSONObject pJson = jo.getJSONObject(j);
                                People p = new People();
                                p.setId(pJson.getString("userId"));
                                p.setName(pJson.getString("userName"));
                                p.setTitle(departname);
                                mList.add(p);
                            }
                        }
                        if(mList == null || mList.size()<=0){
                            mError.setErrorType(ErrorLayout.NODATA);
                        }else{
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            mAdapter = new PeopleAdapter(mContext, mList,operMap);
                            mListView.setAdapter(mAdapter);
                        }
                    }catch (Exception e) {
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

    @Override
    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }
}
