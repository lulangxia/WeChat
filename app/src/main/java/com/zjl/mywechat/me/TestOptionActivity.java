package com.zjl.mywechat.me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.login.view.LoginActivity;
import com.zjl.mywechat.main.MainActivity;

/**
 * Created by dllo on 16/10/26.
 */

public class TestOptionActivity extends BaseAty {

    private Button mButton;

    @Override
    protected int setLayout() {
        return R.layout.testoption;
    }

    @Override
    protected void initView() {
        mButton = bindView(R.id.outlogin);

    }

    @Override
    protected void initData() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });

    }

    void logout() {

        final ProgressDialog pd = new ProgressDialog(TestOptionActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                      // MyApp.getInstance().setCurrentUserName("");

                        // 将数据库对象置空，这样下次登录新号会新建一个库
                        DBTools.getInstance().setDbToolsNull();

                        startActivity(new Intent(TestOptionActivity.this, LoginActivity.class));

                        MainActivity.instance.finish();
                        finish();
                    }
                });


            }

            @Override
            public void onError(int i, String s) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(TestOptionActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
