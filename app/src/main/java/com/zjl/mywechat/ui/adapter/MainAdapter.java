package com.zjl.mywechat.ui.adapter;

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


    public void setFragments(ArrayList<Fragment> fragments) {
        mFragments = fragments;
    }

    public MainAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


}
