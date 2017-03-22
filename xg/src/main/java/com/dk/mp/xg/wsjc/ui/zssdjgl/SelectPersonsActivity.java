package com.dk.mp.xg.wsjc.ui.zssdjgl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.ZssdjglSelectPersonsAdapter;
import com.dk.mp.xg.wsjc.adapter.ZssglSelectClassAdapter;
import com.dk.mp.xg.wsjc.adapter.ZssglSelectRoomAdapter;
import com.dk.mp.xg.wsjc.entity.Class;
import com.dk.mp.xg.wsjc.entity.Room;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/8 11:22
 */
public class SelectPersonsActivity extends MyActivity implements View.OnClickListener,ZssglSelectClassAdapter.OnItemClickListener,ZssglSelectRoomAdapter.OnItemClickListener{
    private TextView back,ok,title;
    private RecyclerView ssry,fjh,bj;//宿舍成员，房间号，宿舍人员
    private ErrorLayout mError;
    private LinearLayout mRootView;
    private List<Class> classes = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Zssdjgl> persons = new ArrayList<>();
    private ZssglSelectClassAdapter cAdapter;
    private ZssglSelectRoomAdapter rAdapter;
    private ZssdjglSelectPersonsAdapter pAdapter;
    int selectBj = 0;//选择第几个
    int selectRoom = 0;//选择第几个
    int selectPerson = 0;//选择第几个
    private List<Zssdjgl> selectPersons = new ArrayList<>();
    private String type;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zssdjgl_select_persons;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.select_title));
        }
        mRootView = (LinearLayout) findViewById(R.id.layout_search);
        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.ok);
        title = (TextView) findViewById(R.id.title);
        ssry = (RecyclerView) findViewById(R.id.ssry);
        fjh = (RecyclerView) findViewById(R.id.fjh);
        bj = (RecyclerView) findViewById(R.id.bj);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        type = getIntent().getStringExtra("type");
        selectPersons.addAll((List<Zssdjgl>)getIntent().getSerializableExtra("persons"));
        title.setText("1".equals(type)?"住宿人员":"请假人员");
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                back();
            }
        });

        bj.setHasFixedSize ( true );
        bj.setLayoutManager ( new LinearLayoutManager( mContext ) );
        cAdapter = new ZssglSelectClassAdapter( mContext,classes,selectBj);
        bj.setAdapter ( cAdapter );
        bj.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        bj.setItemAnimator(new DefaultItemAnimator());

        fjh.setHasFixedSize ( true );
        fjh.setLayoutManager ( new LinearLayoutManager( mContext ) );
        rAdapter = new ZssglSelectRoomAdapter(mContext,rooms,selectRoom);
        fjh.setAdapter ( rAdapter );
        fjh.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        fjh.setItemAnimator(new DefaultItemAnimator());

        ssry.setHasFixedSize ( true );
        ssry.setLayoutManager ( new LinearLayoutManager( mContext ) );
        pAdapter = new ZssdjglSelectPersonsAdapter(mContext,persons,SelectPersonsActivity.this);
        ssry.setAdapter ( pAdapter );
        ssry.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        ssry.setItemAnimator(new DefaultItemAnimator());

        getData();

        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Map<String,Object> map =  pAdapter.getIsSelected();
                if(map.isEmpty()){
                    showErrorMsg(mRootView,"请选择人员");
                }else{
                    ArrayList<Zssdjgl> zssdjgls = new ArrayList<Zssdjgl>();
                    for(Map.Entry<String,Object> entry : map.entrySet()){
                        Zssdjgl p = (Zssdjgl) entry.getValue();
                        if(p != null && !"addperson".equals(p.getId())){
                            zssdjgls.add(p);
                        }
                    }
                    Intent in = new Intent();
                    in.putExtra("persons",(Serializable)zssdjgls);
                    setResult(RESULT_OK, in);
                    back();
                }
            }
        });

        for(Zssdjgl z : selectPersons){
            pAdapter.getIsSelected().put(z.getId(),z);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    /**
     * 获取数据
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            getList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getList(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsdjgl/users", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Class> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Class>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Class> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                int i=0;
                                for(Class c : dfxxes){
                                    if(StringUtils.isNotEmpty(c.getClassName())){
                                        classes.add(c);
                                        if(i == 0){
                                            rooms.addAll(c.getRooms());
                                            i++;
                                            int j=0;
                                            if(j == 0 && rooms.size()>0){
                                                persons.addAll(rooms.get(0).getXsxxs());
                                                j++;
                                            }
                                        }
                                        cAdapter.notifyDataSetChanged();
                                        rAdapter.notifyDataSetChanged();
                                        pAdapter.notifyDataSetChanged();
                                    }
                                }
                                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            }else{
                                mError.setErrorType(ErrorLayout.NODATA);
                            }
                        } else {
                            mError.setErrorType(ErrorLayout.DATAFAIL);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                } catch (Exception e) {
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

    /**
     * 处理确定按钮
     */
    public void dealOK(boolean flag){
        if(flag){
            ok.setEnabled(true);
            ok.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            ok.setEnabled(false);
            ok.setTextColor(getResources().getColor(R.color.colorPrimary50));
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        selectBj = position;
        cAdapter.setSelected(selectBj);
        cAdapter.notifyDataSetChanged();
        Class k = classes.get(position);
        rooms.clear();
        selectRoom = 0;
        rooms.addAll(k.getRooms());
        rAdapter.setSelected(selectRoom);
        rAdapter.notifyDataSetChanged();
        persons.clear();
        if (k.getRooms().size() > 0) {
           persons.addAll(k.getRooms().get(0).getXsxxs());
        }
        pAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickRoom(View view, int position) {
        selectRoom = position;
        rAdapter.setSelected(selectRoom);
        rAdapter.notifyDataSetChanged();
        persons.clear();
        persons.addAll(rooms.get(position).getXsxxs());
        pAdapter.notifyDataSetChanged();
    }

    @Override
    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
