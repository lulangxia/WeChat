package com.zjl.mywechat.login.modle;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by dllo on 16/10/31.
 */

public class LoginModelImp implements ILoginModel {
    @Override
    public void startLogin(final String username, final String password, final OnFinishListener onFinishListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(username, password, new EMCallBack() {

                    @Override
                    public void onSuccess() {

                        onFinishListener.onFinished(username, password);
                    }

                    @Override
                    public void onError(int i, String s) {
                        onFinishListener.onError(i, s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        }).start();




    }
}
