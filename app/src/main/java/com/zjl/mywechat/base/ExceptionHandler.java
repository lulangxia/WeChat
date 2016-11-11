package com.zjl.mywechat.base;


import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


// 生成日志文件
public class ExceptionHandler implements Thread.UncaughtExceptionHandler{

    // logt
    private static final String TAG = "ExceptionHandler";

    private BaseAty mActivity;

    public ExceptionHandler(BaseAty mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // logm
        Log.d(TAG, "uncaughtException() called with: t = [" + t + "], e = [" + e + "]");

        // 获取堆栈
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "WeChatError.log");


        OutputStream os = null;
        try {
            // 如果文件不存在那就创建一个
            if (!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file, true);
            os.write((e.getMessage() + "\n").getBytes());
            for (StackTraceElement element : stackTraceElements) {
                byte[] bytes = (element.toString() + "\n").getBytes();
                os.write(bytes);
            }
            os.write("----------------\n".getBytes());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }


        Toast.makeText(mActivity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

        Toast.makeText(mActivity, e.getMessage().toString(), Toast.LENGTH_SHORT).show();


    }



}
