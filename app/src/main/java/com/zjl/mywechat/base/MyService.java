package com.zjl.mywechat.base;


import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.database.DBTools;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyService extends android.app.Service {


    private MyBinder myBinder;
    private Intent mIntent;
    private Intent broadIntent;


    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();

        mIntent = new Intent("服务");

        broadIntent = new Intent("未读消息数目变化");

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }










    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }



    public class MyBinder extends Binder{



        public void requestReceiveListener() {

            Log.d("MyBinder", "?");

            // 有关好友请求的监听
            EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

                @Override
                public void onContactAgreed(String username) {
                    //好友请求被同意
                    Log.d("TAGGG_MainActivity", "邀请1");

                    RequestBean requestBean = new RequestBean();
                    requestBean.setIsPositive(2);
                    requestBean.setIsAgree(1);
                    requestBean.setName(username);
                    DBTools.getInstance().getmLiteOrm().update(requestBean);

                }

                @Override
                public void onContactRefused(String username) {
                    //好友请求被拒绝
                    Log.d("TAGGG_MainActivity", "邀请2");
                    RequestBean requestBean = new RequestBean();
                    requestBean.setIsPositive(3);
                    requestBean.setIsAgree(2);
                    requestBean.setName(username);
                    DBTools.getInstance().getmLiteOrm().update(requestBean);
                }


                @Override
                public void onContactInvited(String username, String reason) {
                    Log.d("TAGGG_MainActivity", "邀请3");
                    Log.d("TAGGG_MainActivity", username);








                    // EventBus
                    RequestBean bean = new RequestBean();
                    bean.setName(username);
                    bean.setReason(reason);


                    broadIntent.putExtra("RequestBean", bean);
                    sendBroadcast(broadIntent);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("?", bean);




//                    EventBus.getDefault().post(bean);


                    // 跳转，传值，MainActivity显示角标，新的朋友右侧显示1+
                    // 点进去是一个listView，存储主动请求和被动请求（数据库），右边填写同意or不同意
                }


                @Override
                public void onContactDeleted(String username) {
                    //被删除时回调此方法
                    Log.d("TAGGG_MainActivity", "邀请4你已被删除");

                    RequestBean bean = new RequestBean();
                    bean.setName(username);

                    DBTools.getInstance().getmLiteOrm().delete(bean);
                    Boolean refresh = true;
                    EventBus.getDefault().post(refresh);
                }


                @Override
                public void onContactAdded(String username) {
                    //增加了联系人时回调此方法
                    Log.d("TAGGG_MainActivity", "邀请5");

                    Boolean refresh = true;
                    EventBus.getDefault().post(refresh);

                }

            });

        }

        public void messageReceiveListener() {

            Log.d("MyBinder", "??");

            // 有关消息的监听
            EMMessageListener msgListener = new EMMessageListener() {

                @Override
                public void onMessageReceived(List<EMMessage> messages) {
                    //收到消息
                    Log.d("MainActivity", "收到消息");
                    Log.d("MainActivity", "messages.size():" + messages.size());
                    Log.d("MainActivity", messages.get(0).getBody().toString());

                    // 控制刷新的参数
                    Boolean newmsg = true;
                    EventBus.getDefault().post(newmsg);




//                    broadIntent.putExtra();
                    ArrayList<EMMessage> arrayList = (ArrayList<EMMessage>) messages;
                    broadIntent.putParcelableArrayListExtra("EMMessage", arrayList);

                    sendBroadcast(broadIntent);


//                    EventBus.getDefault().post(messages);


                }

                @Override
                public void onCmdMessageReceived(List<EMMessage> messages) {
                    //收到透传消息
                    Log.d("MainActivity", "透传消息");
                }

                @Override
                public void onMessageReadAckReceived(List<EMMessage> list) {

                    //收到已读回执
                    Log.d("MainActivity", "收到已读回执");
                }


                @Override
                public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                    //收到已送达回执
                    Log.d("MainActivity", "收到已送达回执");

                }

                @Override
                public void onMessageChanged(EMMessage message, Object change) {
                    Log.d("MainActivity", change.toString());
                    //消息状态变动
                }
            };
            EMClient.getInstance().chatManager().addMessageListener(msgListener);






        }



    }



}
