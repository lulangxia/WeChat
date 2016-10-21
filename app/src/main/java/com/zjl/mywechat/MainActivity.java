package com.zjl.mywechat;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends BaseAty implements Toolbar.OnMenuItemClickListener {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTabLayout = bindView(R.id.tb_titles_main);
        mViewPager = bindView(R.id.vp_fragments_main);
        mToolbar = bindView(R.id.toolbar_main);

    }

    @Override
    protected void initData() {
        mToolbar.setTitle("微信");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(this);


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), this);
        mainAdapter.setFragments(fragments);
        mViewPager.setAdapter(mainAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbat,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add:
                AddPopwindow addPopwindow = new AddPopwindow(this);
                addPopwindow.showPopupWindow(mToolbar);
                Toast.makeText(MainActivity.this, "添加", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}
