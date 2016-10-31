package com.zjl.mywechat.login.view;

/**
 * Created by dllo on 16/10/31.
 */

public interface ILoginView {
    void showDialog();

    void dismissDialog();

    void onResponse(String username, String password);

    void onError(int i, String s);
}
