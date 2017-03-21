package com.dk.mp.main.setting.ui;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.FileUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.main.R;
import com.dk.mp.main.login.LoginActivity;
import com.dk.mp.main.util.PushUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：janabo on 2016/12/14 16:13
 */
public class SettingActivity extends MyActivity{
    private Context context = SettingActivity.this;
    private TextView name,xm,bmhyx;
    private LinearLayout login;
    private CoreSharedPreferencesHelper helper;
    private LinearLayout xsxx;
    SwitchCompat checkbox_settting;
    private ScrollView setting_scro;
    private ImageView pesonphoto;
    private TextView font_txt;
    private String selectFont="标准";
    public static SettingActivity instance;


    @Override
    protected int getLayoutID() {
        return R.layout.mp_setting;
    }

    @Override
    protected void initialize() {
        super.initialize();
        helper = getSharedPreferences();
        instance = SettingActivity.this;
        findView();
        setTitle("设置");
        BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"login","user"});
        setUser();
    }
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("user")) {
                setUser();
            }else if(action.equals("login")){
                Intent in = new Intent(context,LoginActivity.class);
                startActivity(in);
            }
        }
    };

    public void findView(){
        font_txt = (TextView) findViewById(R.id.font_txt);
        setting_scro = (ScrollView) findViewById(R.id.setting_scro);
        name = (TextView) findViewById(R.id.name);
//        version_new = (TextView) findViewById(R.id.version_new);
        login = (LinearLayout) findViewById(R.id.login);
        xsxx = (LinearLayout) findViewById(R.id.xsxx);
        xm = (TextView) findViewById(R.id.xm);
        bmhyx = (TextView) findViewById(R.id.bmhyx);
        pesonphoto = (ImageView) findViewById(R.id.photo);
        checkbox_settting = (SwitchCompat) findViewById(R.id.checkbox_settting);
        checkbox_settting.setChecked(helper.getBoolean("push_check"));
        checkbox_settting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SnackBarUtil.showShort(setting_scro,
                        isChecked?"开启推送设置，您将及时收到学校公布的各类重要消息！":"关闭推送设置，您将不会收到学校公布的各类重要消息！");
                PushUtil.setStatus(SettingActivity.this,isChecked?1:0);
                helper.setBoolean("push_check",isChecked);
            }
        });
        String value = helper.getValue("font_type");
        if(value == null || "标准".equals(value)) {
            font_txt.setText("标准");
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
            helper.setBoolean("push_check",false);
            PushUtil.setStatus(SettingActivity.this,0);
        }else{
            SnackBarUtil.showShort(setting_scro,"开启推送设置，您将及时收到学校公布的各类重要消息！");
            checkbox_settting.setChecked(true);
            helper.setBoolean("push_check",true);
            PushUtil.setStatus(SettingActivity.this,1);
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
        Intent intent = new Intent(mContext,AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 设置字体大小
     * @param v
     */
    public void tosetFontSize(View v){
        final String[] fonts={"特大","大","标准"};
        new AlertDialog(mContext).show("设置字体大小", fonts, new DialogInterface.OnClickListener(){
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
    public void tologin(View v){
        User user = helper.getUser();
        if (user != null) {
            Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
            ActivityOptions options = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(SettingActivity.this, pesonphoto, "personphoto");
                ActivityCompat.startActivity(SettingActivity.this,intent, options.toBundle());
            }else{
                startActivity(intent);
            }

        } else {
            Intent in = new Intent(context,LoginActivity.class);
            startActivity(in);
        }
    }

    private void setUser() {
        User user = helper.getUser();
        if (user != null) {
            name.setVisibility(View.GONE);
            xsxx.setVisibility(View.VISIBLE);
            xm.setText(user.getUserName());
            String departname = "null".equals(user.getDepartName())||user.getDepartName() == null ? "":user.getDepartName();
            if("1".equals(user.getRoles())){
                bmhyx.setText("部门："+departname);
            }else{
                bmhyx.setText("院系："+departname);
            }
        } else {
            name.setText("点击登录");
            name.setVisibility(View.VISIBLE);
            xsxx.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initBirthTheme();
    }

    public void initBirthTheme(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        boolean isBrithDay;
        if(helper.getUser() == null){
            isBrithDay = false;
        }else if(StringUtils.isNotEmpty(helper.getUser().getBirthday())){
            String brithday = helper.getUser().getBirthday();
//            if(helper.getUser().getUserId().equals("portal")) {
//                brithday = "2017-03-21";
//            }
            if(brithday.length() == 10){
                isBrithDay = today.substring(5,today.length()).equals(brithday.substring(5,brithday.length()));
            }else{
                isBrithDay = today.substring(6,today.length()).equals(brithday.substring(5,brithday.length()));
            }
        }else{
            isBrithDay = false;
        }

        if (isBrithDay) {
            showBrithdayTheme();
        }else{
            hideBrithdayTheme();
        }
    }
}
