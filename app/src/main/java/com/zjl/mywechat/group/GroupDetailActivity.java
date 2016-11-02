package com.zjl.mywechat.group;

import android.content.Intent;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.BaseListViewAdapter;
import com.zjl.mywechat.base.BaseListViewHolder;
import com.zjl.mywechat.widget.ExpandGridView;

import java.util.List;

public class GroupDetailActivity extends BaseAty {


    private ExpandGridView mExpandGridView;
    private EaseSwitchButton mEaseSwitchButton;

    @Override
    protected int setLayout() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void initView() {

        mExpandGridView = bindView(R.id.gridview);
        mEaseSwitchButton = bindView(R.id.switch_btn);
    }

    @Override
    protected void initData() {
        Intent intent1 = getIntent();
        String groupId = intent1.getStringExtra("groupId");
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
        List<String> name = group.getMembers();


        mExpandGridView.setAdapter(new BaseListViewAdapter<String>(this, name, R.layout.em_grid) {
            @Override
            public void convent(BaseListViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.tv_name, s);
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
        if(mEaseSwitchButton.isSwitchOpen()){
            mEaseSwitchButton.closeSwitch();
        } else {
            mEaseSwitchButton.openSwitch();
        }
    }
}
