package com.zjl.mywechat.database;


public interface DataAction {

    // 添加
    void inset();



    // 删除
    void delete();



    // 查询
    void query();





    // 内部类可以直接调用外部类的对象

    // 静态内部类可以直接外部类.内部类()或者直接new静态内部类，不会加载外部类的对象，
    // 只能调用外部类的静态属性，不能调用外部类的其他属性  外部类存在的意义相当于一个路径

    // 内存泄漏问题（外部类的对象在内存空间的生命周期）

    // 内部类必须要用外部类的对象.内部类()，



}
