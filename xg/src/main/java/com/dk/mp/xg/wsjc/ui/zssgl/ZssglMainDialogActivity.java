package com.dk.mp.xg.wsjc.ui.zssgl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dk.mp.xg.R;

/**
 * 作者：janabo on 2017/1/18 16:39
 */
public class ZssglMainDialogActivity extends Activity implements View.OnClickListener{
    Button cancel_btn,tiaossq,tingssq,tuissq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_zssgl_dialog);
        findView();
    }

    public void findView(){
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        tiaossq = (Button) findViewById(R.id.tiaossq);
        tingssq = (Button) findViewById(R.id.tingssq);
        tuissq = (Button) findViewById(R.id.tuissq);
        cancel_btn.setOnClickListener(this);
        tiaossq.setOnClickListener(this);
        tingssq.setOnClickListener(this);
        tuissq.setOnClickListener(this);
    }

    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }

    @Override
    public void onClick(View view) {
        String lmlb = "-1";
        String lmlbname = "取消";
        if(view.getId() == R.id.tingssq){
            lmlb = "1";
            lmlbname = "停宿申请";
        }else if(view.getId() == R.id.tiaossq){
            lmlb = "2";
            lmlbname = "调宿申请";
        }else if(view.getId() == R.id.tuissq){
            lmlb = "3";
            lmlbname = "退宿申请";
        }
        Intent intent = new Intent();
        intent.putExtra("lmlb",lmlb);
        intent.putExtra("lmlbname",lmlbname);
        setResult(RESULT_OK, intent);
        back();
    }
}
