package com.zjl.mywechat.mvp.presenter;

import android.util.Log;

import com.zjl.mywechat.mvp.model.IsHasThisData;
import com.zjl.mywechat.mvp.model.Model;
import com.zjl.mywechat.mvp.view.MainView;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.mvp.model.ModelInterface;
import com.zjl.mywechat.mvp.model.OnFinishListener;

import java.util.ArrayList;

public class MainPresenter {

    private MainView view;
    private ModelInterface model;


    public MainPresenter(MainView view) {
        this.view = view;
        // 利用多态生成不同种类的model层对象
        model = new Model<>();
    }


    // 主动加好友 被加好友
    // 允许 拒绝
    // 已读 未读

    public void hasData(final RequestBean bean) {

        model.checkDB(new IsHasThisData() {
            @Override
            public void existThisData() {
                // 已经同意，忽略（判断这个最简单）
                Log.d("MainPresenter", "请求1");
            }
            @Override
            public void noThisData() {
                // 不存在这个数据，执行插入操作
                model.insertDB(bean);
                view.showMessageView();
                Log.d("MainPresenter", "请求2");
            }

            @Override
            public void unAgreeThisData() {

                // 存在这个数据
                // 未读，那就刷新置顶（先刷新再添加）
                // 已经拒绝过，那就刷新置顶

                // 先删除再添加
                model.delete(bean);
                model.insertDB(bean);

                Log.d("MainPresenter", "请求3");
            }

        }, bean);
    }





    public <T> void onInsert(T bean) {
        model.insertDB(bean);
    }



    private <T> void onDelete(T bean) {
        model.delete(bean);
    }



    public void onQuery() {
        model.query(new OnFinishListener() {
            @Override
            public void onSuccess(ArrayList<RequestBean> arrayList) {
                view.showUnAgreeView(arrayList);
                Log.d("MainPresenter", "显示联系人的信息");
            }

            @Override
            public void onError() {
                // 提示列表为空
                view.showMessageView();
            }
        });
    }










}
