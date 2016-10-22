package com.zjl.mywechat.login_register;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.staticfinal.StringStatic;

public class LoginActivity extends BaseAty implements View.OnClickListener {

    private static final int REQUEST_CODE = 200;
    private Toolbar mToolbar;
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mLogin = bindView(R.id.btn_qtlogin);
        mToolbar = bindView(R.id.toolbar_login);
        mUsername = bindView(R.id.et_usertel);
        mPassword = bindView(R.id.et_password);
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("使用手机号登陆");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.toolback);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qtlogin:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, StringStatic.RESULTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==StringStatic.RESULTCODE&&resultCode==StringStatic.REQUESTCODE){
            Log.d("LoginActivity", data.getStringExtra(StringStatic.USERNAME));
            mUsername.setText(data.getStringExtra(StringStatic.USERNAME));
            mPassword.setText(data.getStringExtra(StringStatic.PASSWORD));
        }
    }
}
