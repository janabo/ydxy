package com.dk.mp.newoa.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.widget.ArrayWheelAdapter;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.newoa.R;
import com.dk.mp.newoa.entity.Jbxx;

import java.util.List;

/**
 * 作者：janabo on 2017/3/22 15:48
 */
public class HysPickActivity extends Activity {
    private String[] PLANETM;
    private WheelView hys;
    private List<Jbxx> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_hys);
        Bundle bundle = getIntent().getExtras();
        list = (List<Jbxx>) bundle.getSerializable("hys");
        PLANETM = new String[list.size()];
        if(list != null && list.size()>0){
            int i =0;
            for(Jbxx j : list){
                PLANETM[i] = j.getValue();
                i++;
            }
        }
        findView();
    }

    private void findView(){
        hys = (WheelView) findViewById(R.id.hys);
        hys.setAdapter(new ArrayWheelAdapter<String>(PLANETM));
        hys.setCyclic(true);

        Button bt = (Button)findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = PLANETM[hys.getCurrentItem()];
                Intent in = new Intent();
                in.putExtra("hys", str);
                in.putExtra("hysid", list.get(hys.getCurrentItem()).getKey());
                setResult(3, in);
                back();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
    public void back() {
        finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
