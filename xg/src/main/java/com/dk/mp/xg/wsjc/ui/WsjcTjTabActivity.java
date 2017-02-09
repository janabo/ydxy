package com.dk.mp.xg.wsjc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.GsonData;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.TimeUtils;
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
import java.util.List;

/**
 * 打分统计界面
 * 作者：janabo on 2017/1/13 16:12
 */
public class WsjcTjTabActivity extends MyActivity {
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private String title;
    private String type;//bzr,xb,xg;班主任 学工 系部
    private LinearLayout dropdown;
    private int tabSelect = 0;
    List<Common> mWeeks = new ArrayList<>();//周
    List<Common> mSemester = new ArrayList<>();//学期
    List<Common> mTemplet = new ArrayList<>();//模板
    private String weekid="";//选择的周次
    private String weekname="";//选择的周次名
    private String month;//选择的月
    private String semesterid;//学期id
    private String semestername;//学期名
    private String templetid;//模板id
    private WsjcTjFragment fragment1 = new WsjcTjFragment();
    private WsjcTjFragment fragment2 = new WsjcTjFragment();
    private WsjcTjFragment fragment3 = new WsjcTjFragment();

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjctj_tab;
    }

    @Override
    protected void initialize() {
        super.initialize();
        type = getIntent().getStringExtra("type");
        findView();
        initViewPager();
        getWeeks();
        getSemesters();
        getTempl();
    }

    private void findView(){
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
        dropdown = (LinearLayout) findViewById(R.id.dropdown);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tabSelect == 0) {//按周统计
                    if (mWeeks.size() > 0) {
                        Intent intent = new Intent(mContext, WsjcTjWeekPickActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("kfs", (Serializable) mWeeks);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                    } else {
                        showErrorMsg(mViewpager, "未获取到周次选项");
                    }
                }else if(tabSelect == 1){//按月统计
                    Intent intent = new Intent(mContext, WsjcTjMonthPickActivity.class);
                    startActivityForResult(intent, 2);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                }else if(tabSelect == 2){//按学期统计
                    if (mSemester.size() > 0 && mTemplet.size()>0) {
                        Intent intent = new Intent(mContext, WsjcTjSemesterPickActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("xq", (Serializable) mSemester);
                        bundle.putSerializable("mb", (Serializable) mTemplet);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 3);
                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                    }else{
                        showErrorMsg(mViewpager, "未获取到学期或模板选项");
                    }
                }
            }
        });
    }

    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("按周统计");
        titles.add("按月统计");
        titles.add("按学期统计");

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
                if(tabSelect == 0){
                    if(mWeeks.size()>0){
                        weekname = mWeeks.get(0).getName();
                    }else{
                        weekname = "第一周";
                    }
                    setTitle(weekname);
                    fragment1.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                            "?type=week&key="+weekid+"&role="+1+"&pfmb="+"&name="+weekname);
                }else if(tabSelect == 1){
                    setTitle(TimeUtils.getCurrMonth());
                    fragment2.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                            "?type=month&key="+TimeUtils.getCurrMonth()+"&role="+1+"&pfmb="+"&name="+TimeUtils.getCurrMonth());
                }else if(tabSelect == 2){
                    if(mSemester.size()>0){
                        semesterid = mSemester.get(0).getId();
                        templetid = mTemplet.get(0).getId();
                        semestername = mSemester.get(0).getName();
                    }else{
                        semestername= "第一学期";
                    }
                    setTitle(semestername);
                    fragment3.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                            "?type=year&key="+semesterid+"&role="+1+"&pfmb="+templetid+"&name="+semestername);
                }
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
     * 获取周次
     */
    public void getWeeks(){
        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/week", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mWeeks.addAll(dfxxes);
                                weekname = dfxxes.get(0).getName();
                                setTitle(weekname);
                                weekid = dfxxes.get(0).getId();
                                fragment1.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                                        "?type=week&key="+weekid+"&role="+1+"&pfmb="+"&name="+weekname);
                            }else{

                            }
                        } else {
                        }
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    /**
     * 获取学期
     */
    public void getSemesters(){
        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/year", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mSemester.addAll(dfxxes);
                            }else{

                            }
                        } else {
                        }
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    /**
     * 获取模板
     */
    public void getTempl(){
        HttpUtil.getInstance().postJsonObjectRequest("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/pfmb", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                mTemplet.addAll(dfxxes);
                            }else{

                            }
                        } else {
                        }
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

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
                    fragment1.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                            "?type=week&key="+weekid+"&role="+1+"&pfmb="+"&name="+weekname);
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    month = data.getStringExtra("date");
                    setTitle(month);
                    fragment2.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                            "?type=month&key="+month+"&role="+1+"&pfmb="+"&name="+month);
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    semesterid = data.getStringExtra("xqsid");
                    setTitle(data.getStringExtra("xqs"));
                    templetid = data.getStringExtra("mbs");
                    fragment3.setMUrl("http://192.168.3.127:8082/mp-lgj/apps/sswsdftj/tj" +
                            "?type=year&key="+semesterid+"&role="+1+"&pfmb="+templetid+"&name="+data.getStringExtra("xqs"));
                }
                break;
        }
    }



}