package com.zjl.mywechat.addfriends;


import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.BaseListViewAdapter;
import com.zjl.mywechat.base.BaseListViewHolder;

import java.util.ArrayList;

public class RequestActivity extends BaseAty {


    private ListView lv;

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


        // 里面的数据要从网络端获取
        ArrayList<RequestBean> been = new ArrayList<>();


        lv.setAdapter(new BaseListViewAdapter<RequestBean>(MyApp.getmContext(), been, R.layout.item_personrequest) {
            @Override
            public void convent(BaseListViewHolder viewHolder, RequestBean requestBean) {
                viewHolder.setText(R.id.tv_add_personname, requestBean.getName());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    protected void initData() {




    }





}
