package com.zjl.mywechat.view;

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
import com.hyphenate.chat.EMClient;
import com.zjl.mywechat.R;
import com.zjl.mywechat.addfriends.RequestBean;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.mvp.presenter.MainPresenter;
import com.zjl.mywechat.mvp.view.MainView;
import com.zjl.mywechat.staticfinal.Constant;
import com.zjl.mywechat.view.adapter.MainAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class MainActivity extends BaseAty implements Toolbar.OnMenuItemClickListener ,MainView{


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TextView mUnreadnum;
    private TextView mUnagreenum;
    private TextView mUnknow;
    public static MainActivity instance = null;
    private UnReadBroadcastReceiver myBroadcastReceiver;

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


        // 广播
//        UnReadBroadcastReceiver receiver = new UnReadBroadcastReceiver();
//        IntentFilter filter1 = new IntentFilter();
//        registerReceiver(receiver, filter1);





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



                EventBus eventBus = new EventBus();




//                eventBus.post();


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



    // 继承来的方法
    @Override
    public void showMessageView() {

        // 功能不需要写了
//         消息列表里边，数字就用集合表示，利用查询的返回值的
//         ArrayList<int> 根据ArrayList长度去设定
//         对不同联系人的匹配问题

        // 参数应该传来一个ArrayLis<int>类型的，如果是和=0，就隐藏

        // 监听到新消息后，用presenter立即插入到数据库

        // 点击后，（视为已读，presenter设置isRead参数为0）

        // presenter调用这个方法，将右上角的图标设定数据显示出来
        // 但是这个数字是要从数据库的查询未读消息数目显示


    }

    @Override
    public void showUnAgreeView() {

    }

    @Override
    public void showUnKnownView() {

    }




    // 广播接收者
    class UnReadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int num = intent.getIntExtra(Constant.UNREAD_MSG_CONVERSA, 0);
            Log.d("UnReadBroadcastReceiver", "num:" + num);
            if (num <= 0) {
                mUnreadnum.setVisibility(View.INVISIBLE);
            } else {
                mUnreadnum.setVisibility(View.VISIBLE);
                mUnreadnum.setText(num + "");
            }



            int unAgreeNum = intent.getIntExtra("num", 0);
            Log.d("UnReadBroadcastReceiver", "unAgreeNum:" + unAgreeNum);

            if (unAgreeNum <= 0) {
                mUnagreenum.setVisibility(View.INVISIBLE);
            } else {
                mUnagreenum.setVisibility(View.VISIBLE);
                mUnagreenum.setText(unAgreeNum + "");
            }



        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}
