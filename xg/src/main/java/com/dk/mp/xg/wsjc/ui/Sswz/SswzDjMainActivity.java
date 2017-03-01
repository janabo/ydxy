package com.dk.mp.xg.wsjc.ui.Sswz;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.SswzImageAdapter;
import com.dk.mp.xg.wsjc.entity.Common;
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

import static com.dk.mp.xg.wsjc.ui.WsjcDetailActivity.BASEPICPATH;


/**
 * 作者：janabo on 2017/1/17 10:09
 */
public class SswzDjMainActivity extends MyActivity implements EasyPermissions.PermissionCallbacks,View.OnClickListener{
    DrawHookView progress;//提交动画
    DrawCheckMarkView progress_check;//成功动画
    DrawCrossMarkView progress_cross;//错误动画
    ScrollView mRootView;
    TextView ok;//提交按钮
    LinearLayout ok_lin;
    List<String> imgs = new ArrayList<>();//保存图片地址
    SswzImageAdapter wImageAdapter;
    private RecyclerView gRecyclerView;//图片
    private static final int WRITE_RERD = 1;
    private String noCutFilePath ="";
    private ImageView wjxs_img,tbr_img;//违纪学生,提报人
    private TextView wjxs_name,wjxs_x,wjrq,wjlb_txt,tbr_name,tbr_x;//违纪学生姓名,违纪日期,违纪类别,提报人姓名
    private LinearLayout wjxs_lin,wjrq_lin,wjlb_lin,tbr_lin;//违纪日期,违纪类别，提报人
    List<Common> wjlbs = new ArrayList<>();//违纪类别
 //   private EditText wjdh_txt,bz;//违纪单号,备注
    private EditText bz;//违纪单号,备注
    private String wjxsid,wjlbid,tbrid;//违纪学生,违纪类别
    WsjcDetail detail = null;
    private TextView fjh,ssl,ssq,xq;//房间号，宿舍楼，宿舍区，校区
    private String fjhid,sslid,ssqid,xqid;
    private String uuid = UUID.randomUUID().toString();


    @Override
    protected int getLayoutID() {
        return R.layout.app_ssws_dj;
    }

    @Override
    protected void initView() {
        super.initView();
        fjh = (TextView) findViewById(R.id.fjh);
        ssl = (TextView) findViewById(R.id.ssl);
        ssq = (TextView) findViewById(R.id.ssq);
        xq = (TextView) findViewById(R.id.xq);
        bz = (EditText) findViewById(R.id.bz);
        wjlb_lin = (LinearLayout) findViewById(R.id.wjlb_lin);
        wjlb_txt = (TextView) findViewById(R.id.wjlb_txt);
//        wjdh_txt = (EditText) findViewById(R.id.wjdh_txt);
        wjrq_lin = (LinearLayout) findViewById(R.id.wjrq_lin);
        wjrq = (TextView) findViewById(R.id.wjrq);
        wjxs_x = (TextView) findViewById(R.id.wjxs_x);
        wjxs_lin = (LinearLayout) findViewById(R.id.wjxs_lin);
        wjxs_img = (ImageView) findViewById(R.id.wjxs_img);
        wjxs_name= (TextView) findViewById(R.id.wjxs_name);
        tbr_x = (TextView) findViewById(R.id.tbr_x);
        tbr_lin = (LinearLayout) findViewById(R.id.tbr_lin);
        tbr_img = (ImageView) findViewById(R.id.tbr_img);
        tbr_name= (TextView) findViewById(R.id.tbr_name);
        progress = (DrawHookView) findViewById(R.id.progress);
        progress_check = (DrawCheckMarkView) findViewById(R.id.progress_check);
        progress_cross = (DrawCrossMarkView) findViewById(R.id.progress_cross);
        mRootView = (ScrollView) findViewById(R.id.mRootView);
        ok_lin = (LinearLayout) findViewById(R.id.ok);
        ok = (TextView) findViewById(R.id.ok_text);
        ok.setEnabled(false);
        imgs.add("addImage");
        gRecyclerView = (RecyclerView) findViewById(R.id.imgView);
        gRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        wImageAdapter = new SswzImageAdapter(mContext,SswzDjMainActivity.this,imgs);
        gRecyclerView.setAdapter(wImageAdapter);
        wjrq_lin.setOnClickListener(this);
        wjxs_lin.setOnClickListener(this);
        tbr_lin.setOnClickListener(this);
        wjlb_lin.setOnClickListener(this);

        String wsjcDetail = getIntent().getStringExtra("wsjcDetail");
        if(StringUtils.isNotEmpty(wsjcDetail)) {
            detail = new Gson().fromJson(wsjcDetail, WsjcDetail.class);
        }
        setDetailSetText(detail);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("宿舍违章登记");
        getWjlbs();
    }

    public void setDetailSetText(WsjcDetail d){
        if(d != null) {
            fjh.setText(d.getFjh());
            ssl.setText(d.getSslName());
            ssq.setText(d.getSsqName());
            xq.setText(d.getXqName());
            fjhid = d.getFjhId();
            sslid = d.getSslId();
            ssqid = d.getSsqId();
            xqid = d.getXqId();
        }
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
//        Intent intent=new Intent(Intent.ACTION_PICK);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent, 5);

        noCutFilePath = BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(noCutFilePath)));
        startActivityForResult(getImageByCamera, 6);
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
                    wjxsid = data.getStringExtra("userIds");
                    String userName = data.getStringExtra("userNames");
                    if(userName.length()>0){
                        wjxs_name.setText(userName);
                        wjxs_x.setText(userName.substring(0,1));
                        getBackgroud(wjxs_lin,userName.substring(0,1));
                    }
                    wjxs_lin.setVisibility(View.VISIBLE);
                    wjxs_name.setVisibility(View.VISIBLE);
                    wjxs_img.setVisibility(View.GONE);
                }
                break;
            case 2://违纪日期
                if(resultCode == RESULT_OK){
                    String mWjrq = data.getStringExtra("date");
                    wjrq.setText(mWjrq);
                    dealOkButton();
                }
                break;
            case 3://违纪类别
                if(resultCode == RESULT_OK){
                    String kfs = data.getStringExtra("kfs");
                    wjlbid = data.getStringExtra("kfsid");
                    wjlb_txt.setText(kfs);
                    dealOkButton();
                }
                break;
            case 4://提报人
                if (resultCode == RESULT_OK) {
                    tbrid = data.getStringExtra("userIds");
                    String userName = data.getStringExtra("userNames");
                    if(userName.length()>0){
                        tbr_name.setText(userName);
                        tbr_x.setText(userName.substring(0,1));
                        getBackgroud(tbr_lin,userName.substring(0,1));
                    }
                    tbr_lin.setVisibility(View.VISIBLE);
                    tbr_name.setVisibility(View.VISIBLE);
                    tbr_img.setVisibility(View.GONE);
                    dealOkButton();
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
            case 6:
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

    @Override
    public void onClick(View view) {
        if(R.id.wjxs_lin == view.getId()){//违纪学生
            wjxs_lin.setVisibility(View.GONE);
            wjxs_name.setVisibility(View.GONE);
            wjxs_img.setVisibility(View.VISIBLE);
        }else if(R.id.wjrq_lin == view.getId()){//选择日期
            Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        }else if(R.id.wjlb_lin == view.getId()){//违纪类别
            if(wjlbs.size()>0) {
                Intent intent = new Intent(mContext, SswzWjlbPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) wjlbs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }else{
                getWjlbs();
                if(wjlbs.size()>0) {
                    Intent intent = new Intent(mContext, SswzWjlbPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("kfs", (Serializable) wjlbs);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 3);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                }else {
                    showErrorMsg(mRootView, "未获取到违纪类别信息");
                }
            }
        }else if(R.id.tbr_lin == view.getId()){//提报人
            tbr_lin.setVisibility(View.GONE);
            tbr_name.setVisibility(View.GONE);
            tbr_img.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 增加违纪学生
     * @param view
     */
    public void addWjxs(View view){
        Intent intent = new Intent(mContext, SswzSelectPersonActivity.class);
        intent.putExtra("fjhid",fjhid);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    /**
     * 增加提报人
     * @param view
     */
    public void addTbr(View view){
        Intent intent = new Intent(mContext, SswzSelectPersonActivity.class);
        intent.putExtra("type","tbr");
        intent.putExtra("fjhid",fjhid);
        startActivityForResult(intent, 4);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
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
            updateImg();
        }else{
            submit();
        }
    }

    public void submit(){
        Map<String,Object> params = new HashMap<>();
        params.put("id", uuid);
        params.put("xq",xqid);
        params.put("ssq",ssqid);
        params.put("ssl",sslid);
        params.put("fjh",fjhid);
        params.put("wjxs",wjxsid);
        params.put("wjrq",wjrq.getText().toString()+" 00:00:00");
        params.put("wjdhbh",uuid);
        params.put("wjlb",wjlbid);
        params.put("tbr",tbrid);
        params.put("fjName",uuid);
        params.put("bz",bz.getText().toString());

        HttpUtil.getInstance().postJsonObjectRequest("apps/sswzdj/tjwz", params, new HttpListener<JSONObject>() {
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
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    public void clearImg(){
        noCutFilePath="";
    }

    /**
     * 上传图片
     */
    public void updateImg(){
        LoginMsg loginMsg = getSharedPreferences().getLoginMsg();
        String mUrl = getReString(R.string.uploadUrl);
        if(loginMsg != null) {
            mUrl +="stargate/attachmentUpload.service?type=sswsjcAttachment&userId="+loginMsg.getUid()+"&password="+loginMsg.getPsw()+"&ownerId="+uuid;
        }else{
            mUrl +="stargate/attachmentUpload.service?type=sswsjcAttachment&ownerId="+uuid;
        }
        List<File> files = new ArrayList<>();
        files.add(new File(noCutFilePath));
        HttpUtil.getInstance().uploadImg(mUrl,files,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();// 上传失败取消请求释放内存
                errorInfo();
                showErrorMsg("上传附件失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                Logger.info("######################result="+result);
                call.cancel();// 上传失败取消请求释放内存
                submit();
            }
        });
    }
}
