package com.zjl.mywechat.register.model;

/**
 * Created by dllo on 16/10/31.
 */

public interface IRegisterModel {

    void startRegister(String username,String password,OnFinishedListener listener);
}
