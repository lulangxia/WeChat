package com.zjl.mywechat.mvp.view;


import com.zjl.mywechat.bean.RequestBean;

import java.util.ArrayList;

public interface MainView {


    // 应该MainActivity继承这个接口，实现收到消息后的实时监听并显示

    void showMessageView();

    void showUnAgreeView(ArrayList<RequestBean> arraylist);

//    void showUnKnownView();

//    void showNullMessage();










}
