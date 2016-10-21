package com.zjl.mywechat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zjl.mywechat.my.FragmentMy;

import java.util.ArrayList;


public class MainActivity extends BaseAty {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTabLayout = bindView(R.id.titles_main);
        mViewPager = bindView(R.id.fragments_main);

    }

    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new FragmentMy());

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(),this);
        mainAdapter.setFragments(fragments);
        mViewPager.setAdapter(mainAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
