package com.zjl.mywechat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by dllo on 16/10/20.
 */
public class MainAdapter extends FragmentPagerAdapter {

    Context mContext;
    private ArrayList<Fragment> mFragments;
    private final ArrayList<String> mTitles;


    public void setFragments(ArrayList<Fragment> fragments) {
        mFragments = fragments;
    }

    public MainAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitles = new ArrayList<>();
        mContext = context;
        mTitles.add("微信");
        mTitles.add("通讯录");
        mTitles.add("发现");
        mTitles.add("我");
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
