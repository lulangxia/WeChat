package com.zjl.mywechat.contacts.group;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.BaseListViewAdapter;
import com.zjl.mywechat.base.BaseListViewHolder;
import com.zjl.mywechat.conversation.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class GrouplistActivity extends BaseAty {

    protected List<EMGroup> grouplist;
    private ListView mListView;
    private Toolbar mToolbar;
    private TextView tv_total;
//    private GroupAdapter groupAdapter;


    @Override
    protected int setLayout() {
        return R.layout.activity_grouplist;
    }

    @Override
    protected void initView() {

        mListView = bindView(R.id.groupListView);
        mToolbar = bindView(R.id.toolbar_group);
        View footerView = LayoutInflater.from(this).inflate(R.layout.fx_item_group_footer, null);
        tv_total = (TextView) footerView.findViewById(R.id.tv_total);

        mListView.addFooterView(footerView, null, false);
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Log.d("FragmentConversationLis", "re");
        }


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

                // mListView.setAdapter(groupAdapter);

                mListView.setAdapter(new BaseListViewAdapter<EMGroup>(GrouplistActivity.this, list, R.layout.test_group_item) {
                    @Override
                    public void convent(BaseListViewHolder viewHolder, EMGroup emGroup) {
                        viewHolder.setText(R.id.te_name, emGroup.getGroupName());
                        // viewHolder.setImage(R.id.iv_avatar, emGroup.getGroupName());
                    }
                });


                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String chatId = ((EMGroup) mListView.getItemAtPosition(position)).getGroupId();
                        Intent intent = new Intent(GrouplistActivity.this, ChatActivity.class);
                        intent.putExtra(EaseConstant.EXTRA_USER_ID, chatId);
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                        startActivityForResult(intent, 0);

                    }
                });


            }
        }.execute();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uiRefresh(String s) {
        if (s.equals("changegroupname")) {
          initData();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
