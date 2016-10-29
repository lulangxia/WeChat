package com.zjl.mywechat.ui;

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

import com.zjl.mywechat.R;
import com.zjl.mywechat.app.MyApp;
import com.zjl.mywechat.base.BaseAty;

import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.staticfinal.Constant;
import com.zjl.mywechat.ui.adapter.MainAdapter;



import java.util.ArrayList;


public class MainActivity extends BaseAty implements Toolbar.OnMenuItemClickListener {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TextView mUnreadnum;
    private TextView mUnagreenum;
    private TextView mUnknow;
    public static MainActivity instance = null;
    private UnReadBroadcastReceiver myBroadcastReceiver;

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
        DBTools dbTools = DBTools.getInstance(MyApp.getmContext());


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


        //接收未读消息数广播接收器
        myBroadcastReceiver = new UnReadBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UNREAD_MSG);
        registerReceiver(myBroadcastReceiver, filter);

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

    class UnReadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int num = intent.getIntExtra(Constant.UNREAD_MSG_CONVERSA, 0);
            Log.d("UnReadBroadcastReceiver", "num:" + num);
            if (num <= 0) {
                mUnreadnum.setVisibility(View.INVISIBLE);
                mToolbar.setTitle("微信");
            } else {
                mUnreadnum.setVisibility(View.VISIBLE);
                mUnreadnum.setText(num + "");
                mToolbar.setTitle("微信"+"("+num+")");
            }
        }

    }


}
