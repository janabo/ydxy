package com.dk.mp.xg.wsjc.ui.zsskq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.adapter.MyFragmentPagerAdapter;
import com.dk.mp.xg.wsjc.entity.Common;
import com.dk.mp.xg.wsjc.fragment.WsjcTjFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 住宿生统计
 * 作者：janabo on 2017/1/22 09:42
 */
public class ZsskqTjTabActivity extends MyActivity{
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    ImageView dropdownimg;//下拉图标
    private String type;//bzr,xb,xg;班主任 学工 系部
    private LinearLayout dropdown;
    private int tabSelect = 0;
    List<Common> mClasses = new ArrayList<>();//班级列表
    private WsjcTjFragment fragment1 = new WsjcTjFragment();
    private WsjcTjFragment fragment2 = new WsjcTjFragment();
    private WsjcTjFragment fragment3 = new WsjcTjFragment();
    private String weekid="";//选择的班级id
    private String weekname="";//选择的班级名称
    LoginMsg loginMsg;

    @Override
    protected int getLayoutID() {
        return R.layout.app_zsskqtj_tab;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("住宿生考勤统计");
        loginMsg = getSharedPreferences().getLoginMsg();
        type = getIntent().getStringExtra("role");
        findView();
        initViewPager();
        getClassesOrDepartMents();
    }

    private void findView(){
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
        dropdown = (LinearLayout) findViewById(R.id.dropdown);
        dropdownimg = (ImageView) findViewById(R.id.dropdownimg);
        dropdownimg.setVisibility(View.GONE);
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClasses.size() > 0) {
                    Intent intent = new Intent(mContext, ZsskqBjPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("kfs", (Serializable) mClasses);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.push_up_in, 0);
                } else {
                    showErrorMsg(mViewpager, "未获取到班级或院系选项");
                }
            }
        });
    }

    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("今天");
        titles.add("本周");
        titles.add("本月");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelect = tab.getPosition();
                getTjUrl(weekid);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 获取班级或部门
     */
    private void getClassesOrDepartMents(){
        Map<String,Object> map = new HashMap<>();
        map.put("role",type);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zsskq/select", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mClasses.addAll(dfxxes);
                                setTitle(mClasses.get(0).getName());
                                if(dfxxes.size()>1){
                                    dropdownimg.setVisibility(View.VISIBLE);
                                }
                                weekid = dfxxes.get(0).getId();
                                getTjUrl(weekid);
                            }else{
                                setTitle("住宿生考勤");
                            }
                        } else {
                            setTitle("住宿生考勤");
                        }
                    }else{
                        setTitle("住宿生考勤");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setTitle("住宿生考勤");
                }
            }

            @Override
            public void onError(VolleyError error) {
                setTitle("住宿生考勤");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    weekid = data.getStringExtra("kfsid");
                    weekname = data.getStringExtra("kfs");
                    setTitle(weekname);
                    getTjUrl(weekid);
                }
                break;
        }
    }

    public void getTjUrl(String id){
        String mUrl="";
        if(loginMsg != null){
            mUrl = "&uid="+loginMsg.getUid()+"&pwd="+ loginMsg.getPsw();
        }
        if(tabSelect == 0){
            fragment1.setMUrl("apps/zsskq/tj?type=today&id="+id+"&role="+type+mUrl);
        }else if(tabSelect == 1){
            fragment2.setMUrl("apps/zsskq/tj?type=week&id="+id+"&role="+type+mUrl);
        }else if(tabSelect == 2){
            fragment3.setMUrl("apps/zsskq/tj?type=month&id="+id+"&role="+type+mUrl);
        }

    }

}
