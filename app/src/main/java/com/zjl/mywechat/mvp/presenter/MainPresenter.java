package com.zjl.mywechat.mvp.presenter;

import com.zjl.mywechat.mvp.model.Model;
import com.zjl.mywechat.mvp.model.ModelInterface;
import com.zjl.mywechat.mvp.view.MainView;

public class MainPresenter {

    private MainView view;
    private ModelInterface model;


    public MainPresenter(MainView view) {
        this.view = view;
        // 利用多态生成多个model层对象
        model = new Model<>();
    }



    public <T> void onInsert(T bean) {
        model.insertDB(bean);
    }



    public void onQuery() {

        view.showMessageView();
        view.showUnAgreeView();
        view.showUnKnownView();


    }










}
