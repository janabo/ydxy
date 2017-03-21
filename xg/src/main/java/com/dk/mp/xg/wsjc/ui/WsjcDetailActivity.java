package com.dk.mp.xg.wsjc.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 卫生检查打分页面
 * 作者：janabo on 2017/1/9 14:20
 */
public class WsjcDetailActivity extends MyActivity implements WsjcDetailAdapter.SaveEditListener,EasyPermissions.PermissionCallbacks{
    String uuid = UUID.randomUUID().toString();
    public static final String BASEPICPATH = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";
    ErrorLayout mError;
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
//    String dfxxId = "";//打分信息id（多个，隔开）
//    String df = "";//打分（多个，隔开，和dfxxId顺序对应）
    EditText bz_edit;
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
        if(wsjcDetail!= null){
            detail = new Gson().fromJson(wsjcDetail,WsjcDetail.class);
        }

        setDetailSetText(detail);
    }

    private void findView(){
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        ssqName = (TextView) findViewById(R.id.ssqName);
        sslName = (TextView) findViewById(R.id.sslName);
        lcName = (TextView) findViewById(R.id.lcName);
        fjhName = (TextView) findViewById(R.id.fjhName);
        rzxbName = (TextView) findViewById(R.id.rzxbName);
        cwsName = (TextView) findViewById(R.id.cwsName);
        bz_edit = (EditText) findViewById(R.id.bz_edit);

        mRootView = (ScrollView) findViewById(R.id.mRootView);
        mdfxx = (TextView) findViewById(R.id.dfxx_pick);
        ok_lin = (LinearLayout) findViewById(R.id.ok);
        ok = (TextView) findViewById(R.id.ok_text);
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
        ok.setEnabled(false);
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
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    /**
     * 获取打分项
     */
    public void getDfxx(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdf/dfxx", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    showErrorMsg(mRootView,getReString(R.string.data_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                showErrorMsg(mRootView,getReString(R.string.data_fail));
            }
        });
    }

    /**
     * 获取扣分原因
     */
    public void getKf(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdf/kf", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    showErrorMsg(mRootView,getReString(R.string.data_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
            overridePendingTransition(R.anim.push_up_in, 0);
        }else{
            getKf();
            if(mKfs.size()>0) {
                Intent intent = new Intent(mContext, WsjcDfyyPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) mKfs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
                overridePendingTransition(R.anim.push_up_in, 0);
            }else {
                showErrorMsg(mRootView, "未获取到扣分原因选项");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                if(resultCode == RESULT_OK) {
                    String kfs = data.getStringExtra("kfs");
                    String kdfid = data.getStringExtra("kfsid");
                    kfId = kdfid;
                    mdfxx.setText(kfs);
                }
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
            case 4:
                if (resultCode == RESULT_OK) {
                    if (StringUtils.isNotEmpty(noCutFilePath) && new File(noCutFilePath).exists()) {
                        imgs.remove(0);
                        imgs.add(noCutFilePath);
                        wImageAdapter.notifyDataSetChanged();
                    }
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
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
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
        noCutFilePath = BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /*获取当前系统的android版本号*/
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion<24){
            getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(noCutFilePath)));
        }else{
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, new File(noCutFilePath).getAbsolutePath());
            Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(getImageByCamera, 4);
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
        if(bz_edit.getText().toString().length()>200){
            showErrorMsg("备注不能大于200字");
            return;
        }
        ok.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        if(StringUtils.isNotEmpty(noCutFilePath)){
            File f = new File(noCutFilePath);
            if(f.exists() && f.isFile()){
                updateImg();
            }else{
                submit("");
            }
        }else{
            submit("");
        }
    }

    public void submit(String filename){
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
        params.put("fjName",filename);
        params.put("bz",bz_edit.getText().toString());
        params.put("id", uuid);
        params.put("zzdf",mScore);

        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdf/tjwz", params, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JsonData jd = getGson().fromJson(result.toString(),JsonData.class);
                    if (jd.getCode() == 200 && (Boolean) jd.getData()) {
                        mHandler.sendEmptyMessage(1);
                    }else{
                        showErrorMsg(mRootView,result.getString("msg"));
                        mHandler.sendEmptyMessage(-1);
//                        errorInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(-1);
//                    errorInfo();
                }
            }
            @Override
            public void onError(VolleyError error) {
//                errorInfo();
                mHandler.sendEmptyMessage(-1);
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

    /**
     * 成功
     */
    private void successInfo(){
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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://成功
                    successInfo();
                    break;
                case -1://失败
                    errorInfo();
                    break;
            }
        }
    };



    /**
     * 上传图片
     */
    public void updateImg(){
        LoginMsg loginMsg = getSharedPreferences().getLoginMsg();
        String mUrl = getReString(R.string.uploadUrl);
        if(loginMsg != null) {
            mUrl +="attachmentUpload.service?type=sswsjcAttachment&userId="+loginMsg.getUid()+"&password="+loginMsg.getPsw()+"&ownerId="+uuid;
        }else{
            mUrl +="attachmentUpload.service?type=sswsjcAttachment&ownerId="+uuid;
        }
        List<File> files = new ArrayList<>();
        files.add(new File(noCutFilePath));
        HttpUtil.getInstance().uploadImg(mUrl,files,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(-1);
                showErrorMsg("上传附件失败");
                call.cancel();// 上传失败取消请求释放内存
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Logger.info("######################result="+result);
                call.cancel();// 上传失败取消请求释放内存
                submit(uuid);
            }
        });
    }

    public void clearImg(){
        noCutFilePath="";
    }
}
