package com.zjl.mywechat.mvp.model;


import com.zjl.mywechat.bean.RequestBean;

public interface ModelInterface<T> {

    // 收到消息监听 进行数据库的增删改查操作
    // 耗时操作要用回调的方法去实现


//    应该分成三个表分别去建？？？？？
//    void insertDBUnRead();
//    void insertDBUnAgree();
//    void insertDBUnKnown();

    // 插入
    void insertDB(T bean);

    // 删除
    void delete(T bean);

    // 查询
    void query(OnFinishListener listener);

    // 检索表中是否有该数据
    void checkDB(IsHasThisData listener, RequestBean bean);





//    int getCount();




}
