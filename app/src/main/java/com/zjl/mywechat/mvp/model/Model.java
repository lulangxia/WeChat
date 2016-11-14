package com.zjl.mywechat.mvp.model;


import android.util.Log;

import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.database.DBTools;

import java.util.ArrayList;

// 需要在前面再声明下，不然后面的 'T' 表示一个类
public class Model<Q, T> implements ModelInterface<T>{



    // DataBase，message表

    @Override
    public void insertDB(T bean) {
        DBTools.getInstance().insert(bean);
        Log.d("Model", "添加成功");
    }



    @Override
    public void delete(T bean) {
        DBTools.getInstance().delete(bean);
    }



    @Override
    public void query(final OnFinishListener listener) {
        DBTools.getInstance().getAll(new DBTools.QueryListener<RequestBean>() {
            @Override
            public void onQuery(ArrayList<RequestBean> persons) {
                if (persons.size() == 0) {
                    // 不能判空，要去判断长度
                    listener.onError();
                } else {
                    // 如果非空
                    listener.onSuccess(persons);
                }
            }
        }, RequestBean.class);
    }




    // 查重
    @Override
    public void checkDB(final IsHasThisData listener, RequestBean bean) {

        DBTools.getInstance().check(new DBTools.CheckListener<RequestBean>() {
            @Override
            public void onCheck(RequestBean requestBean) {
                if (requestBean == null) {
                    listener.noThisData();
                } else if (requestBean.getIsAgree() == 1) {
                    // 已经同意了好友请求
                    listener.existThisData();
                } else {
                    // 拒绝请求2 或者 未读请求0
                    listener.unAgreeThisData();
                }
            }
        }, bean);
    }



//    @Override
//    public int getCount() {
//        return DBTools.getInstance().get;
//    }


}
