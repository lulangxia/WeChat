package com.zjl.mywechat.mvp.presenter;

import android.util.Log;

import com.zjl.mywechat.mvp.model.Model;
import com.zjl.mywechat.mvp.model.ModelInterface;
import com.zjl.mywechat.mvp.model.OnFinishListener;
import com.zjl.mywechat.mvp.view.MainView;

public class MainPresenter {

    private MainView view;
    private ModelInterface model;


    public MainPresenter(MainView view) {
        this.view = view;
        // 利用多态生成不同种类的model层对象
        model = new Model<>();
    }



    public <T> void onInsert(T bean) {
        model.insertDB(bean);
    }



    public void onQuery() {


        model.query(new OnFinishListener() {
            @Override
            public void onSuccess() {
                view.showMessageView();

                Log.d("MainPresenter", "显示联系人的信息");

            }

            @Override
            public void onError() {
                view.showMessageView();

            }
        });


//        view.showMessageView();
//        view.showUnAgreeView();
//        view.showUnKnownView();


    }










}
