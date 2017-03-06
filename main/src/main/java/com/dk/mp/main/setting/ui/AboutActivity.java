package com.dk.mp.main.setting.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.JsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.main.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 关于软件
 * 作者：janabo on 2017/2/17 14:40
 */
public class AboutActivity extends MyActivity{
    private TextView version,mTitle,updateInfo,copyright_info_ch,copyright_info_zn;
    private Button back;
    private String url="",desc="",versionName="";//下载地址，描述
    private ErrorLayout mError;
    private RelativeLayout update_layout;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_about;
    }

    @Override
    protected void initView() {
        super.initView();
        update_layout = (RelativeLayout) findViewById(R.id.update_layout);
        version = (TextView) findViewById(R.id.version);
        mTitle = (TextView) findViewById(R.id.schoolname);
        updateInfo = (TextView) findViewById(R.id.updateInfo);
        copyright_info_ch = (TextView) findViewById(R.id.copyright_info_ch);
        copyright_info_zn = (TextView) findViewById(R.id.copyright_info_en);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        getData();
    }

    @Override
    protected void initialize() {
        super.initialize();
        version.setText("版本"+DeviceUtil.getVersionName(mContext));
        mTitle.setText(getReString(R.string.app_name)+"移动校园");
        copyright_info_ch.setText(getReString(R.string.about_copyright_cn));
        copyright_info_zn.setText(getReString(R.string.about_copyright_zn));
    }

    /**
     * 检查更新信息
     * @param v
     */
    public void toUpdate(View v){
        if(StringUtils.isNotEmpty(url)){
            AlertDialog alertDialog = new AlertDialog(mContext);
            alertDialog.update("发现新版本(" + versionName + ")", desc, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, HttpWebActivity.class);
                    intent.putExtra("title", "检查更新");
                    intent.putExtra("url", "http://a.app.qq.com/o/simple.jsp?pkgname="+DeviceUtil.getPackage(mContext));
                    startActivity(intent);
                }
            });
        }else{
            showErrorMsg("您已经是最新版本");
        }
    }

    /**
     * 获取更新日志
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            Map<String,Object> map = new HashMap<>();
            map.put("versionId",DeviceUtil.getVersionCode(mContext));
            map.put("platform","android");
            HttpUtil.getInstance().postJsonObjectRequest("version", map, new HttpListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        if(result != null) {
                            JsonData jsonData = new Gson().fromJson(result.toString(),JsonData.class);
                            if (jsonData.getCode() == 200) {
                                Map map = (Map) jsonData.getData();
                                if(map != null && !map.isEmpty()){
                                    url = (String) map.get("url");
                                    desc = (String) map.get("desc");
                                    versionName = (String) map.get("versionName");
                                    updateInfo.setText(desc);
                                    if(StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(desc)){
                                        update_layout.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
//                                showErrorMsg(getReString(R.string.net_no2));
                                showErrorMsg("获取版本内容失败");
                            }
                        }else{
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            showErrorMsg("获取版本内容失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        showErrorMsg("获取版本内容失败");
                    }
                }
                @Override
                public void onError(VolleyError error) {
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    showErrorMsg("获取版本内容失败");
                }
            });
        }else{
            showErrorMsg(getReString(R.string.net_no2));
        }
    }
}
