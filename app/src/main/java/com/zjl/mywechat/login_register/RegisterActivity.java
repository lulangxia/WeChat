package com.zjl.mywechat.login_register;

import com.zjl.mywechat.base.BaseAty;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.zjl.mywechat.R;
import com.zjl.mywechat.staticfinal.StringStatic;

public class RegisterActivity extends BaseAty {

    private Toolbar mToolbar;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected int setLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mToolbar = bindView(R.id.toolbar_register);
        mUsername = bindView(R.id.et_usertel);
        mPassword = bindView(R.id.et_password);

    }

    @Override
    protected void initData() {
        mToolbar.setTitle("填写手机号");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.toolback);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra(StringStatic.USERNAME,mUsername.getText().toString());
                intent.putExtra(StringStatic.PASSWORD,mPassword.getText().toString());
                Log.d("RegisterActivity", "mPassword.getText():" + mUsername.getText().toString());
                setResult(StringStatic.REQUESTCODE,intent);
                finish();
            }
        });
    }
}
