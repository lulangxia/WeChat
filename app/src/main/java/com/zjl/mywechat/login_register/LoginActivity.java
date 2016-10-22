package com.zjl.mywechat.login_register;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;

public class LoginActivity extends BaseAty{


    private Toolbar mToolbar;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mToolbar = bindView(R.id.toolbar_login);
    }

    @Override
    protected void initData() {
        mToolbar.setLogo(R.mipmap.fx_icon_back_n);
        mToolbar.setTitle("请用手机好登陆");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
    }
}
