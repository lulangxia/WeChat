package com.zjl.mywechat.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.MyService;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.contacts.FragmentTelList;
import com.zjl.mywechat.conversation.ChatActivity;
import com.zjl.mywechat.conversation.FragmentConversationList;
import com.zjl.mywechat.login.view.LoginActivity;
import com.zjl.mywechat.me.FragmentMy;
import com.zjl.mywechat.mvp.presenter.MainPresenter;
import com.zjl.mywechat.mvp.view.MainView;
import com.zjl.mywechat.socalfriend.view.FragmentFind;
import com.zjl.mywechat.ui.adapter.MainAdapter;
import com.zjl.mywechat.widget.AddPopwindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseAty implements Toolbar.OnMenuItemClickListener, MainView {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TextView mUnreadnum;
    private TextView mUnagreenum;
    private TextView mUnknow;
//    public static MainActivity instance;


    private int unAgreeNum = 0;


    private SharedPreferences sp;
    private SharedPreferences.Editor spET;


    private int mFirstNum = 0;
    private MainPresenter presenter;
    private ZeroReceiver receiver;
    private SharedPreferences.Editor setEditor;
    private MyService.MyBinder myBinder;
    private Intent intentService;
    private MyConnection myConnection;


    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Log.d("TAGGG_MainActivity", "re");
        }

//        instance = this;// 退出登录时，finish掉MainActivity
        mTabLayout = bindView(R.id.tb_titles_main);
        mViewPager = bindView(R.id.vp_fragments_main);
        mToolbar = bindView(R.id.toolbar_main);


        // 初始化控制层
        presenter = new MainPresenter(this);


        // 初始化广播
        receiver = new ZeroReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("未读消息数目变化");
        registerReceiver(receiver, filter);


    }

    @Override
    protected void initData() {
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());


        sp = getSharedPreferences("shared", MODE_PRIVATE);
        spET = sp.edit();

        mFirstNum = sp.getInt("unreadnum", 0);


        mToolbar.setTitle("微信");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentConversationList());
        fragments.add(new FragmentTelList());
        fragments.add(new FragmentFind());
        fragments.add(new FragmentMy());


        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), this);
        mainAdapter.setFragments(fragments);
        mViewPager.setAdapter(mainAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(0xff999999, 0xff45c01a);
        mTabLayout.getTabAt(0).setCustomView(R.layout.weixin_tab01);
        //mTabLayout.getTabAt(0).setIcon(R.drawable.tab_weixin);
        mTabLayout.getTabAt(1).setCustomView(R.layout.weixin_tab02);
        // mTabLayout.getTabAt(1).setIcon(R.drawable.tab_contact_list);
        mTabLayout.getTabAt(2).setCustomView(R.layout.weixin_tab03);
        // mTabLayout.getTabAt(2).setIcon(R.drawable.tab_find);
        //mTabLayout.getTabAt(3).setIcon(R.drawable.tab_profile);
        mTabLayout.getTabAt(3).setCustomView(R.layout.weixin_tab04);


        mUnreadnum = (TextView) mTabLayout.findViewById(R.id.unread_msg_number);
        mUnagreenum = (TextView) mTabLayout.findViewById(R.id.unagree_msg_number);
        mUnknow = (TextView) mTabLayout.findViewById(R.id.unknow_msg);
        mUnreadnum.setVisibility(View.INVISIBLE);
        mUnagreenum.setVisibility(View.INVISIBLE);

        Log.d("MainActivity", "mFirstNum:" + mFirstNum);
        if (mFirstNum != 0) {
            mUnreadnum.setVisibility(View.VISIBLE);
            mToolbar.setTitle("微信" + "(" + mFirstNum + ")");
            mUnreadnum.setText(mFirstNum + "");
        }


        SharedPreferences sharedSetting = this.getSharedPreferences("unAgreeNum", MODE_PRIVATE);
        setEditor = sharedSetting.edit();

        unAgreeNum = 0;
        // 持久化技术存放
        unAgreeNum = sharedSetting.getInt("unAgreeNum", 0);
        if (unAgreeNum <= 0) {
            mUnagreenum.setVisibility(View.INVISIBLE);
        } else {
            mUnagreenum.setVisibility(View.VISIBLE);
            mUnagreenum.setText(unAgreeNum + "");
        }


        // 服务
        Log.d("TAGGG_MainActivity", "start service");
        Intent intentService = new Intent(this, MyService.class);
        startService(intentService);
        myConnection = new MyConnection();
        bindService(intentService, myConnection, BIND_AUTO_CREATE);


        //        // 有关消息的监听
        //        EMMessageListener msgListener = new EMMessageListener() {
        //
        //            @Override
        //            public void onMessageReceived(final List<EMMessage> messages) {
        //                //收到消息
        //                Log.d("MainActivity", "收到消息");
        //                Log.d("MainActivity", "messages.size():" + messages.size());
        //                Log.d("MainActivity", messages.get(0).getBody().toString());
        //
        //
        //                runOnUiThread(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        Log.d("MainActivity", "消息增加");
        //                        if (!ChatActivity.instance.flag) {
        //                            mFirstNum = mFirstNum + messages.size();
        //                            mUnreadnum.setText(mFirstNum + "");// 底部数字的改变
        //                            mUnreadnum.setVisibility(View.VISIBLE);
        //                            mToolbar.setTitle("微信" + "(" + mFirstNum + ")");// ToolBar的数字个数改变
        //                            Boolean newmsg = true;
        //                            EventBus.getDefault().post(newmsg);
        //                            spET.putInt("unreadnum", mFirstNum);// 持久化保存
        //                            spET.commit();
        //                            Log.d("MainActivity", "mFirstNum:" + mFirstNum);
        //                        }
        //                    }
        //                });
        //
        //
        //            }
        //
        //            @Override
        //            public void onCmdMessageReceived(List<EMMessage> messages) {
        //                //收到透传消息
        //                Log.d("MainActivity", "透传消息");
        //            }
        //
        //            @Override
        //            public void onMessageReadAckReceived(List<EMMessage> list) {
        //
        //                //收到已读回执
        //                Log.d("MainActivity", "收到已读回执");
        //            }
        //
        //
        //            @Override
        //            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        //                //收到已送达回执
        //                Log.d("MainActivity", "收到已送达回执");
        //
        //            }
        //
        //            @Override
        //            public void onMessageChanged(EMMessage message, Object change) {
        //                Log.d("MainActivity", change.toString());
        //                //消息状态变动
        //            }
        //        };
        //        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        //
        //        // 有关好友请求的监听
        //        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
        //
        //            @Override
        //            public void onContactAgreed(String username) {
        //                //好友请求被同意
        //                Log.d("MainActivity", "邀请1");
        //                //                Boolean refresh = true;
        //                //                EventBus.getDefault().post(refresh);
        //
        //
        //                RequestBean requestBean = new RequestBean();
        //                requestBean.setIsPositive(2);
        //                requestBean.setIsAgree(1);
        //                requestBean.setName(username);
        //                DBTools.getInstance().getmLiteOrm().update(requestBean);
        //
        //            }
        //
        //            @Override
        //            public void onContactRefused(String username) {
        //                //好友请求被拒绝
        //                Log.d("MainActivity", "邀请2");
        //                RequestBean requestBean = new RequestBean();
        //                requestBean.setIsPositive(3);
        //                requestBean.setIsAgree(2);
        //                requestBean.setName(username);
        //                DBTools.getInstance().getmLiteOrm().update(requestBean);
        //            }
        //
        //
        //            @Override
        //            public void onContactInvited(String username, String reason) {
        //                Log.d("MainActivity", "邀请3");
        //
        //                // EventBus
        //                RequestBean bean = new RequestBean();
        //                bean.setName(username);
        //                bean.setReason(reason);
        //                EventBus.getDefault().post(bean);
        //
        //                presenter.hasData(bean);
        //
        //
        //                // 跳转，传值，MainActivity显示角标，新的朋友右侧显示1+
        //                // 点进去是一个listView，存储主动请求和被动请求（数据库），右边填写同意or不同意
        //            }
        //
        //
        //            @Override
        //            public void onContactDeleted(String username) {
        //                //被删除时回调此方法
        //                Log.d("MainActivity", "邀请4你已被删除");
        //
        //                RequestBean bean = new RequestBean();
        //                bean.setName(username);
        //
        //                DBTools.getInstance().getmLiteOrm().delete(bean);
        //                Boolean refresh = true;
        //                EventBus.getDefault().post(refresh);
        //            }
        //
        //
        //            @Override
        //            public void onContactAdded(String username) {
        //                //增加了联系人时回调此方法
        //                Log.d("MainActivity", "邀请5");
        //
        //                Boolean refresh = true;
        //                EventBus.getDefault().post(refresh);
        //
        //            }
        //
        //        });
        //聊天消息监听



        Boolean refresh = true;
        EventBus.getDefault().post(refresh);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // ToolsBar点击
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                Toast.makeText(MainActivity.this, "静待后续版本实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add:
                AddPopwindow addPopwindow = new AddPopwindow(this);
                addPopwindow.showPopupWindow(mToolbar);
                // Toast.makeText(MainActivity.this, "添加", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    // P层控制view层调用的方法1
    @Override
    public void showMessageView() {

        // 显示下方的消息个数
        ++unAgreeNum;


        setEditor.putInt("unAgreeNum", unAgreeNum);
        Log.d("FragmentTelList66666", "unAgreeNum:" + unAgreeNum);
        setEditor.commit();


        Intent intent = new Intent("加好友");
        intent.putExtra("num", unAgreeNum);
        sendBroadcast(intent);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (unAgreeNum <= 0) {
                    mUnagreenum.setVisibility(View.INVISIBLE);
                } else {
                    mUnagreenum.setVisibility(View.VISIBLE);
                    mUnagreenum.setText(unAgreeNum + "");
                }
            }
        });
    }

    // P层控制view层调用的方法2
    @Override
    public void showUnAgreeView(ArrayList<RequestBean> arraylist) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(myConnection);
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void readNum(Integer i) {
        Log.d("MainActivity", "？？？");
        mFirstNum = mFirstNum - i;
        if (mFirstNum <= 0) {
            mUnreadnum.setVisibility(View.INVISIBLE);
            mToolbar.setTitle("微信");
            mFirstNum = 0;
            spET.putInt("unreadnum", mFirstNum);
            spET.commit();
        } else {
            mUnreadnum.setVisibility(View.VISIBLE);
            mUnreadnum.setText((mFirstNum) + "");
            mToolbar.setTitle("微信" + "(" + (mFirstNum) + ")");
            spET.putInt("unreadnum", mFirstNum);
            spET.commit();

        }
        Log.d("MainActivity", "mFirstNum:" + mFirstNum);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    private void getRequestBean(RequestBean bean) {
        Log.d("TAGGG_MainActivity", "?.");
        Log.d("TAGGG_MainActivity", bean.getName());
        presenter.hasData(bean);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    private void getMessageBeen(final List<EMMessage> messages) {
        Log.d("MainActivity", "消息增加");
        if (!ChatActivity.instance.flag) {
            mFirstNum = mFirstNum + messages.size();
            mUnreadnum.setText(mFirstNum + "");// 底部数字的改变
            mUnreadnum.setVisibility(View.VISIBLE);
            mToolbar.setTitle("微信" + "(" + mFirstNum + ")");// ToolBar的数字个数改变
            // Boolean newmsg = true;
            // EventBus.getDefault().post(newmsg);
            spET.putInt("unreadnum", mFirstNum);// 持久化保存
            spET.commit();
            Log.d("MainActivity", "mFirstNum:" + mFirstNum);
        }
    }


    // 点击邀请信息后，下标清零
    private class ZeroReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 未阅读的好友添加数

            if (intent.getIntExtra("zeroNum", 0) < 0) {
                // FragmentTelList发送的值是 -1
                unAgreeNum = 0;
                mUnagreenum.setVisibility(View.INVISIBLE);
                // 数据持久化置零
                setEditor.putInt("unAgreeNum", 0);
                setEditor.commit();
            }


            RequestBean requestBean = intent.getParcelableExtra("RequestBean");
            if (requestBean != null) {
                Log.d("TAGGG_MainActivity", "?.");
                Log.d("TAGGG_MainActivity", requestBean.getName());
                presenter.hasData(requestBean);
            }


            ArrayList<EMMessage> messages = intent.getParcelableArrayListExtra("EMMessage");
            if (messages != null) {
                messageChange(messages);
            }

        }
    }

    private void messageChange(ArrayList<EMMessage> messages) {
        Log.d("MainActivity", "消息增加");
        if (!ChatActivity.instance.flag) {
            mFirstNum = mFirstNum + messages.size();
            mUnreadnum.setText(mFirstNum + "");// 底部数字的改变
            mUnreadnum.setVisibility(View.VISIBLE);
            mToolbar.setTitle("微信" + "(" + mFirstNum + ")");// ToolBar的数字个数改变
            // Boolean newmsg = true;
            // EventBus.getDefault().post(newmsg);
            spET.putInt("unreadnum", mFirstNum);// 持久化保存
            spET.commit();
            Log.d("MainActivity", "mFirstNum:" + mFirstNum);
        }
    }


    private class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private long clickTime = 0; //记录第一次点击的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出应用",
                    Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }




    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        Toast.makeText(MainActivity.this, "账号已被删除", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Toast.makeText(MainActivity.this, "同一账号在其他设备登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            });
        }
    }

}