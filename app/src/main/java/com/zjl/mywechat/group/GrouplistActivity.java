package com.zjl.mywechat.group;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;

import java.util.List;


public class GrouplistActivity extends BaseAty {

    protected List<EMGroup> grouplist;
    private ListView mListView;
    private Toolbar mToolbar;
    private TextView tv_total;
    private GroupAdapter groupAdapter;


    @Override
    protected int setLayout() {
        return R.layout.activity_grouplist;
    }

    @Override
    protected void initView() {

        mListView = bindView(R.id.groupListView);
        mToolbar = bindView(R.id.toolbar_group);

    }

    @Override
    protected void initData() {
        mToolbar.setTitle("群聊");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.fx_top_bar_back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupAdapter = new GroupAdapter(this);
        View footerView = LayoutInflater.from(this).inflate(R.layout.fx_item_group_footer, null);
        tv_total = (TextView) footerView.findViewById(R.id.tv_total);

        mListView.addFooterView(footerView);

        new AsyncTask<Void, Void, List>() {
            @Override
            protected List doInBackground(Void... params) {
                try {
                    grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
//                    Log.d("GrouplistActivity111", grouplist.get(0).getGroupName());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                return grouplist;
            }

            @Override
            protected void onPostExecute(List list) {
                super.onPostExecute(list);

                tv_total.setText(String.valueOf(list.size()) + "个群聊");
                groupAdapter.setGroupList(list);
                mListView.setAdapter(groupAdapter);

            }
        }.execute();


        //

//        View footerView = LayoutInflater.from(this).inflate(
//                R.layout.fx_item_group_footer, null);
//        tv_total = (TextView) footerView.findViewById(R.id.tv_total);
//        tv_total.setText(String.valueOf(grouplist.size()) + "个群聊");
//        mListView.addFooterView(footerView);
//
//        groupAdapter = new GroupAdapter(this, grouplist);
//
//        mListView.setAdapter(groupAdapter);


    }

}
