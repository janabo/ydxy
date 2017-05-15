package com.dk.mp.xg.wsjc.ui.Sswz;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.entity.ResultCode;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.SswzImageAdapter;
import com.dk.mp.xg.wsjc.adapter.SswzImageAdapter2;
import com.dk.mp.xg.wsjc.adapter.SswzSdluPersonsAdapter;
import com.dk.mp.xg.wsjc.adapter.ZssdjglPersonsAdapter;
import com.dk.mp.xg.wsjc.entity.Common;
import com.dk.mp.xg.wsjc.entity.Student;
import com.dk.mp.xg.wsjc.entity.WsjcDetail;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.dk.mp.xg.wsjc.ui.zssdjgl.SelectPersonsActivity;
import com.dk.mp.xg.wsjc.ui.zssdjgl.ZssdjglAddLiusuActivity;
import com.dk.mp.xg.wsjc.ui.zssgl.ZssglSelectPersonsActivity;
import com.dk.mp.xg.wsjc.ui.zssgl.ZsstjPickActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
 * Created by cobb on 2017/4/17.
 */

public class SswzSdluActivity extends MyActivity implements EasyPermissions.PermissionCallbacks,View.OnClickListener, SswzSdluPersonsAdapter.OnItemClickListener {

    private static final int INIT_GETDATA = 1;
    private static final int PICK_GETDATA = 2;

    private DrawHookView progress;
    private DrawCheckMarkView progress_check;
    private DrawCrossMarkView progress_cross;

    private LinearLayout ok_lin;
    private TextView ok;//提交按钮

    private ErrorLayout mError;
    private ScrollView mRootView;

    private TextView xq,ssq,ssl,lc,fjh,kfyy;//校区,宿舍区,宿舍楼,楼层,房间号,
    private TextView xq_pick,ssq_pick,ssl_pick,lc_pick,fjh_pick,kfyy_pick;
    private String xqid,ssqid,sslid,lcid,fjhid;//校区id,宿舍区,宿舍楼id,楼层id,

    private List<Common> xqs = new ArrayList<>();//存放校区信息
    private List<Common> ssqs = new ArrayList<>();//存放宿舍区信息
    private List<Common> ssls = new ArrayList<>();//存放宿舍楼信息
    private List<Common> lcs = new ArrayList<>();//存放楼层信息
    private List<Common> fjhs = new ArrayList<>();//存放房间号信息

    private String wjxsid,wjlbid,tbrid;//违纪学生,违纪类别
    private TextView wjxs_x2,wjxs_name2;
    private ImageView wjxs_img2;
    private LinearLayout wjxs_lin2;

    private RecyclerView mRecyclerView;
    private SswzSdluPersonsAdapter zAdapter;
    private List<Zssdjgl> persons = new ArrayList<>();

    private TextView wjrq, wjlb_txt;
    private LinearLayout wjrq_lin,wjlb_lin;//违纪日期,违纪类别
    List<Common> wjlbs = new ArrayList<>();//违纪类别

    public static final String BASEPICPATH = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";
    private RecyclerView gRecyclerView;//图片
    List<String> imgs = new ArrayList<>();//保存图片地址
    SswzImageAdapter2 wImageAdapter;
    private String noCutFilePath ="";
    private static final int WRITE_RERD = 1;
    private String uuid = UUID.randomUUID().toString();

    private TextView bz, dhbh;

    @Override
    protected int getLayoutID() {
        return R.layout.app_ssww_sdlr;
    }

    @Override
    public void back() {
        super.back();

        onBackPressed();
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitle("宿舍违纪登记");

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        finId();

        getWjlbs();

    }

    private void finId() {

        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mRootView = (ScrollView) findViewById(R.id.mRootView);

        ok_lin = (LinearLayout) findViewById(R.id.ok);
        ok = (TextView) findViewById(R.id.ok_text);
        ok_lin.setEnabled(false);

        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);

        xq_pick= (TextView) findViewById(R.id.xq_pick);
        ssq_pick = (TextView) findViewById(R.id.ssq_pick);
        ssl_pick = (TextView) findViewById(R.id.ssl_pick);
        lc_pick = (TextView) findViewById(R.id.lc_pick);
        fjh_pick = (TextView) findViewById(R.id.fjh_pick);

//        wjxs_x = (TextView) findViewById(R.id.wjxs_x);
//        wjxs_name= (TextView) findViewById(R.id.wjxs_name);
//        wjxs_img = (ImageView) findViewById(R.id.wjxs_img);
//        wjxs_lin = (LinearLayout) findViewById(R.id.wjxs_lin);
        mRecyclerView  = (RecyclerView) findViewById(R.id.person_recycle);
        persons.add(new Zssdjgl("addperson",""));
        zAdapter = new SswzSdluPersonsAdapter(persons,mContext,SswzSdluActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(zAdapter);

        wjxs_x2 = (TextView) findViewById(R.id.wjxs_x2);
        wjxs_name2= (TextView) findViewById(R.id.wjxs_name2);
        wjxs_img2 = (ImageView) findViewById(R.id.wjxs_img2);
        wjxs_lin2 = (LinearLayout) findViewById(R.id.wjxs_lin2);

        //wjxs_lin.setOnClickListener(this);
        wjxs_lin2.setOnClickListener(this);

        wjrq_lin = (LinearLayout) findViewById(R.id.wjrq_lin);
        wjlb_lin = (LinearLayout) findViewById(R.id.wjlb_lin);
        wjrq = (TextView) findViewById(R.id.wjrq);
        wjlb_txt= (TextView) findViewById(R.id.wjlb_txt);

        wjrq_lin.setOnClickListener(this);
        wjlb_lin.setOnClickListener(this);

        imgs.add("addImage");
        gRecyclerView = (RecyclerView) findViewById(R.id.imgView);
        gRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.HORIZONTAL));
        wImageAdapter = new SswzImageAdapter2(mContext,SswzSdluActivity.this,imgs);
        gRecyclerView.setAdapter(wImageAdapter);

        bz = (TextView) findViewById(R.id.bz);
        dhbh = (TextView) findViewById(R.id.dhbh);

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
        File appDir = new File(BASEPICPATH);
        if(!appDir.exists()){
            appDir.mkdir();
        }
        noCutFilePath = BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
        File file = new File(noCutFilePath);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /*获取当前系统的android版本号*/
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion<24){
            getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }else{
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
//        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
//        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(noCutFilePath)));
        startActivityForResult(getImageByCamera, 10);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://选择违纪学生
                if (resultCode == RESULT_OK) {
                    persons.clear();
                    persons.addAll((List<Zssdjgl>)data.getSerializableExtra("persons"));
                    persons.add(new Zssdjgl("addperson",""));
                    zAdapter.notifyDataSetChanged();
                    dealOkButton();
//                    wjxsid = data.getStringExtra("userIds");
//                    String userName = data.getStringExtra("userNames");
//                    if(userName.length()>0){
//                        wjxs_name.setText(userName);
//                        wjxs_x.setText(userName.substring(0,1));
//                        getBackgroud(wjxs_lin,userName.substring(0,1));
//                    }
//                    wjxs_lin.setVisibility(View.VISIBLE);
//                    wjxs_name.setVisibility(View.VISIBLE);
//                    wjxs_img.setVisibility(View.GONE);
//
//                    dealOkButton();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String mXqid = data.getStringExtra("kfsid");
                    String xqname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(xqid) && !xqid.equals(mXqid)){
                        ssqid ="";sslid="";lcid="";fjhid="";
                        ssq_pick.setText("");ssl_pick.setText("");lc_pick.setText("");
                        fjh_pick.setText("");
                    }
                    xqid = mXqid;
                    xq_pick.setText(xqname);
                    getSsqs(xqid,INIT_GETDATA);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    String mSsqid = data.getStringExtra("kfsid");
                    String ssqname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(ssqid) && !ssqid.equals(mSsqid)){
                        sslid="";lcid="";fjhid="";
                        ssl_pick.setText("");lc_pick.setText("");
                        fjh_pick.setText("");
                    }
                    ssqid = mSsqid;
                    ssq_pick.setText(ssqname);
                    getSsls(ssqid,INIT_GETDATA);
//                    getTsyys(ssqid);
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    String mSslid = data.getStringExtra("kfsid");
                    String sslname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(sslid) && !ssqid.equals(mSslid)){
                        lcid="";fjhid="";
                        lc_pick.setText("");
                        fjh_pick.setText("");
                    }
                    sslid = mSslid;
                    ssl_pick.setText(sslname);
                    getLcs(sslid,INIT_GETDATA);
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    String mLcid = data.getStringExtra("kfsid");
                    String lcname = data.getStringExtra("kfs");
                    if(StringUtils.isNotEmpty(lcid) && !ssqid.equals(mLcid)){
                        fjhid="";
                        fjh_pick.setText("");
                    }
                    lcid = mLcid;
                    lc_pick.setText(lcname);
                    getFjhs(sslid, lcid,INIT_GETDATA);

                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    String fjhname = data.getStringExtra("kfs");
                    String mfjhid = data.getStringExtra("kfsid");
                    fjh_pick.setText(fjhname);
                    fjhid = mfjhid;

                    dealOkButton();
                }
                break;
            case 7://提报人
                if (resultCode == RESULT_OK) {
                    tbrid = data.getStringExtra("userIds");
                    String userName = data.getStringExtra("userNames");
                    if(userName.length()>0){
                        wjxs_name2.setText(userName);
                        wjxs_x2.setText(userName.substring(0,1));
                        getBackgroud(wjxs_lin2,userName.substring(0,1));
                    }
                    wjxs_lin2.setVisibility(View.VISIBLE);
                    wjxs_name2.setVisibility(View.VISIBLE);
                    wjxs_img2.setVisibility(View.GONE);

                    dealOkButton();
                }
                break;
            case 8://违纪日期
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    wjrq.setText(mWjrq);
                    dealOkButton();
                }
                break;
            case 9://违纪类别
                if(resultCode == RESULT_OK){
                    String kfs = data.getStringExtra("kfs");
                    wjlbid = data.getStringExtra("kfsid");
                    wjlb_txt.setText(kfs);
                    dealOkButton();
                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    if (StringUtils.isNotEmpty(noCutFilePath) && new File(noCutFilePath).exists()) {
                        Bitmap bitmap = createThumbnail(noCutFilePath);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(new File(noCutFilePath));
                            bitmap.compress(Bitmap.CompressFormat.JPEG,30,fos);// 把数据写入文件
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        imgs.remove(0);
                        imgs.add(noCutFilePath);
                        wImageAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    /**
     * 设置值
     */
    public void setText(Student s){
        xq.setText(s.getXq());
        ssq.setText(s.getSsq());
        ssl.setText(s.getSsl());
        lc.setText(s.getLc());
        fjh.setText(s.getFjh());
    }

    /**
     * 获取校区列表
     */
    public void getXqs(final int param){
        xqs.clear();
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/xqList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                xqs.addAll(dfxxes);
                                toPickActivity(xqs,2,param);
                            }else{
                                errorMsg(param,"获取校区为空");
                            }
                        } else {
                            errorMsg(param,"获取校区为空");
                        }
                    }else{
                        errorMsg(param,"获取校区为空");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取校区为空");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取校区为空");
            }
        });
    }

    /**
     * 获取宿舍区列表
     */
    public void getSsqs(String mXqid, final int param){
        ssqs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("xq",mXqid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/ssqList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                ssqs.addAll(dfxxes);
                                toPickActivity(ssqs,3,param);
                            }else{
                                errorMsg(param,"获取宿舍区为空");
                            }
                        } else {
                            errorMsg(param,"获取宿舍区为空");
                        }
                    }else{
                        errorMsg(param,"获取宿舍区为空");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取宿舍区为空");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取宿舍区为空");
            }
        });
    }

    /**
     * 获取宿舍楼列表
     */
    public void getSsls(String mSsqid, final int param){
        ssls.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssq",mSsqid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/sslList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                ssls.addAll(dfxxes);
                                toPickActivity(ssls,4,param);
                            }else{
                                errorMsg(param,"获取宿舍楼为空");
                            }
                        } else {
                            errorMsg(param,"获取宿舍楼为空");
                        }
                    }else{
                        errorMsg(param,"获取宿舍楼为空");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取宿舍楼为空");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取宿舍楼为空");
            }
        });
    }

    /**
     * 获取楼层列表
     */
    public void getLcs(String sslid,final int param){
        lcs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssl",sslid);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/lcList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                lcs.addAll(dfxxes);
                                toPickActivity(lcs,5,param);
                            }else{
                                errorMsg(param,"获取楼层为空");
                            }
                        } else {
                            errorMsg(param,"获取楼层为空");
                        }
                    }else{
                        errorMsg(param,"获取楼层为空");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取楼层为空");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取楼层为空");
            }
        });
    }

    /**
     * 获取房间号列表
     */
    public void getFjhs(String ssl,String lc,final int param){
        fjhs.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("ssl",ssl);
        map.put("lc",lc);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgl/roomList", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                fjhs.addAll(dfxxes);
                                toPickActivity(fjhs,6,param);
                            }else{
                                errorMsg(param,"获取房间号为空");
                            }
                        } else {
                            errorMsg(param,"获取房间号为空");
                        }
                    }else{
                        errorMsg(param,"获取房间号为空");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg(param,"获取房间号为空");
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorMsg(param,"获取房间号为空");
            }
        });
    }

    /**
     * 获取违纪类别
     */
    public void getWjlbs(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdj/wjlb", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                wjlbs.addAll(dfxxes);
                            }else{
                                showErrorMsg(mRootView,"未获取到违纪类别");
                            }
                        } else {
                            showErrorMsg(mRootView,"未获取到违纪类别");
                        }
                    }else{
                        showErrorMsg(mRootView,"未获取到违纪类别");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg(mRootView,"获取违纪类别失败");
                }
            }

            @Override
            public void onError(VolleyError error) {
                showErrorMsg(mRootView,"获取违纪类别失败");
            }
        });
    }

    /**
     * 选择校区
     * @param v
     */
    public void toPickXq(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (xqs.size() > 0) {//初始化获取到校区就无须再请求
            toPickActivity(xqs,2,PICK_GETDATA);
        } else {
            if(DeviceUtil.checkNet()) {
                getXqs(PICK_GETDATA);
            }else{
                errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
            }
        }
    }

    /**
     * 选择宿舍区
     * @param v
     */
    public void toPickSsq(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (ssqs.size() > 0) {//初始化获取到校区就无须再请求
            toPickActivity(ssqs,3,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(xqid)) {
                if(DeviceUtil.checkNet()) {
                    getSsqs(xqid, PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择校区");
            }
        }
    }

    /**
     * 选择宿舍楼
     * @param v
     */
    public void toPickSsl(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (ssls.size() > 0) {
            toPickActivity(ssls,4,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(ssqid)) {
                if(DeviceUtil.checkNet()) {
                    getSsls(ssqid,PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择宿舍区");
            }
        }
    }

    /**
     * 选择楼层
     * @param v
     */
    public void toPickLc(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (lcs.size() > 0) {
            toPickActivity(lcs,5,PICK_GETDATA);
        } else {
            if(StringUtils.isNotEmpty(sslid)) {
                if(DeviceUtil.checkNet()) {
                    getLcs(sslid,PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }else{
                errorMsg(PICK_GETDATA,"请先选择宿舍楼");
            }
        }
    }

    /**
     * 选择房间号
     * @param v
     */
    public void toPickFjh(View v){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (fjhs.size() > 0) {
            toPickActivity(fjhs,6,PICK_GETDATA);
        } else {
//            if(!StringUtils.isNotEmpty(xb.getText().toString())){
//                errorMsg(PICK_GETDATA, "请先选择提名学生");
//            }else
            if(!StringUtils.isNotEmpty(sslid)){
                errorMsg(PICK_GETDATA,"请先选择宿舍楼");
            }else if(!StringUtils.isNotEmpty(lcid)){
                errorMsg(PICK_GETDATA,"请先选择楼层");
            }else {
                if(DeviceUtil.checkNet()) {
                    getFjhs(sslid, lcid, PICK_GETDATA);
                }else{
                    errorMsg(PICK_GETDATA,getReString(R.string.net_no2));
                }
            }
        }
    }

    /**
     * 展示错误信息
     * @param param
     * @param msg
     */
    private void errorMsg(int param,String msg){
        if(param == PICK_GETDATA){
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            showErrorMsg(mRootView, msg);
        }
    }

    /**
     * 跳转到选择页面
     * @param picks
     * @param result
     */
    public void toPickActivity(List<Common> picks,int result,int param){
        if(param == PICK_GETDATA) {
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            Intent intent = new Intent(mContext, ZsstjPickActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("kfs", (Serializable) picks);
            intent.putExtras(bundle);
            startActivityForResult(intent, result);
            overridePendingTransition(R.anim.push_up_in, 0);
        }
    }

    /**
     * 违纪学生
     * @param view
     */
    @Override
    public void onItemClick(View view, int position) {
        if (StringUtils.isNotEmpty(fjhid)){
            if ("addperson".equals(persons.get(position).getId())){
                Intent intent = new Intent(mContext, SswzSelectPersonsActivity2.class);
                intent.putExtra("fjhid",fjhid);
                intent.putExtra("persons",(Serializable)persons);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }else {
                persons.remove(position);
                zAdapter.notifyDataSetChanged();
                dealOkButton();
            }

        }else {
            errorMsg(PICK_GETDATA,"请先选择房间号");
        }

    }

    /**
     * 提名学生
     * @param view
     */
    public void addWjxs2(View view){

        Intent intent = new Intent(mContext, SswzSelectPersonActivity.class);
        intent.putExtra("type","tbr");
        intent.putExtra("fjhid",fjhid);
        startActivityForResult(intent, 7);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

    }

    /**
     * 设置ok按钮的样式
     */
    public void dealOkButton(){
        if(wjrq.getText().toString().length()>0 && wjlb_txt.getText().toString().length()>0 && StringUtils.isNotEmpty(tbrid)){
            ok_lin.setBackground(getResources().getDrawable(R.drawable.ripple_bg));
            ok_lin.setEnabled(true);
        }else{
            ok_lin.setBackgroundColor(getResources().getColor(R.color.rcap_gray));
            ok_lin.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
//        if(R.id.wjxs_lin == view.getId()){//违纪学生
//            wjxs_lin.setVisibility(View.GONE);
//            wjxs_name.setVisibility(View.GONE);
//            wjxs_img.setVisibility(View.VISIBLE);
//        }else
        if(R.id.wjxs_lin2 == view.getId()){//违纪学生
            wjxs_lin2.setVisibility(View.GONE);
            wjxs_name2.setVisibility(View.GONE);
            wjxs_img2.setVisibility(View.VISIBLE);
        }else if(R.id.wjrq_lin == view.getId()){//选择日期
            Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
            startActivityForResult(intent, 8);
            overridePendingTransition(R.anim.push_up_in, 0);
        }else if(R.id.wjlb_lin == view.getId()){//违纪类别
            if(wjlbs.size()>0) {
                Intent intent = new Intent(mContext, SswzWjlbPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) wjlbs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 9);
                overridePendingTransition(R.anim.push_up_in, 0);
            }else{
                getWjlbs();
                if(wjlbs.size()>0) {
                    Intent intent = new Intent(mContext, SswzWjlbPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("kfs", (Serializable) wjlbs);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 9);
                    overridePendingTransition(R.anim.push_up_in, 0);
                }else {
                    showErrorMsg(mRootView, "未获取到违纪类别信息");
                }
            }


        }

    }

    public void clearImg(){
        noCutFilePath="";
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
     * 成功
     */
    private void successInfo(){
        progress.setVisibility(View.GONE);
        progress_check.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {//等待成功动画结束
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                ok_lin.setEnabled(true);
                back();
            }
        },2000);
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
     * 提交宿舍违章
     * @param v
     */
    public void submitSswz(View v){
        ok_lin.setEnabled(false);//设置不能点击
        if(bz.getText().toString().length()>200){
            showErrorMsg("备注不能大于200字");
            return;
        }
        ok.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        if(StringUtils.isNotEmpty(noCutFilePath)){
            if(StringUtils.isNotEmpty(noCutFilePath)) {
                File f = new File(noCutFilePath);
                if (f.exists() && f.isFile()) {
                    updateImg();
                } else {
                    submit("");
                }
            }
        }else{
            submit("");
        }
    }

    public void submit(String filename){

        String users = "";
        for(Zssdjgl p : persons){
            if ("addperson".equals(p.getId())){
                continue;
            }
            users += p.getId() + ",";
        }

        Map<String,Object> params = new HashMap<>();
        params.put("id", uuid);
        params.put("xq",xqid);
        params.put("ssq",ssqid);
        params.put("ssl",sslid);
        params.put("fjh",fjhid);
        params.put("wjxs",users);
        params.put("wjrq",wjrq.getText().toString()+" 00:00:00");
        params.put("wjdhbh",uuid);
        params.put("wjlb",wjlbid);
        params.put("tbr",tbrid);
        params.put("fjName",filename);
        params.put("bz",bz.getText().toString());

        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdj/tjwz", params, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getInt("code") != 200) {
                        showErrorMsg(mRootView,result.getString("msg"));
//                        errorInfo();
                        mHandler.sendEmptyMessage(-1);
                    }else{
                        mHandler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    errorInfo();
                    mHandler.sendEmptyMessage(-1);
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
     * 上传图片
     */
    public void updateImg(){
        LoginMsg loginMsg = getSharedPreferences().getLoginMsg();
        String mUrl = getReString(R.string.uploadUrl);
        if(loginMsg != null) {
            mUrl +="/independent.service?.lm=ssgl-dwjk&.ms=view&action=fjscjk&.ir=true&type=sswzdjAttachment&userId="+loginMsg.getUid()+"&password="+ loginMsg.getEncpsw() +"&ownerId=sswzdjAttachment"+uuid;
//            mUrl +="attachmentUpload.service?type=sswzdjAttachment&userId="+loginMsg.getUid()+"&password="+ loginMsg.getEncpsw()+"&ownerId="+uuid;
        }else{
//            mUrl +="attachmentUpload.service?type=sswzdjAttachment&ownerId="+uuid;
            mUrl +="/independent.service?.lm=ssgl-dwjk&.ms=view&action=fjscjk&.ir=true&type=sswzdjAttachment&ownerId=sswzdjAttachment"+uuid;
        }
        List<File> files = new ArrayList<>();
        files.add(new File(noCutFilePath));
        HttpUtil.getInstance().uploadImg(mUrl,files,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
//                errorInfo();
                mHandler.sendEmptyMessage(-1);
                showErrorMsg("上传附件失败");
                call.cancel();// 上传失败取消请求释放内存
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200 ){
                    String result = response.body().string();
                    Logger.info("######################result="+result);
                    if(StringUtils.isNotEmpty(result)){
                        ResultCode rcode = getGson().fromJson(result,ResultCode.class);
                        if(rcode.getCode() == 200) {
                            call.cancel();// 上传失败取消请求释放内存
                            submit(uuid);
                        }else{
                            mHandler.sendEmptyMessage(-1);
                            showErrorMsg(rcode.getMsg());
                            call.cancel();// 上传失败取消请求释放内存
                        }
                    }else{
                        mHandler.sendEmptyMessage(-1);
                        showErrorMsg("上传附件失败");
                        call.cancel();// 上传失败取消请求释放内存
                    }
                }else{
                    mHandler.sendEmptyMessage(-1);
                    showErrorMsg("上传附件失败");
                    call.cancel();// 上传失败取消请求释放内存
                }
//
//                if(response.code() == 200) {
//                    String result = response.body().string();
//                    Logger.info("######################result=" + result);
//                    call.cancel();// 上传失败取消请求释放内存
//                    submit(uuid);
//                }else{
//                    mHandler.sendEmptyMessage(-1);
//                    showErrorMsg("上传附件失败");
//                    call.cancel();// 上传失败取消请求释放内存
//                }
            }
        });
    }

    /**
     * 压缩图片
     */
    private Bitmap createThumbnail(String filepath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filepath, options);

        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        // 想要缩放的目标尺寸
        float hh = h/2;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = w;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (options.outHeight / hh);
        }
        if (be <= 0) be = 1;
        options.inSampleSize = 4;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了

        return BitmapFactory.decodeFile(filepath, options);
    }

}
