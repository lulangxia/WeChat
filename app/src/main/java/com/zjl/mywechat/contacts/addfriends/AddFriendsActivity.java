package com.zjl.mywechat.contacts.addfriends;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.mvp.presenter.MainPresenter;


// 添加好友
public class AddFriendsActivity extends BaseAty implements View.OnClickListener {


    private EditText etNum;
    private ImageView ivSearch;
    //    private ListView lv;
    private TextView tvName;
    private Button btnAdd;
    private RelativeLayout rl;
    private ProgressDialog progressDialog;
    private EditText etAddReason;
    private MainPresenter presenter;


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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etNum = bindView(R.id.et_addfriend_usertel);
        // 查询按钮
        ivSearch = bindView(R.id.iv_search);


        rl = bindView(R.id.add_person_hide);
        tvName = bindView(R.id.tv_add_personname);
        btnAdd = bindView(R.id.btn_addperson);

//        presenter = new MainPresenter(this);


    }

    @Override
    protected void initData() {

        // 点击事件
        ivSearch.setOnClickListener(this);

        btnAdd.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                search();
                break;
            case R.id.btn_addperson:
                addFriends();
                break;

        }
    }


    private void search() {
        String str = etNum.getText().toString();
        if (TextUtils.isEmpty(str)) {
            new EaseAlertDialog(this, "请输入用户名").show();
            return;
        }
        rl.setVisibility(View.VISIBLE);
        tvName.setText(str);
    }


    private void addFriends() {

        // 判断是否是在添加自己
        if (EMClient.getInstance().getCurrentUser().equals(etNum.getText().toString())) {
            new EaseAlertDialog(this, "不能添加自己").show();
            return;
        } else if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(etNum.getText().toString())) {
            new EaseAlertDialog(this, "该好友在你的黑名单里面").show();
            return;
        }

//        else if(){
//            // 判断该名称是否在你的好友列表
//        }


        showDialog();


    }


    private void showDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(AddFriendsActivity.this);
        View viewAdd = LayoutInflater.from(this).inflate(R.layout.dialog_addrequest, null);
        etAddReason = (EditText) viewAdd.findViewById(R.id.et_dialog_addrequest);


        dialog.
                setTitle("添加" + etNum.getText().toString() + "为好友").
                setNegativeButton("确定发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 添加好友进度提示窗口
//                        progressDialog = new ProgressDialog(MyApp.getmContext());
//                        progressDialog.setMessage("正在发送请求");
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        progressDialog.show();

                        newThreadAddFriends();

                    }
                }).
                setPositiveButton("取消发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).
                setView(viewAdd);


        dialog.show();


    }


    private void newThreadAddFriends() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    if (!etAddReason.getText().toString().equals("")) {
                        // 第一个参数是你的名字，第二个参数是加好友的验证消息
                        EMClient.getInstance().contactManager().addContact(etNum.getText().toString(), etAddReason.getText().toString());
                    } else {
                        // 默认验证消息
                        EMClient.getInstance().contactManager().addContact(etNum.getText().toString(), "交个朋友吧");
                    }

                    RequestBean bean = new RequestBean();
                    bean.setName(etNum.getText().toString());
                    bean.setReason(etAddReason.getText().toString());
                    bean.setIsPositive(1);

                    DBTools.getInstance().getmLiteOrm().insert(bean);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            progressDialog.dismiss();
                            Toast.makeText(AddFriendsActivity.this, "请求已发送，请等待", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (HyphenateException e) {
                    Toast.makeText(AddFriendsActivity.this, "发送请求失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }


            }
        }).start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
