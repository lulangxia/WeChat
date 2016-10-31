package com.zjl.mywechat.addfriends;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.bean.RequestBean;

import java.util.ArrayList;

public class RequestActivity extends BaseAty {


    private ListView lv;
    private ArrayList<RequestBean> been;
//    private UnAgreeRequest receiver;

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

//        receiver = new UnAgreeRequest();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("加好友");
//        registerReceiver(receiver, filter);

        // 里面的数据要从网络端获取
        been = new ArrayList<>();




        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String reason = intent.getStringExtra("reason");
        RequestBean bean = new RequestBean(name, reason);




        // 添加数据
        been.add(bean);

        Lv adapter = new Lv(this);
        adapter.setArrayList(been);
        lv.setAdapter(adapter);





    }


//    private class UnAgreeRequest extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String name = intent.getStringExtra("name");
//            Log.d("UnAgreeRequest", "name" + name);
//            String reason = intent.getStringExtra("reason");
//            Log.d("UnAgreeRequest", "reason" + reason);
//
//            RequestBean requestBean = new RequestBean(name, reason);
//
//            been.add(requestBean);
//            lv.setAdapter(new BaseListViewAdapter<RequestBean>(RequestActivity.this, been, R.layout.item_personrequest) {
//                @Override
//                public void convent(BaseListViewHolder viewHolder, RequestBean requestBean) {
//                    for (int i = 0; i < 100; i++) {
//                        viewHolder.setText(R.id.tv_request_name, "????");
//                    }
//                    // viewHolder.setText(requestBean.getName());
//                }
//            });
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unregisterReceiver(receiver);
    }
}
