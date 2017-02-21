package com.dk.mp.oldoa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.dialog.MsgDialog;

import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/20 14:42
 */
public class MyActivity extends Activity {
    private ProgressDialog mProgressDialog;//发送请求 等待的dialog
    public ImageButton right;
    public PopupWindow popupWindow;
    ImageView right_src;

    /**
     * 初始化皮肤.
     * @param title 标题栏文字
     */
    public void setTitle(String title) {
        try {
            TextView textView = (TextView) findViewById(R.id.title);
            textView.setText(title);
        } catch (Exception e) {
        }

        try {
            Button back = (Button) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    back();
                }
            });
        } catch (Exception e) {
        }
    }

    //===================================
    /**
     * 无网络提示
     */
    public void setNoWorkNet() {
        LinearLayout zwsj = (LinearLayout) findViewById(R.id.zwsj);
        ImageView zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
        TextView zwsj_text = (TextView) findViewById(R.id.zwsj_text);
        zwsj.setVisibility(View.VISIBLE);
        zwsj_icon.setImageResource(R.mipmap.nonet);
        zwsj_text.setText(getString(R.string.net_no2));
    }

    /**
     * 无数据提示
     * @param text 提示文字
     */
    public void setNoDate(String text) {
        LinearLayout zwsj = (LinearLayout) findViewById(R.id.zwsj);
        ImageView zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
        TextView zwsj_text = (TextView) findViewById(R.id.zwsj_text);
        zwsj.setVisibility(View.VISIBLE);
        zwsj_icon.setImageResource(R.mipmap.nodata);
        if(text!=null){
            zwsj_text.setText(text);
        }else{
            zwsj_text.setText(getString(R.string.nodata));
        }
    }

    /**
     * 数据失败提示
     * @param text 提示文字
     */
    public void setErrorDate(String text) {
        LinearLayout zwsj = (LinearLayout) findViewById(R.id.zwsj);
        ImageView zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
        TextView zwsj_text = (TextView) findViewById(R.id.zwsj_text);
        zwsj.setVisibility(View.VISIBLE);
        zwsj_icon.setImageResource(R.mipmap.data_fail);
        if(text!=null){
            zwsj_text.setText(text);
        }else{
            zwsj_text.setText(getString(R.string.data_fail));
        }
    }

    //===================================

    /**
     * 初始化皮肤.
     * @param title 标题栏文字
     */
    public void setTitle(int title) {
        setTitle(getReString(title));
    }


    public void setRightText(String text, View.OnClickListener listener) {
        try {
            TextView right_txt = (TextView) findViewById(R.id.right_txt);
            right_txt.setText(text);
            right_txt.setVisibility(View.VISIBLE);
            right_txt.setOnClickListener(listener);
        } catch (Exception e) {
        }
    }

    public void setRightTextColor(int color) {
        try {
            TextView right_txt = (TextView) findViewById(R.id.right_txt);
            right_txt.setTextColor(color);
        } catch (Exception e) {
        }
    }

    public void setRightText(String text, int color, View.OnClickListener listener) {
        try {
            TextView right_txt = (TextView) findViewById(R.id.right_txt);
            right_txt.setText(text);
            right_txt.setVisibility(View.VISIBLE);
            right_txt.setTextColor(color);
            right_txt.setOnClickListener(listener);
        } catch (Exception e) {
        }
    }

    /**
     * 设置右上角按钮图标.
     * @param text 资源
     */
    public void setRightText(String text) {
        try {
            TextView right_txt = (TextView) findViewById(R.id.right_txt);
            right_txt.setText(text);
            right_txt.setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
    }

    /**
     * 设置右上角按钮图标.
     * @param image 资源
     */
    public void setRightButton(int image, View.OnClickListener click) {
        try {
            right = (ImageButton) findViewById(R.id.right);
            right.setImageResource(image);
            right.setVisibility(View.VISIBLE);
            right.setOnClickListener(click);
        } catch (Exception e) {
        }
    }


    /**
     * 公共手机返回按钮事件.
     * @param keyCode keyCode
     * @param event  KeyEvent
     * @return  boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void back() {
        finish();
    }

    /**
     * 显示提示信息.
     * @param message 提示信息
     */
    public void showMessage(String message) {
        MsgDialog.show(this, message);
    }

    /**
     * 获取string
     * @param string R.string.x
     * @return  ""
     */
    public String getReString(int string) {
        return getResources().getString(string);
    }

    /**
     * 显示提示信息.
     * @param message 提示信息
     */
    public void showMessage(int message) {
        MsgDialog.show(this, getReString(message));
    }



    /**
     * 创建进度框.
     * @param
     */
    public void showProgressDialog(String tip) {
        if (mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            mProgressDialog = dialog;
        }
        try {
            LayoutInflater inflater = LayoutInflater.from(MyActivity.this);
            View view = inflater.inflate(R.layout.core_loading, null);
            TextView textView = (TextView) view.findViewById(R.id.oaprogresstitle);
            String temp = getResources().getString(R.string.loading);
            if (StringUtils.isNotEmpty(tip)) {
                temp = tip;
            }
            textView.setText(temp);
            mProgressDialog.show();
            mProgressDialog.setContentView(view);
            float density = getResources().getDisplayMetrics().density;
            mProgressDialog.setContentView(view,
                    new LayoutParams(StringUtils.dip2px(this, 64), StringUtils.dip2px(this, 64)));
        } catch (Exception e) {
        }
    }

    /**
     * 创建进度框.
     * @param
     */
    public void showProgressDialog() {
        float density = getResources().getDisplayMetrics().density;
        if (mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            mProgressDialog = dialog;
        }
        try {
            mProgressDialog.show();
            View view = LayoutInflater.from(this).inflate(R.layout.core_loading,
                    (ViewGroup) getWindow().getDecorView(), false);
            mProgressDialog.setContentView(view,
                    new LayoutParams(StringUtils.dip2px(this, 64), StringUtils.dip2px(this, 64)));
        } catch (Exception e) {
        }

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 初始化弹出列表.
     * @param types  List<Map<String, String>>
     * @param onItemClick OnItemClickListener
     */
    public void initControls(List<Map<String, String>> types, AdapterView.OnItemClickListener onItemClick) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.core_popupwindow_list, null);
        SimpleAdapter adapter = new SimpleAdapter(this, types, R.layout.core_popupwindow_item, new String[] { "text" },
                new int[] { R.id.item });
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);

        //自适配长、框设置
        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.privacy_spinner_pop_bg));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.update();
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
    }

    /**
     * 显示snakebar 错误信息
     * @param v
     * @param msg
     */
    public void showErrorMsg(View v,String msg){
        SnackBarUtil.showShort(v,msg);
    }
}
