package com.zjl.mywechat.mvp.model;


import android.util.Log;

import com.zjl.mywechat.database.DBTools;

import java.util.ArrayList;

public class Model<T> implements ModelInterface{



    // DataBase，message表


//    @Override
//    public <T> void insertDB(T bean) {
//        DBTools.getInstance().insert(bean);
//    }


//    @Override
//    public void delete(T bean) {
//        DBTools.getInstance().delete(bean);
//    }



    @Override
    public void insertDB(Object bean) {
        DBTools.getInstance().insert(bean);
        Log.d("Model", "添加成功");
    }


    @Override
    public void delete(Object bean) {
        DBTools.getInstance().delete(bean);
    }








    @Override
    public void query(OnFinishListener listener) {

        DBTools.getInstance().getAll(new DBTools.QueryListener<T>() {
            @Override
            public void onQuery(ArrayList<T> persons) {




            }
        });
    }




}
