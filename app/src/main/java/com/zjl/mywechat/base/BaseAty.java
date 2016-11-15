package com.zjl.mywechat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zjl.mywechat.tools.ExceptionHandler;

import static com.zjl.mywechat.tools.CreateAndDeleteMap.onCre;
import static com.zjl.mywechat.tools.CreateAndDeleteMap.onDel;


/**
 * Created by dllo on 16/9/19.
 */
public abstract class BaseAty extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());

        // 异常处理日志文件 因为要传this，所以要放到BaseActivity里
        ExceptionHandler handler = new ExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(handler);

        onCre(this);

        initView();
        initData();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDel(this.getClass().getSimpleName());
    }


    /**
     * 设置布局
     *
     * @return 布局文件的id
     */
    protected abstract int setLayout();

    /**
     * 初始化View 执行FindViewById等操作
     */
    protected abstract void initView();

    /**
     * 初始化数据,例如拉取网络数据,或者一些逻辑代码
     */
    protected abstract void initData();


    /**
     * 简化FindViewById的操作,不需要强转
     *
     * @param id  组件的id
     * @param <T>
     * @return
     */

    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }

    public void back(View view) {
        finish();
    }


}
