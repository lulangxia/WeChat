package com.zjl.mywechat.register.model;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by dllo on 16/10/31.
 */

public class RegisterModelImp implements IRegisterModel {
    @Override
    public void startRegister(final String username, final String password, final OnFinishedListener listener) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(username, password);//同步方法
                    listener.onFinished(username,password);

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }.start();
    }
}
