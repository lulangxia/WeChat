package com.zjl.mywechat.login.modle;

/**
 * Created by dllo on 16/10/31.
 */

public interface ILoginModel {
    void startLogin(String username,String password,OnFinishListener onFinishListener);
}
