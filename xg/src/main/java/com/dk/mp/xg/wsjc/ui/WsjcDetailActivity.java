package com.dk.mp.xg.wsjc.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.WsjcDetailAdapter;
import com.dk.mp.xg.wsjc.adapter.WsjcImageAdapter;
import com.dk.mp.xg.wsjc.entity.Dfxx;
import com.dk.mp.xg.wsjc.entity.Kf;
import com.dk.mp.xg.wsjc.entity.WsjcDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.dk.mp.xg.R.id.ok_text;


/**
 * 卫生检查打分页面
 * 作者：janabo on 2017/1/9 14:20
 */
public class WsjcDetailActivity extends MyActivity implements WsjcDetailAdapter.SaveEditListener,EasyPermissions.PermissionCallbacks{
    private TextView ssqName,sslName,lcName,fjhName,rzxbName,cwsName;//宿舍区，宿舍楼，楼层，房间号，入住性别，床位数
    DrawHookView progress;//提交动画
    DrawCheckMarkView progress_check;//成功动画
    DrawCrossMarkView progress_cross;//错误动画
    private static final int WRITE_RERD = 1;
    private String noCutFilePath ="";
    ScrollView mRootView;
    WsjcDetailAdapter mAdapter;
    private RecyclerView mRecyclerView;//打分信息list
    List<Dfxx> mData = new ArrayList<>();
    List<Kf> mKfs = new ArrayList<>();
    TextView mdfxx;//扣分原因
    TextView ok;//提交按钮
    LinearLayout ok_lin;
    int mScore = 0;//总分
    private RecyclerView gRecyclerView;//图片
    List<String> imgs = new ArrayList<>();//保存图片地址
    WsjcImageAdapter wImageAdapter;
    WsjcDetail detail = null;
    Map<String,Integer> map = new HashMap<>();//存储打分信息
    String ssq = "";//宿舍区id
    String ssl = "";//宿舍楼id
    String lc = "";//楼层
    String fjh = "";//房间号id
    String kfId = "";//扣分id
    String dfxxId = "";//打分信息id（多个，隔开）
    String df = "";//打分（多个，隔开，和dfxxId顺序对应）
    String bz = "";//备注
    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjc_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("宿舍卫生检查");
        findView();
        String wsjcDetail = getIntent().getStringExtra("wsjcDetail");
        detail = new Gson().fromJson(wsjcDetail,WsjcDetail.class);
        setDetailSetText(detail);
    }

    private void findView(){
        ssqName = (TextView) findViewById(R.id.ssqName);
        sslName = (TextView) findViewById(R.id.sslName);
        lcName = (TextView) findViewById(R.id.lcName);
        fjhName = (TextView) findViewById(R.id.fjhName);
        rzxbName = (TextView) findViewById(R.id.rzxbName);
        cwsName = (TextView) findViewById(R.id.cwsName);

        mRootView = (ScrollView) findViewById(R.id.mRootView);
        mdfxx = (TextView) findViewById(R.id.dfxx_pick);
        ok_lin = (LinearLayout) findViewById(R.id.ok);
        ok = (TextView) findViewById(ok_text);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
        mRecyclerView = (RecyclerView) findViewById(R.id.ssdfxx);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new WsjcDetailAdapter ( mContext,mData);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecyclerView.setNestedScrollingEnabled(false);
        getData();
        imgs.add("addImage");
        gRecyclerView = (RecyclerView) findViewById(R.id.imgView);
        gRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        wImageAdapter = new WsjcImageAdapter(mContext,WsjcDetailActivity.this,imgs);
        gRecyclerView.setAdapter(wImageAdapter);

    }

    public void setDetailSetText(WsjcDetail d){
        if(d != null) {
            ssqName.setText(d.getSsqName());
            sslName.setText(d.getSslName());
            lcName.setText(d.getLcId());
            fjhName.setText(d.getFjh());
            rzxbName.setText(d.getXb());
            cwsName.setText(d.getCws());
            ssq = d.getSsqId();
            ssl = d.getSslId();
            lc = d.getLcId();
            fjh = d.getFjhId();
        }
    }

    public void getData(){
        if(DeviceUtil.checkNet()){
            getDfxx();
            getKf();
        }else{
            SnackBarUtil.showShort(mRootView,getReString(R.string.net_no2));
        }
    }

    /**
     * 获取打分项
     */
    public void getDfxx(){
        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.163:8082/mp-lgj/apps/sswzdf/dfxx", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Dfxx> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Dfxx>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Dfxx> dfxxes = gsonData.getData();
                            int sumScore = 0;
                            if(dfxxes.size()>0){//获取数据不为空
                                mData.addAll(dfxxes);
                                mAdapter.notifyDataSetChanged();
                                for(Dfxx k : dfxxes){
                                    map.put(k.getId(),Integer.parseInt(k.getFs()));
                                    sumScore += Integer.parseInt(k.getFs());//计算总分值
                                }
                                mScore = sumScore;
                                setOkScore(sumScore+"");
                            }else{
                                showErrorMsg(mRootView,"未获取到打分信息");
                            }
                        } else {
                            showErrorMsg(mRootView,getReString(R.string.data_fail));
                        }
                    }else{
                        showErrorMsg(mRootView,getReString(R.string.data_fail));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg(mRootView,getReString(R.string.data_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
                showErrorMsg(mRootView,getReString(R.string.data_fail));
            }
        });
    }

    /**
     * 获取扣分原因
     */
    public void getKf(){
        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.163:8082/mp-lgj/apps/sswzdf/kf", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Kf> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Kf>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Kf> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){
                                mKfs.addAll(dfxxes);
                                mAdapter.notifyDataSetChanged();
                            }else{
                                showErrorMsg(mRootView,"未获取到扣分原因");
                            }
                        } else {
                            showErrorMsg(mRootView,getReString(R.string.data_fail));
                        }
                    }else{
                        showErrorMsg(mRootView,getReString(R.string.data_fail));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg(mRootView,getReString(R.string.data_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
                showErrorMsg(mRootView,getReString(R.string.data_fail));
            }
        });
    }

    /**
     * 选择扣分原因
     * @param v
     */
    public void toPickKfyy(View v){
        if(mKfs.size()>0) {
            Intent intent = new Intent(mContext, WsjcDfyyPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) mKfs);
            intent.putExtras(bundle);
            startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        }else{
            showErrorMsg(mRootView,"未获取到扣分原因选项");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                String kfs = data.getStringExtra("kfs");
                String kdfid = data.getStringExtra("kfsid");
                kfId = kdfid;
                mdfxx.setText(kfs);
                break;
            case 5://相册选取不 剪切
                if (data != null) {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    Cursor c = cr.query(uri, null, null, null, null);
                    if (c.moveToNext()) {
                        noCutFilePath=c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    c.close();
                    if(StringUtils.isNotEmpty(noCutFilePath)) {
                        imgs.remove(0);
                        imgs.add(noCutFilePath);
                    }
                    wImageAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /**
     * 设置提交按钮上面的分数
     */
    public void setOkScore(String score){
        ok.setText("提交最终分值（"+score+"）分");
    }

    @Override
    public void SaveEdit(String dfid,int score) {
        mScore = mScore+score;
        setOkScore(mScore+"");
        map.put(dfid,map.get(dfid)+score);//各打分项 分数
    }


    @AfterPermissionGranted(WRITE_RERD)
    public void startReadWi(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            try {
                ablum();
            } catch (Exception e) {
                showErrorMsg(mRootView,"请正确授权");
            }
        } else {
            EasyPermissions.requestPermissions(this,
                    "请求读写权限",
                    WRITE_RERD, perms);
        }
    }

    public void ablum(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 5);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            if(requestCode == WRITE_RERD) {
                ablum();
            }
        } catch (Exception e) {
            showErrorMsg(mRootView,"请正确授权");
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        showErrorMsg(mRootView,"请正确授权");
    }

    /**
     * 提交宿舍卫生检查情况
     * @param v
     */
    public void submitWs(View v){
        ok_lin.setEnabled(false);//设置不能点击
        StringBuffer dfxx = new StringBuffer();
        StringBuffer dffs = new StringBuffer();
        int i =0;
        for(Map.Entry<String,Integer> entry:map.entrySet()){//拼装打分信息
            if(i==0){
                dfxx.append(entry.getKey());
                dffs.append(entry.getValue());
            }else{
                dfxx.append(","+entry.getKey());
                dffs.append(","+entry.getValue());
            }
            i++;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("ssq",ssq);
        params.put("ssl",ssl);
        params.put("lc",lc);
        params.put("fjh",fjh);
        params.put("kfId",kfId);
        params.put("dfxxId",dfxx.toString());
        params.put("df",dffs.toString());
        params.put("fjName","");
        params.put("bz",bz);
        params.put("Id", UUID.randomUUID().toString());
        params.put("zzdf",mScore);

        ok.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.163:8082/mp-lgj/apps/sswzdf/tjwz", params, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getInt("code") != 200) {
                        showErrorMsg(mRootView,result.getString("msg"));
                        errorInfo();
                    }else{
                        progress.setVisibility(View.GONE);
                        progress_check.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {//等待成功动画结束
                            @Override
                            public void run() {
                                ok_lin.setEnabled(true);
                                back();
                            }
                        },2000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorInfo();
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorInfo();
            }
        });
    }

    /**
     * 错误提示和动画
     */
    private void errorInfo(){
        progress.setVisibility(View.GONE);
        progress_cross.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {//等待成功动画结束
            @Override
            public void run() {
                progress_cross.setVisibility(View.GONE);
                ok.setVisibility(View.VISIBLE);
                ok_lin.setEnabled(true);
            }
        },1000);
    }
}
