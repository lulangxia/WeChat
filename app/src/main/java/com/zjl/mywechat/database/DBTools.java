package com.zjl.mywechat.database;


import android.os.Handler;
import android.os.Looper;

import com.litesuits.orm.LiteOrm;
import com.zjl.mywechat.base.MyApp;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBTools {

    private static DBTools sDbTools;
    private LiteOrm mLiteOrm;
    private ExecutorService threadPool;
    private Handler mHandler;


    private DBTools() {
        mLiteOrm = LiteOrm.newSingleInstance(MyApp.getMcontext(), "MyDataBase.db");
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        mHandler = new Handler(Looper.getMainLooper());
    }

    // 单例模式
    public static DBTools getInstance() {
        if (sDbTools == null) {
            synchronized (DBTools.class) {
                if (sDbTools == null) {
                    sDbTools = new DBTools();
                }
            }
        }
        return sDbTools;
    }


    // 插入单条数据
    public <T> void insert(final T bean) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                mLiteOrm.insert(bean);
            }
        });
    }

    // 插入集合
    public <T> void insert(final ArrayList<T> been) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                mLiteOrm.insert(been);
            }
        });
    }




    // 查询所有数据
    public void getAll(final QueryListener<PersonBean> queryListener) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 参数是(实体类.class)
                final  ArrayList<PersonBean> been = mLiteOrm.query(PersonBean.class);
                mHandler.post(new HandlerRunnable<>(been, queryListener));
            }
        });
    }




    class QueryRunnable<T> implements Runnable{
        private Class<T> mTClass;
        private QueryListener<T> queryListener;

        public QueryRunnable(Class<T> mTClass, QueryListener<T> queryListener) {
            this.mTClass = mTClass;
            this.queryListener = queryListener;
        }
        @Override
        public void run() {
            ArrayList<T> tArrayList = mLiteOrm.query(mTClass);
            mHandler.post(new HandlerRunnable<>(tArrayList, queryListener));
        }
    }





    class HandlerRunnable<T> implements Runnable {

        private ArrayList<T> mTArrayList;
        private QueryListener<T> mTQueryListener;

        public HandlerRunnable(ArrayList<T> tArrayList, QueryListener<T> queryListener) {
            mTArrayList = tArrayList;
            mTQueryListener = queryListener;
        }
        @Override
        public void run() {
            mTQueryListener.onQuery(mTArrayList);
        }
    }




    // 查询完成后的回调接口
    interface QueryListener<T>{
        // 将接口用泛型去实现
        // 当查询完成后,将查到的数据作为data 返回给Activity等
        void onQuery(ArrayList<T> persons);
    }




}
