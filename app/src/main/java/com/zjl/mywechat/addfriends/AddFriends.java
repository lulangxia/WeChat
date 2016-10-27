package com.zjl.mywechat.addfriends;


import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;


public class AddFriends extends BaseAty implements View.OnClickListener {


    private EditText etNum;
    private ImageView ivSearch;
    private ListView lv;

    @Override
    protected int setLayout() {
        return R.layout.activity_addfriends;
    }

    @Override
    protected void initView() {

        Toolbar toolbar = bindView(R.id.toolbar_addfriend);
        toolbar.setTitle("添加朋友");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.fx_icon_back_n);


        etNum = bindView(R.id.et_addfriend_usertel);
        // 查询按钮
        ivSearch = bindView(R.id.btn_search);
        lv = bindView(R.id.lv_addfriend_queryfriend);








    }

    @Override
    protected void initData() {

        // 点击事件
        ivSearch.setOnClickListener(this);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:

                search();
                break;


        }
    }


    private void search() {

        String num = etNum.getText().toString();



    }





}
