package com.dk.mp.core.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Process;

import com.dk.mp.core.R;
import com.dk.mp.core.util.StringUtils;

/**
 * 作者：janabo on 2016/12/14 16:43
 */
public class AlertDialog {
    private Context context;
    public AlertDialog(Context context) {
        this.context = context;
    }

    /**
     * 公共对话框.
     */
    public void exitApp() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle(context.getResources().getString(R.string.app_name))
                .setMessage("您确定退出"+context.getResources().getString(R.string.app_name)+"?")
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                android.os.Process.killProcess(Process.myPid());
            }
        });
        alertDialog.create().show();
    }

    /**
     * 公共对话框.
     */
    public void show(String title,String content,DialogInterface.OnClickListener okOnClick) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        if(StringUtils.isNotEmpty(title)){
            alertDialog.setTitle(title);
        }
        alertDialog.setMessage(content)
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", okOnClick);
        alertDialog.create().show();
    }

    /**
     * 公共对话框.
     */
    public void show(String title,String content) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        if(StringUtils.isNotEmpty(title)){
            alertDialog.setTitle(title);
        }
        alertDialog.setMessage(content)
                .setNegativeButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.create().show();
    }

    /**
     * 更新应用对话框.
     */
    public void update(String title,String content,DialogInterface.OnClickListener okOnClick) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setMessage(content)
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("去下载", okOnClick);
        alertDialog.create().show();
    }

    /**
     * 弹出选择框
     * @param title
     * @param okSelectClick
     */
    public void show(String title, final String[] strs, DialogInterface.OnClickListener okSelectClick){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title)
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setItems(strs,okSelectClick);
        alertDialog.show();
    }



}
