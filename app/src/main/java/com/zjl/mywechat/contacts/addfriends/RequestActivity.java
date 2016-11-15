package com.zjl.mywechat.contacts.addfriends;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.BaseListViewAdapter;
import com.zjl.mywechat.base.BaseListViewHolder;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.contacts.adapter.AddContactsAdapter;
import com.zjl.mywechat.mvp.presenter.MainPresenter;
import com.zjl.mywechat.mvp.view.MainView;

import java.util.ArrayList;


// 点进去的 两种请求的 界面
public class RequestActivity extends BaseAty implements MainView{


    private ListView lv;
    //    private ArrayList<RequestBean> been;
    private UnAgreeRequest receiver;

    private MainPresenter presenter;
    private AddContactsAdapter adapter;
    private ArrayList<RequestBean> requestBeen;

    @Override
    protected int setLayout() {
        return R.layout.activity_request;
    }

    @Override
    protected void initView() {

        Toolbar toolbar = bindView(R.id.toolbar_request);
        toolbar.setNavigationIcon(R.mipmap.fx_icon_back_n);
        toolbar.setTitle("添加朋友");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv = bindView(R.id.lv_request);

        presenter = new MainPresenter(this);

        // @InjectView(R.id.button1) Button button1;
        // 需要获取加别人和被别人加的信息
        // EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    protected void initData() {

        // 广播接收器，获取一个请求就要添加一次。。而且以前的还不能删除
        receiver = new UnAgreeRequest();
        IntentFilter filter = new IntentFilter();
//        filter.addAction("加好友");
        filter.addAction("未读消息数目变化");

        registerReceiver(receiver, filter);

        presenter.onQuery();
        requestBeen = new ArrayList<>();

    }

    @Override
    public void showMessageView() {
        // 没有未读消息

    }

    @Override
    public void showUnAgreeView(final ArrayList<RequestBean> arraylist) {
        Log.d("RequestActivity", "arraylist.size():" + arraylist.size());


//        BaseListViewAdapter adapter = new BaseListViewAdapter(this, arraylist, R.layout.item_personrequest) {
//
//            @Override
//            public void convent(BaseListViewHolder viewHolder, Object o) {
//                viewHolder.setText(arraylist.get);
//
//            }
//        };
        requestBeen = arraylist;

        lv.setAdapter(new BaseListViewAdapter<RequestBean>(this, arraylist, R.layout.item_personrequest) {
            @Override
            public void convent(BaseListViewHolder viewHolder, RequestBean requestBean) {
                viewHolder.setText(R.id.tv_request_name, requestBean.getName());
                viewHolder.setText(R.id.tv_request_reason, requestBean.getReason());
            }
        });


        adapter = new AddContactsAdapter(this);
        adapter.setArrayList(arraylist);
        lv.setAdapter(adapter);

    }




    private class UnAgreeRequest extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            RequestBean requestBean = intent.getParcelableExtra("RequestBean");

            Log.d("UnAgreeRequest", requestBean.getName());

            requestBeen.add(requestBean);
            adapter.setArrayList(requestBeen);
            lv.setAdapter(adapter);




        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }
}
