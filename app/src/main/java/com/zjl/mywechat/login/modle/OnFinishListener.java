package com.zjl.mywechat.login.modle;

/**
 * Created by dllo on 16/10/31.
 */

public interface OnFinishListener {
    void onFinished(String username, String password);
    void onError(int i, String s);
}
