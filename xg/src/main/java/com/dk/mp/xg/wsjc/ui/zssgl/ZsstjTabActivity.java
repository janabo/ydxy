package com.dk.mp.xg.wsjc.ui.zssgl;

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
import com.dk.mp.core.util.encrypt.Base64Utils;
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

/**住宿生统计首界面
 * 作者：janabo on 2017/1/18 11:42
 */
public class ZsstjTabActivity extends MyActivity {
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private String title;
    private String type;//1,2,3;调宿 停宿 退宿
    private String role;//角色 1:班主任，2:辅导员，3:系部，4:学工/宿管
    private LinearLayout dropdown;
    private int tabSelect = 0;
    List<Common> mWeeks = new ArrayList<>();//周
    private String weekid="";//选择的周次
    private String weekname="";//选择的周次名
    private WsjcTjFragment fragment1 = new WsjcTjFragment();
    private WsjcTjFragment fragment2 = new WsjcTjFragment();
    private WsjcTjFragment fragment3 = new WsjcTjFragment();
    private ImageView dropdown_img;
    LoginMsg loginMsg;

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjctj_tab;
    }

    @Override
    protected void initialize() {
        super.initialize();
        loginMsg = getSharedPreferences().getLoginMsg();
        type = getIntent().getStringExtra("lx");
        role = getIntent().getStringExtra("role");
        setTitle(getIntent().getStringExtra("title"));
        findView();
        initViewPager();
        getClassesorDepartments();
    }

    private void findView(){
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
        dropdown = (LinearLayout) findViewById(R.id.dropdown);
        dropdown_img = (ImageView) findViewById(R.id.dropdown_img);

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWeeks.size() > 0) {
                    Intent intent = new Intent(mContext, ZsstjPickActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("kfs", (Serializable) mWeeks);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
                if(tabSelect == 0){
                    dropdown_img.setVisibility(View.VISIBLE);
//                    fragment1.setMUrl("apps/zxzssgltj/tongji" +
//                            "?id="+weekid+"&type=today&lmlb="+type);
                    fragment1.setMUrl(getUrl(weekid,"today",type));
                }else if(tabSelect == 1){
//                    fragment2.setMUrl("apps/zxzssgltj/tongji" +
//                            "?id="+weekid+"&type=week&lmlb="+type);
                    fragment2.setMUrl(getUrl(weekid,"week",type));
                    dropdown_img.setVisibility(View.VISIBLE);
                }else if(tabSelect == 2){
                    dropdown_img.setVisibility(View.VISIBLE);
//                    fragment3.setMUrl("apps/zxzssgltj/tongji" +
//                            "?id="+weekid+"&type=month&lmlb="+type);
                    fragment3.setMUrl(getUrl(weekid,"month",type));
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
     * 获取班级h或院系
     */
    public void getClassesorDepartments(){
        Map<String,Object> map = new HashMap<>();
        map.put("lmlb",type);
        HttpUtil.getInstance().postJsonObjectRequest("apps/zxzssgltj/selected", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null) {
                        GsonData<Common> gsonData = new Gson().fromJson(result.toString(), new TypeToken<GsonData<Common>>() {
                        }.getType());
                        if (gsonData.getCode() == 200) {
                            List<Common> dfxxes = gsonData.getData();
                            if(dfxxes.size()>0){//获取数据不为空
                                dropdown_img.setVisibility(View.VISIBLE);
                                mWeeks.addAll(dfxxes);
                                weekname = dfxxes.get(0).getName();
                                setTitle(weekname);
                                weekid = dfxxes.get(0).getId();
//                                fragment1.setMUrl("apps/zxzssgltj/tongji" +
//                                        "?role="+role+"&id="+weekid+"&type=today&lx="+type);
                                fragment1.setMUrl(getUrl(weekid,"today",type));
                            }else{
                                showErrorMsg(mViewpager,getErrorMsg());
                            }
                        } else {
                            showErrorMsg(mViewpager,getErrorMsg());
                        }
                    }else{
                        showErrorMsg(mViewpager,getErrorMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg(mViewpager,getErrorMsg());
                }
            }

            @Override
            public void onError(VolleyError error) {
                showErrorMsg(mViewpager,getErrorMsg());
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
                    if(tabSelect == 0) {
                        fragment1.setMUrl(getUrl(weekid, "today", type));
                    }else if(tabSelect == 1) {
                        fragment2.setMUrl(getUrl(weekid, "week", type));
                    }else if(tabSelect == 2){
                        fragment3.setMUrl(getUrl(weekid, "month", type));
                    }
                }
                break;
        }
    }

    /**
     * 显示错误名称
     * @return
     */
    public String getErrorMsg(){
        dropdown_img.setVisibility(View.INVISIBLE);
//        fragment1.setMUrl("apps/zxzssgltj/tongji" +
//                "?role="+role+"&id="+weekid+"&type=today&lx="+type);
        fragment1.setMUrl(getUrl(weekid,"today",type));
        if("1".equals(role) || "2".equals(role) || "3".equals(role)){
            return "未获取到班级列表";
        }else{
            return "未获取到院系列表";
        }
    }

    public String getUrl(String id,String type,String lx){
        String mUrl = "apps/zxzssgltj/tongji?id="+id+"&type="+type+"&lmlb="+lx;
        if(loginMsg != null){
            mUrl += "&uid="+loginMsg.getUid()+"&pwd="+ loginMsg.getPsw();
        }
        return mUrl;
    }

}
