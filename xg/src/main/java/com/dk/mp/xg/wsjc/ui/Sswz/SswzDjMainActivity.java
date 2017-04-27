package com.dk.mp.xg.wsjc.ui.Sswz;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
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
import com.dk.mp.core.entity.ResultCode;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.DrawCheckMarkView;
import com.dk.mp.core.view.DrawCrossMarkView;
import com.dk.mp.core.view.DrawHookView;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.SswzDjPersonsAdapter;
import com.dk.mp.xg.wsjc.adapter.SswzImageAdapter;
import com.dk.mp.xg.wsjc.adapter.SswzSdluPersonsAdapter;
import com.dk.mp.xg.wsjc.entity.Common;
import com.dk.mp.xg.wsjc.entity.WsjcDetail;
import com.dk.mp.xg.wsjc.entity.Zssdjgl;
import com.dk.mp.xg.wsjc.ui.zssdjgl.SelectPersonsActivity;
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

import static com.dk.mp.xg.wsjc.ui.WsjcDetailActivity.BASEPICPATH;


/**
 * 作者：janabo on 2017/1/17 10:09
 */
public class SswzDjMainActivity extends MyActivity implements EasyPermissions.PermissionCallbacks,View.OnClickListener, SswzDjPersonsAdapter.OnItemClickListener{
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
    private ImageView tbr_img;//违纪学生,提报人
    private TextView wjrq,wjlb_txt,tbr_name,tbr_x;//违纪学生姓名,违纪日期,违纪类别,提报人姓名
    private LinearLayout wjrq_lin,wjlb_lin,tbr_lin;//违纪日期,违纪类别，提报人
    List<Common> wjlbs = new ArrayList<>();//违纪类别
 //   private EditText wjdh_txt,bz;//违纪单号,备注
    private EditText bz;//违纪单号,备注
    private String wjxsid,wjlbid,tbrid;//违纪学生,违纪类别
    WsjcDetail detail = null;
    private TextView fjh,ssl,ssq,xq;//房间号，宿舍楼，宿舍区，校区
    private String fjhid,sslid,ssqid,xqid;
    private String uuid = UUID.randomUUID().toString();

    private RecyclerView mRecyclerView;
    private SswzDjPersonsAdapter zAdapter;
    private List<Zssdjgl> persons = new ArrayList<>();

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

//        wjxs_x = (TextView) findViewById(R.id.wjxs_x);
//        wjxs_lin = (LinearLayout) findViewById(R.id.wjxs_lin);
//        wjxs_img = (ImageView) findViewById(R.id.wjxs_img);
//        wjxs_name= (TextView) findViewById(R.id.wjxs_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.person_recycle);
        persons.add(new Zssdjgl("addperson",""));
        zAdapter = new SswzDjPersonsAdapter(persons,mContext,SswzDjMainActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(zAdapter);

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
//        wjxs_lin.setOnClickListener(this);
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
                    persons.clear();
                    persons.addAll((List<Zssdjgl>)data.getSerializableExtra("persons"));
                    persons.add(new Zssdjgl("addperson",""));
                    zAdapter.notifyDataSetChanged();
                    dealOkButton();
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

    @Override
    public void onClick(View view) {
//        if(R.id.wjxs_lin == view.getId()){//违纪学生
//            wjxs_lin.setVisibility(View.GONE);
//            wjxs_name.setVisibility(View.GONE);
//            wjxs_img.setVisibility(View.VISIBLE);
//        }else
        if(R.id.wjrq_lin == view.getId()){//选择日期
            Intent intent = new Intent(mContext, SswzWjrqPickActivity.class);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.push_up_in, 0);
        }else if(R.id.wjlb_lin == view.getId()){//违纪类别
            if(wjlbs.size()>0) {
                Intent intent = new Intent(mContext, SswzWjlbPickActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kfs", (Serializable) wjlbs);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
                overridePendingTransition(R.anim.push_up_in, 0);
            }else{
                getWjlbs();
                if(wjlbs.size()>0) {
                    Intent intent = new Intent(mContext, SswzWjlbPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("kfs", (Serializable) wjlbs);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 3);
                    overridePendingTransition(R.anim.push_up_in, 0);
                }else {
                    showErrorMsg(mRootView, "未获取到违纪类别信息");
                }
            }
        }else if(R.id.tbr_lin == view.getId()){//提报人
            tbr_lin.setVisibility(View.GONE);
            tbr_name.setVisibility(View.GONE);
            tbr_img.setVisibility(View.VISIBLE);
            tbrid="";
            dealOkButton();
        }
    }

    /**
     * 增加违纪学生
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
            showErrorMsg("请先选择房间号");
        }

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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
     *
     *
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
