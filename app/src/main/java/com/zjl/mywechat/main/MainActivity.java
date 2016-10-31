package com.zjl.mywechat.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.contacts.FragmentTelList;
import com.zjl.mywechat.conversation.FragmentConversationList;
import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.me.FragmentMy;
import com.zjl.mywechat.mvp.presenter.MainPresenter;
import com.zjl.mywechat.mvp.view.MainView;
import com.zjl.mywechat.socalfriend.FragmentFind;
import com.zjl.mywechat.tool.stringvalue.Constant;
import com.zjl.mywechat.ui.adapter.MainAdapter;
import com.zjl.mywechat.widget.AddPopwindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseAty implements Toolbar.OnMenuItemClickListener, MainView {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TextView mUnreadnum;
    private TextView mUnagreenum;
    private TextView mUnknow;
    public static MainActivity instance = null;
    private UnReadBroadcastReceiver myBroadcastReceiver;
    private int mFirstNum = 0;

    private int num = 0;
    private MainPresenter presenter;


    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        instance = this;
        mTabLayout = bindView(R.id.tb_titles_main);
        mViewPager = bindView(R.id.vp_fragments_main);
        mToolbar = bindView(R.id.toolbar_main);

        // 初始化DBTools
        DBTools dbTools = DBTools.getInstance();

        // 控制层
        presenter = new MainPresenter(this);


    }

    @Override
    protected void initData() {

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


        //接收未读消息数广播接收器
        myBroadcastReceiver = new UnReadBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UNREAD_MSG);
        filter.addAction("加好友");
        registerReceiver(myBroadcastReceiver, filter);

        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(final List<EMMessage> messages) {
                //收到消息
                Log.d("MainActivity", "收到消息");
                Log.d("MainActivity", "messages.size():" + messages.size());
                Log.d("MainActivity", messages.get(0).getBody().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MainActivity", "消息增加");
                        mFirstNum = mFirstNum + messages.size();
                        mUnreadnum.setText(mFirstNum + "");
                        mUnreadnum.setVisibility(View.VISIBLE);
                        mToolbar.setTitle("微信" + "(" + mFirstNum + ")");
                        Boolean newmsg = true;
                        EventBus.getDefault().post(newmsg);
                    }
                });
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

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                Log.d("MainActivity", "邀请1");
                Toast.makeText(MainActivity.this, username + "同意了你的请求", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
                Toast.makeText(MainActivity.this, username + "拒绝了你的请求", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onContactInvited(String username, String reason) {

                // 发个广播
                Intent intent = new Intent("加好友");
                int num = 0;
                intent.putExtra("num", ++num);
                intent.putExtra("name", username);
                intent.putExtra("reason", reason);
                sendBroadcast(intent);


                RequestBean bean = new RequestBean();
                bean.setName(username);
                bean.setReason(reason);

                presenter.onInsert(bean);


                // 跳转，传值，MainActivity显示角标，新的朋友右侧显示1+
                // 点进去是一个listView，存储主动请求和被动请求（数据库），右边填写同意or不同意

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                Toast.makeText(MainActivity.this, "你已被" + username + "删除", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                Log.d("MainActivity", "你同意了" + username + "的请求");
            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbat, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void showMessageView() {

    }

    @Override
    public void showUnAgreeView() {

    }

    @Override
    public void showUnKnownView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    // 广播接收者
    class UnReadBroadcastReceiver extends BroadcastReceiver {
        private boolean flag = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("UnReadBroadcastReceiver", "收到广播");
            flag = intent.getBooleanExtra(Constant.UNREAD_MSG_CONVERSA_SINGLE, false);
            if (flag) {
                mFirstNum = intent.getIntExtra(Constant.UNREAD_MSG_CONVERSA, 0);
                Log.d("UnReadBroadcastReceiver", "num:" + mFirstNum);
                if (mFirstNum <= 0) {
                    mUnreadnum.setVisibility(View.INVISIBLE);
                    mToolbar.setTitle("微信");
                } else {
                    mUnreadnum.setVisibility(View.VISIBLE);
                    mUnreadnum.setText(mFirstNum + "");
                    mToolbar.setTitle("微信" + "(" + mFirstNum + ")");
                }
                flag = false;
                int num = intent.getIntExtra(Constant.UNREAD_MSG_CONVERSA, 0);
                Log.d("UnReadBroadcastReceiver", "num:" + num);
                if (num <= 0) {
                    mUnreadnum.setVisibility(View.INVISIBLE);
                } else {
                    mUnreadnum.setVisibility(View.VISIBLE);
                    mUnreadnum.setText(num + "");
                }
//                int unAgreeNum = intent.getIntExtra("num", 0);
//                Log.d("UnReadBroadcastReceiver", "unAgreeNum:" + unAgreeNum);
//
//                if (unAgreeNum <= 0) {
//                    mUnagreenum.setVisibility(View.INVISIBLE);
//                } else {
//                    mUnagreenum.setVisibility(View.VISIBLE);
//                    mUnagreenum.setText(unAgreeNum + "");
//                }
            }
        }
    }

}