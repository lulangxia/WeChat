package com.zjl.mywechat.service;


import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
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

    private EMMessageListener msgListener;


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
            msgListener =  new EMMessageListener() {

                @Override
                public void onMessageReceived(List<EMMessage> messages) {
                    //收到消息
                    Log.d("MyBinder", "收到消息");
                    Log.d("MyBinder", "messages.size():" + messages.size());
                    Log.d("MyBinder", messages.get(0).getBody().toString());

                    // 控制刷新的参数
                    Boolean newmsg = true;
                    EventBus.getDefault().post(newmsg);




//                    broadIntent.putExtra();
                    ArrayList<EMMessage> arrayList = (ArrayList<EMMessage>) messages;
                    broadIntent.putParcelableArrayListExtra("EMMessage", arrayList);
                    Log.d("MyBinder", "aaaaaa");
                    sendBroadcast(broadIntent);


                    // EventBus.getDefault().post(messages);


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


        public void groupReceiveListener() {

            //群组事件监听
            EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
                @Override
                public void onUserRemoved(String groupId, String groupName) {
                    //当前用户被管理员移除出群组
                    Log.d("MainActivity", "被移除");
                }

                @Override
                public void onGroupDestroyed(String s, String s1) {
                    //群组被创建者解散
                    Log.d("MainActivity", "群解散");
                }

                @Override
                public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {
                    Log.d("MainActivity", "自动接受");
                }

                @Override
                public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                    //收到加入群组的邀请
                    Log.d("MainActivity", "收到邀请");
                }

                @Override
                public void onInvitationDeclined(String groupId, String invitee, String reason) {
                    //群组邀请被拒绝
                    Log.d("MainActivity", "被拒绝");
                }


                @Override
                public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
                    //收到加群申请
                    Log.d("MainActivity", "收到申请");
                }

                @Override
                public void onApplicationAccept(String groupId, String groupName, String accepter) {
                    //加群申请被同意
                    Log.d("MainActivity", "被同意");
                }

                @Override
                public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
                    // 加群申请被拒绝
                }

                @Override
                public void onInvitationAccepted(String s, String s1, String s2) {
                    //群组邀请被接受
                    Log.d("MainActivity", "邀请被接受");
                }
            });



        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
