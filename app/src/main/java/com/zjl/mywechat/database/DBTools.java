package com.zjl.mywechat.database;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.litesuits.orm.LiteOrm;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.bean.RequestBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBTools {

    private static DBTools sDbTools;
    private LiteOrm mLiteOrm;
    private ExecutorService threadPool;
    private Handler mHandler;


    private DBTools() {
        //
        String name = EMClient.getInstance().getCurrentUser();
        mLiteOrm = LiteOrm.newSingleInstance(MyApp.getmContext(), name + "myDataBase.db");
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        mHandler = new Handler(Looper.getMainLooper());
    }


    // 单例模式
    public static DBTools getInstance() {
        if (sDbTools == null) {
            synchronized (DBTools.class) {
                if (sDbTools == null) {
                    sDbTools = new DBTools();
                    Log.d("DBTools", "sDbTools");
                }
            }
        }
        return sDbTools;
    }

    public LiteOrm getmLiteOrm() {
        return mLiteOrm;
    }


    public void setDbToolsNull() {
        sDbTools = null;
    }

    // 需要封装

    // 以类名为表名创建表


    // 插入单条数据（传入单个对象）
    public <T> void insert(final T bean) {
        if (bean == null) {
            Log.d("DBTools", "kong");
            return;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("DBTools", "添加成功DBTools");
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


    // 删除单条数据(传一个bean)
    public <T> void delete(final T bean) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                mLiteOrm.delete(bean);
            }
        });
    }


    // 删除全部数据
    public <T> void delete(final Class<T> clazz) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                mLiteOrm.delete(clazz);
            }
        });
    }


    // 修改单条数据


    // 查询所有数据
    public <T> void getAll(final QueryListener<T> queryListener,final Class<T> clazz) {
     //   final Class<T> clazz = null;
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 参数是(实体类.class)
                final ArrayList<T> been = mLiteOrm.query(clazz);

                mHandler.post(new HandlerRunnable<>(been, queryListener));
            }
        });



    }











    class QueryRunnable<T> implements Runnable {
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
    public interface QueryListener<T> {
        // 将接口用泛型去实现
        // 当查询完成后,将查到的数据作为data 返回给Activity等
        void onQuery(ArrayList<T> persons);
    }








    public interface CheckListener<T> {
        void onCheck(RequestBean bean);
    }





    public <T> void check(final CheckListener<T> listener, final RequestBean requestBean) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 子线程进行数据的查询
                RequestBean bean = mLiteOrm.queryById(requestBean.getName(), RequestBean.class);
                if (bean!=null) {

                    Log.d("DBTools", "name:" + bean.getName());
                }
                listener.onCheck(bean);

            }
        });
    }






//    public int getCount() {
//
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                // 要去查找未读的消息个数
//                final List<RequestBean> queryList =  mLiteOrm.query(RequestBean.class);
//            }
//        });
//        return 0;
//    }












    private class CountRunnable implements Runnable {
        public CountRunnable(List<RequestBean> queryList) {


        }

        @Override
        public void run() {

        }
    }



}
