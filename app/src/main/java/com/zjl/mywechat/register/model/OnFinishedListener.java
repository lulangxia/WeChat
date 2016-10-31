package com.zjl.mywechat.register.model;

/**
 * Created by dllo on 16/10/31.
 */

public interface OnFinishedListener {
    void onFinished(String username,String password);
    void onError(Throwable error);
}
