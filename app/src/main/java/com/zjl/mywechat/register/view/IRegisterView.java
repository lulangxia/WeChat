package com.zjl.mywechat.register.view;

/**
 * Created by dllo on 16/10/31.
 */

public interface IRegisterView {
    void showDialog();

    void dismissDialog();

    void onResponse(String username,String password);

    void onError(Throwable error);
}
