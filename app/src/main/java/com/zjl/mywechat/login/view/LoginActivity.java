package com.zjl.mywechat.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.login.presenter.LoginPresrnter;
import com.zjl.mywechat.main.MainActivity;
import com.zjl.mywechat.register.view.RegisterActivity;
import com.zjl.mywechat.socalfriend.modle.PreferenceManager;
import com.zjl.mywechat.tool.stringvalue.StringStatic;

public class LoginActivity extends BaseAty implements View.OnClickListener, ILoginView {

    private static final int REQUEST_CODE = 200;
    private Toolbar mToolbar;
    private Button mRegister, mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private LoginPresrnter mPresrnter;


    private boolean autoLogin;
    private ProgressDialog mDialog;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mRegister = bindView(R.id.btn_qtlogin);
        mToolbar = bindView(R.id.toolbar_login);
        mUsername = bindView(R.id.et_usertel);
        mPassword = bindView(R.id.et_password);
        mLogin = bindView(R.id.btn_login);

        mPresrnter = new LoginPresrnter(this);
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("使用手机号登陆");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.fx_top_bar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        // 如果用户名改变，清空密码
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDialog = createDialog();
        mRegister.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mUsername.addTextChangedListener(new TextChange());
        mPassword.addTextChangedListener(new TextChange());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qtlogin:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, StringStatic.RESULTCODE);
                break;
            case R.id.btn_login:
                //login();
                mPresrnter.startRequest(mUsername.getText().toString().trim(), mPassword.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StringStatic.RESULTCODE && resultCode == StringStatic.REQUESTCODE) {

            mUsername.setText(data.getStringExtra(StringStatic.USERNAME));
            mPassword.setText(data.getStringExtra(StringStatic.PASSWORD));
        }
    }

    @Override
    public void showDialog() {
        mDialog.show();
    }

    @Override
    public void dismissDialog() {
        mDialog.dismiss();
    }

    @Override
    public void onResponse(final String username, String password) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LoginActivity", "denglu");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                PreferenceManager.getIntance().setCurrentUserName(username);
                Log.d("LoginActivity",PreferenceManager.getIntance().getCurrentUserName());

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                /**
                 * 关于错误码可以参考官方api详细说明
                 * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                 */
                switch (i) {
                    // 网络异常 2
                    case EMError.NETWORK_ERROR:
                        Toast.makeText(LoginActivity.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 无效的用户名 101
                    case EMError.INVALID_USER_NAME:
                        Toast.makeText(LoginActivity.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 无效的密码 102
                    case EMError.INVALID_PASSWORD:
                        Toast.makeText(LoginActivity.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 用户认证失败，用户名或密码错误 202
                    case EMError.USER_AUTHENTICATION_FAILED:
                        Toast.makeText(LoginActivity.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 用户不存在 204
                    case EMError.USER_NOT_FOUND:
                        Toast.makeText(LoginActivity.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 无法访问到服务器 300
                    case EMError.SERVER_NOT_REACHABLE:
                        Toast.makeText(LoginActivity.this, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 等待服务器响应超时 301
                    case EMError.SERVER_TIMEOUT:
                        Toast.makeText(LoginActivity.this, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 服务器繁忙 302
                    case EMError.SERVER_BUSY:
                        Toast.makeText(LoginActivity.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    // 未知 Server 异常 303 一般断网会出现这个错误
                    case EMError.SERVER_UNKNOWN_ERROR:
                        Toast.makeText(LoginActivity.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            boolean Sign1 = mUsername.getText().length() > 0;
            boolean Sign2 = mPassword.getText().length() > 0;

            if (Sign1 & Sign2) {
                mLogin.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                mLogin.setEnabled(false);
            }
        }


    }

    public ProgressDialog createDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在登陆，请稍后...");
        return dialog;
    }


    private long clickTime = 0; //记录第一次点击的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出应用",
                    Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }


}
