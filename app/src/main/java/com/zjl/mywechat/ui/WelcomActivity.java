package com.zjl.mywechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.zjl.mywechat.R;
import com.zjl.mywechat.login.view.LoginActivity;
import com.zjl.mywechat.main.MainActivity;

public class WelcomActivity extends AppCompatActivity {
    private RelativeLayout rootLayout;
    private boolean autoLogin;

    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        rootLayout = (RelativeLayout) findViewById(R.id.activity_welcom);
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if (EMClient.getInstance().isLoggedInBefore()) {
                    autoLogin = true;
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();

                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    startActivity(new Intent(WelcomActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(WelcomActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();
    }
}
