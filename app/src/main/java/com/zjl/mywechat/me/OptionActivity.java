package com.zjl.mywechat.me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zjl.mywechat.R;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.login.view.LoginActivity;
import com.zjl.mywechat.main.MainActivity;
import com.zjl.mywechat.service.MyService;
import com.zjl.mywechat.tools.DataCleanManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import static com.zjl.mywechat.tools.CreateAndDeleteMap.onDel;

/**
 * Created by dllo on 16/10/26.
 */

public class OptionActivity extends BaseAty implements View.OnClickListener {

    private Button mButton;
    private TextView tvClearRecord;
    private RelativeLayout tvClearMemory;
    private TextView tvClearNum;
    private TextView tvCurrentMemory;
    private DataCleanManager manager1;

    private String memoryPath = "";


    @Override
    protected int setLayout() {
        return R.layout.testoption;
    }

    @Override
    protected void initView() {
        mButton = bindView(R.id.outlogin);
        tvClearNum = bindView(R.id.tv_clearNum);
        tvClearRecord = bindView(R.id.tv_clearRecord);
        tvClearMemory = bindView(R.id.tv_clearMemory);
        tvCurrentMemory = bindView(R.id.tv_currentMemory);

        // 清空缓存
        manager1 = new DataCleanManager();

    }

    @Override
    protected void initData() {

        mButton.setOnClickListener(this);
        tvClearNum.setOnClickListener(this);
        tvClearRecord.setOnClickListener(this);
        tvClearMemory.setOnClickListener(this);


        // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        // Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
        File file = new File("/sdcard/Android/data/com.zjl.mywechat/");
        // File file = MyApp.getmContext().getExternalFilesDir(null);// 得到应用程序的文件目录的根目录
        try {
            String n = manager1.getCacheSize(file);
            tvCurrentMemory.setText("当前缓存 : " + n);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.outlogin:
                // 退出登录
                logout();
                break;
            case R.id.tv_clearNum:
                clearNum();

                // finish();

                break;
            case R.id.tv_clearRecord:
                // 清除聊天记录
                clearRecord();
                // finish();

                break;
            case R.id.tv_clearMemory:
                // 清除缓存
                clearMemory();

                // finish();

                break;
        }

    }


    private void logout() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        final ProgressDialog pd = new ProgressDialog(OptionActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                        // MyApp.getInstance().setCurrentUserName("");
                        // 将数据库对象置空，这样下次登录新号会新建一个库
                        DBTools.getInstance().setDbToolsNull();
                        startActivity(new Intent(OptionActivity.this, LoginActivity.class));
                        Log.d("BaseAty*****", MainActivity.class.getSimpleName());


                        // 安卓内存虚拟机会释放掉那个页面，Java的activity对象在下次内存回收时会被清楚
                        Activity activity = onDel(MainActivity.class.getSimpleName());

                        if (activity != null) {
                            activity.finish();
                        }
                        finish();
                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(OptionActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    private void clearNum() {


        int sum = 0;
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Iterator iter = conversations.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Log.d("OptionActivity", key);

            // 获取单人的未读消息数
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(key);
            if (conversation != null) {
                sum += conversation.getUnreadMsgCount();

                Log.d("OptionActivity", "sum:" + sum);
            }
            Log.d("OptionActivity", "sum:" + sum);
        }


        // 先获取未读消息数目，再清零。。。。所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
        Toast.makeText(this, "未读消息数已清零", Toast.LENGTH_SHORT).show();


        Integer i = new Integer(sum);
        EventBus.getDefault().post(99999);

    }


    private void clearRecord() {

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Iterator iter = conversations.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Log.d("OptionActivity", key);
            EMClient.getInstance().chatManager().deleteConversation(key, true);
        }


        Integer i = new Integer(9999);
        EventBus.getDefault().post(i);

        Toast.makeText(this, "聊天记录已清空", Toast.LENGTH_SHORT).show();


        //删除当前会话的某条聊天记录
//        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
//        conversation.removeMessage(deleteMsg.msgId);
    }


    private void clearMemory() {

        // 数组
        String[] path = {"/sdcard/Android/data/com.zjl.mywechat/1132161022178628#zjl/core_log/"};

        manager1.cleanApplicationData(MyApp.getmContext(), path);
        tvCurrentMemory.setText("当前缓存 : 0.0 KB");

        Toast.makeText(this, "缓存已清空", Toast.LENGTH_SHORT).show();
    }


    public void back(View view) {
        finish();
    }


}
