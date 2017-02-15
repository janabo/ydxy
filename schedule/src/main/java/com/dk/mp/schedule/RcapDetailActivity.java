package com.dk.mp.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.entity.RcapDetail;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.schedule.db.RealmHelper;


/**
 * 作者：janabo on 2016/12/28 14:57
 */
public class RcapDetailActivity extends MyActivity implements View.OnClickListener{
    private TextView title, content, place;
    private TextView starttime, endtime;
    private Button delete, edit;
    private RcapDetail rcap;
    public static RcapDetailActivity instance = null;
    private RealmHelper realmHelper;

    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        realmHelper = new RealmHelper(this);
        setTitle(getIntent().getStringExtra("title"));
        initView();
    }

    public void initView(){
        title = (TextView) findViewById(R.id.schedule_title);
        content = (TextView) findViewById(R.id.schedule_content);
        place = (TextView) findViewById(R.id.schedule_place);
        starttime = (TextView) findViewById(R.id.show_starttime);
        endtime = (TextView) findViewById(R.id.show_endtime);
        delete = (Button) findViewById(R.id.delete);
        edit = (Button) findViewById(R.id.edit);
        delete.setOnClickListener(this);
        edit.setOnClickListener(this);

        rcap = (RcapDetail) getIntent().getSerializableExtra("rcapDetail");
        title.setText(rcap.getTitle());
        content.setText(rcap.getContent());
        place.setText(rcap.getLocation());
        starttime.setText(rcap.getTime_start());
        endtime.setText(rcap.getTime_end());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete) {
            final AlertDialog alert = new AlertDialog(this);
            alert.show(null, "确定删除该日程吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(rcap != null) {
                        realmHelper.deleteRcap(rcap.getRcid());
                    }
                    BroadcastUtil.sendBroadcast(mContext, "rcap_refresh");
                    finish();
                }
            });
        } else if (v.getId() == R.id.edit) {
            Intent intent = new Intent(mContext, RcapDetailEditActivity.class);
            intent.putExtra("title", "编辑日程");
            intent.putExtra("idRcap", rcap.getRcid());
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmHelper.closeRealm();
    }
}
