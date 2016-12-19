package com.dk.mp.main.setting.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.User;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.FileUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.main.R;
import com.dk.mp.main.login.LoginActivity;

/**
 * 作者：janabo on 2016/12/14 16:13
 */
public class SettingActivity extends MyActivity{
    private Context context = SettingActivity.this;
    private TextView version_new, name,xm,bmhyx;
    private LinearLayout login;
    private CoreSharedPreferencesHelper helper;
    private LinearLayout xsxx;
    SwitchCompat checkbox_settting;
    private ScrollView setting_scro;
    private ImageView pesonphoto;
    private TextView font_txt;
    private String selectFont="小号字";

    @Override
    protected int getLayoutID() {
        return R.layout.mp_setting;
    }

    @Override
    protected void initialize() {
        super.initialize();
        helper = getSharedPreferences();
        initView();
        setTitle("设置");
        version_new.setText("当前版本:" + DeviceUtil.getVersionName(context));
    }

    public void initView(){
        font_txt = (TextView) findViewById(R.id.font_txt);
        setting_scro = (ScrollView) findViewById(R.id.setting_scro);
        name = (TextView) findViewById(R.id.name);
        version_new = (TextView) findViewById(R.id.version_new);
        login = (LinearLayout) findViewById(R.id.login);
        xsxx = (LinearLayout) findViewById(R.id.xsxx);
        xm = (TextView) findViewById(R.id.xm);
        bmhyx = (TextView) findViewById(R.id.bmhyx);
        pesonphoto = (ImageView) findViewById(R.id.photo);
        checkbox_settting = (SwitchCompat) findViewById(R.id.checkbox_settting);
        checkbox_settting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SnackBarUtil.showShort(setting_scro,
                        isChecked?"开启推送设置，您将及时收到学校公布的各类重要消息！":"关闭推送设置，您将不会收到学校公布的各类重要消息！");
            }
        });
        String value = helper.getValue("font_type");
        if(value == null || "小字号".equals(value)) {
            font_txt.setText("小字号");
        }else{
            font_txt.setText(value);
        }
    }

    /**
     * 是否开启推送
     * @param v
     */
    public void topush(View v){
        if(checkbox_settting.isChecked()){
            SnackBarUtil.showShort(setting_scro,"关闭推送设置，您将不会收到学校公布的各类重要消息！");
            checkbox_settting.setChecked(false);
        }else{
            SnackBarUtil.showShort(setting_scro,"开启推送设置，您将及时收到学校公布的各类重要消息！");
            checkbox_settting.setChecked(true);
        }
    }

    /**
     * 清除缓存
     * @param v
     */
    public  void onClickCleanCache(View v) {
        AlertDialog alert = new AlertDialog(mContext);
        alert.show("提示", "确定要清除缓存吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.delete(7);
                        SnackBarUtil.showShort(setting_scro,"清除缓存成功!");
                    }
                }).start();
            }
        });
    }

    /**
     * 关于
     * @param v
     */
    public void toabout(View v){

    }

    /**
     * 检查版本更新
     * @param v
     */
    public void toversion(View v){

    }

    /**
     * 设置字体大小
     * @param v
     */
    public void tosetFontSize(View v){
        final String[] fonts={"大号字","中号字","小号字"};
        new AlertDialog(mContext).show("字体大小", fonts, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectFont = fonts[which];
                font_txt.setText(selectFont);
                helper.setValue("font_type",selectFont);
                initTheme();
                recreate();
            }
        });
    }

    /**
     * 登录 或者跳转到个人信息
     * @param v
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void tologin(View v){
        User user = helper.getUser();
        if (user != null) {
            Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SettingActivity.this, pesonphoto, "personphoto");
            ActivityCompat.startActivity(SettingActivity.this,intent, options.toBundle());
        } else {
            Intent in = new Intent(context,LoginActivity.class);
            startActivity(in);
        }
    }

}
