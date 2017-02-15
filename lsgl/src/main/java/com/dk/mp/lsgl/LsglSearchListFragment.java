package com.dk.mp.lsgl;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.lsgl.entity.PersonEntity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongqs on 2017/2/4.
 */

public class LsglSearchListFragment extends BaseFragment{

    private MyListView myListView;
    private List<PersonEntity> list = new ArrayList<PersonEntity>();
    private String role;
    private String type;
    private String key;

    private boolean canFk;
    private boolean canBz;

    @Override
    protected int getLayoutId() {
        return R.layout.app_lsgl_list;
    }

    @Override
    protected void initialize(View view) {
        super.initialize(view);
        myListView = (MyListView) view.findViewById(R.id.lsgllistview);
        myListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myListView.setAdapterInterface(list,adapterInterface);
        initDatas();
    }

    private void initDatas(){
        myListView.startRefresh();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", myListView.pageNo);
        map.put("type", type);
        map.put("role", role);
        map.put("key", key);

        if(!DeviceUtil.checkNet()){//判断是否有网络
            myListView.error(MyListView.Error.NoNetwork);
            return;
        }
        HttpUtil.getInstance().postJsonObjectRequest("apps/lsxsgl/ss", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<PersonEntity> persionList = getGson().fromJson(result.getJSONObject("data").getJSONArray("list").toString(),new TypeToken<List<PersonEntity>>(){}.getType());
                        if (persionList == null){
                            myListView.error(MyListView.Error.OnError);
                        } else if (persionList.size() == 0) {
                            myListView.error(MyListView.Error.NoDatas);
                        } else {
                            canFk = result.getJSONObject("data").optBoolean("canFk");
                            canBz = result.getJSONObject("data").optBoolean("canBz");
                            if (myListView.pageNo == 1) {
                                list.clear();
                            }
                            list.addAll(persionList);
                            myListView.flish();
                        }
                    } catch (JSONException e) {
                        myListView.error(MyListView.Error.OnError);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                myListView.error(MyListView.Error.OnError);
            }
        });
    }

    private class MyView extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView roomid;
        private TextView mjjl;
        private Button mess;

        public MyView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);// 取得实例
            roomid = (TextView) itemView.findViewById(R.id.roomid);// 取得实例
            mjjl = (TextView) itemView.findViewById(R.id.mjjl);// 取得实例
            mess = (Button) itemView.findViewById(R.id.mess);// 取得实例
        }
    }

    Handler handler=new Handler();
    boolean isposting = false;

    private AdapterInterface adapterInterface = new AdapterInterface() {
        @Override
        public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
            final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.app_lsgl_list_item, parent, false);// 设置要转化的layout文件
            view1.findViewById(R.id.doclick).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isposting){
                        isposting = true;
                        handler.postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                Intent intent = new Intent(getContext(),LsglInfoActivity.class);
                                intent.putExtra("id",list.get(Integer.valueOf(view1.getTag().toString())).getId());
                                intent.putExtra("type",type);
                                intent.putExtra("canFk",canFk);
                                intent.putExtra("canBz",canBz);
//                                startActivityForResult(intent,0);view.setTransitionName("item");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "item");
                                ActivityCompat.startActivity(getActivity(),intent, options.toBundle());
                                isposting = false;
                            }
                        },500);
                    }

                }
            });
            return new MyView(view1);
        }
        @Override
        public void setItemValue(RecyclerView.ViewHolder holder, int position) {
            ((MyView)holder).name.setText(list.get(position).getName());
            ((MyView)holder).roomid.setText(type.equals("1")?list.get(position).getRoom():list.get(position).getClassName());
            ((MyView)holder).mjjl.setText(list.get(position).getMsg());
            if (role.equals("4")) {
                ((MyView)holder).mess.setVisibility(View.GONE);
            } else {
                ((MyView)holder).mjjl.setVisibility(View.GONE);
            }
        }
        @Override
        public void loadDatas() {
            initDatas();
        }
    };

    public void reloadDatas(String key){
        this.key = key;
        myListView.reLoadDatas();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}


