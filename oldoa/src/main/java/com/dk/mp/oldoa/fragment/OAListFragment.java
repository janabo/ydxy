package com.dk.mp.oldoa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.oldoa.R;
import com.dk.mp.oldoa.activity.BaoGaoDetailActivity;
import com.dk.mp.oldoa.activity.FaWenDetailActivity;
import com.dk.mp.oldoa.activity.ShouWenDetailActivity;
import com.dk.mp.oldoa.adapter.DocAdapter;
import com.dk.mp.oldoa.entity.Doc;
import com.dk.mp.oldoa.entity.OATab;
import com.dk.mp.oldoa.manager.OAManager;
import com.dk.mp.oldoa.utils.Constant;
import com.dk.mp.oldoa.utils.http.HttpClientUtil;
import com.dk.mp.oldoa.view.XListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/4/1 15:04
 */
public class OAListFragment extends BaseFragment implements View.OnClickListener,XListView.IXListViewListener{
    public static final String ACTION_REFRESH = "com.test.action.refresh";
    public static final String ARGS_TABS = "args_tabs";
    private OATab mOaTabs;
    private ErrorLayout mError;
    private LinearLayout mRootView;
    DocAdapter docAdapter;
    String state;//0代表待处理，1代表已处理
    CoreSharedPreferencesHelper shareHelper;
    String interfaceUri;
    private int curPage = 1;
    private int countPage =1;
    XListView listview;
    private List<Doc> mList = new ArrayList<>();

    public static OAListFragment newInstance(OATab oaTabs) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_TABS,oaTabs);
        OAListFragment fragment = new OAListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOaTabs = getArguments().getParcelable(ARGS_TABS);
    }


      @Override
    protected void initialize(View view) {
        super.initialize(view);
          shareHelper = new CoreSharedPreferencesHelper(getContext());
          state = mOaTabs.getState();
          interfaceUri = mOaTabs.getUrl();
          mError = (ErrorLayout) view.findViewById(R.id.error_layout);
          mRootView = (LinearLayout) view.findViewById(R.id.mRootView);
          listview = (XListView) view.findViewById(R.id.listView);

          listview.setPullLoadEnable(true);
          listview.setXListViewListener(this);
          listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                  Doc doc = (Doc) adapter.getItemAtPosition(position);
                  Intent intentActivity = new Intent();
                  if ("OA_SW".equals(doc.getType())) {
                      intentActivity.setClass(getContext(), ShouWenDetailActivity.class);
                      intentActivity.putExtra("dealState", state);
                      intentActivity.putExtra("title", doc.getTitle());
                      intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
                      startActivity(intentActivity);
                  } else if ("OA_FW".equals(doc.getType())) {
                      intentActivity.setClass(getContext(), FaWenDetailActivity.class);
                      intentActivity.putExtra("dealState", state);
                      intentActivity.putExtra("title", doc.getTitle());
                      intentActivity.putExtra("bzid", doc.getId());
                      intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
                      startActivity(intentActivity);
                  } else {
                      intentActivity.setClass(getContext(), BaoGaoDetailActivity.class);
                      intentActivity.putExtra("dealState", state);
                      intentActivity.putExtra("title", doc.getTitle());
                      intentActivity.putExtra(Constant.TYPE_URL, doc.getUrl());
                      startActivity(intentActivity);
                  }

              }
          });
          //模拟登陆 TODO
//		simulateLogin();
          if (DeviceUtil.checkNet()) {
              getListData();
          }
    }



    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.oa_list;
    }

    public void getListData() {
//		showProgressDialog();
        Map<String,String> map = new HashMap<String, String>();
        map.put("pageNo", curPage+"");
        HttpClientUtil.post(interfaceUri, map, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                handler.sendEmptyMessage(-1);
            }
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                PageMsg page = OAManager.getIntence().getOAListInfos(arg0);
                mList.addAll(page.getList());
                countPage = (int) page.getTotalPages();
                if(mList.size()>0){
                    handler.sendEmptyMessage(0);
                }else{
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    // 定义一个Handler，用来异步处理数据
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            if ("0".equals(state)) {
//                Intent intent = new Intent(Constant.UPDATE_COUNT_UI);
//                intent.putExtra("count", mList !=null ?String.valueOf(mList.size()):0);
//                sendBroadcast(intent);
//            }
            switch (msg.what) {
                case 0:
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    if (docAdapter == null) {
                        docAdapter = new DocAdapter(getContext(), mList);
                        listview.setAdapter(docAdapter);
                    } else {
                        docAdapter.setList(mList);
                        docAdapter.notifyDataSetChanged();
                    }
                    if (curPage >= countPage) {
                        listview.hideFooter();
                    } else {
                        listview.showFooter();
                    }
                    listview.stopRefresh();
                    listview.stopLoadMore();
                    break;
                case 1:
                    mError.setErrorType(ErrorLayout.NODATA);
                    break;
                case 2:
                    mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    SnackBarUtil.showShort(mRootView,"没有获取到更多数据");
                    break;
                case -1:
//				showMessage("获取数据失败");
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onRefresh() {
        if (DeviceUtil.checkNet()) {
            curPage = 1;
            getListData();
        } else {
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            listview.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (DeviceUtil.checkNet()) {
            curPage++;
            getDataLoadMore();
        }
    }

    @Override
    public void stopLoad() {

    }

    @Override
    public void getList() {

    }

    public void getDataLoadMore() {
        if (DeviceUtil.checkNet()) {
            Map<String,String> map = new HashMap<String, String>();
            map.put("pageNo", curPage+"");
            HttpClientUtil.post(interfaceUri, map, new RequestCallBack<String>() {
                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    handler.sendEmptyMessage(-1);
                }
                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    PageMsg page = OAManager.getIntence().getOAListInfos(arg0);
                    countPage = (int) page.getTotalPages();
                    if(page.getList().size()>0){
                        mList.addAll(page.getList());
                        handler.sendEmptyMessage(0);
                    }else{
                        handler.sendEmptyMessage(2);
                    }
                }
            });
        }
    }
}
