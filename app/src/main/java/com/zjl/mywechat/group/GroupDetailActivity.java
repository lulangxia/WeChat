package com.zjl.mywechat.group;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.BaseListViewAdapter;
import com.zjl.mywechat.base.BaseListViewHolder;
import com.zjl.mywechat.widget.ExpandGridView;

import java.util.List;

public class GroupDetailActivity extends BaseAty {


    private ExpandGridView mExpandGridView;
    private EaseSwitchButton mEaseSwitchButton;
    private ImageView mBack;
    private TextView mMembersnum;
    private List<String> mName;

    @Override
    protected int setLayout() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void initView() {
        mBack = bindView(R.id.iv_back);
        mExpandGridView = bindView(R.id.gridview);
        mEaseSwitchButton = bindView(R.id.switch_btn);
        mMembersnum = bindView(R.id.tv_m_total);
    }

    @Override
    protected void initData() {
        Intent intent1 = getIntent();
        final String groupId = intent1.getStringExtra("groupId");
        Log.d("GroupDetailActivity", groupId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                EMGroup group = null;
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                    mName = group.getMembers();
                    Log.d("GroupDetailActivity", "name.size():" + mName.size());
                    Log.d("GroupDetailActivity", "group.getMemberCount():" + group.getMemberCount());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        new AsyncTask<String, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {
                try {
                    EMGroup  group = EMClient.getInstance().groupManager().getGroupFromServer(params[0]);
                    mName = group.getMembers();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                return mName;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                mExpandGridView.setAdapter(new BaseListViewAdapter<String>(GroupDetailActivity.this, strings, R.layout.em_grid) {
                    @Override
                    public void convent(BaseListViewHolder viewHolder, String s) {
                        viewHolder.setText(R.id.tv_name, s);
                    }
                });
                mMembersnum.setText("(" + strings.size() + ")");
            }
        }.execute(groupId);


//        mName = group.getMembers();
//        Log.d("GroupDetailActivity", "name.size():" + mName.size());
//        Log.d("GroupDetailActivity", "group.getMemberCount():" + group.getMemberCount());



        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mEaseSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBlockGroup();
            }
        });
    }

    private void toggleBlockGroup() {
        if (mEaseSwitchButton.isSwitchOpen()) {
            mEaseSwitchButton.closeSwitch();
        } else {
            mEaseSwitchButton.openSwitch();
        }
    }
}
