package com.zjl.mywechat.group;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;

import java.util.List;

public class AddGroupActivity extends BaseAty {


    private EditText mName;
    private EditText mDesc;
    private Button mCreat;
    private String mGroupid;

    @Override
    protected int setLayout() {
        return R.layout.activity_add_group2;
    }

    @Override
    protected void initView() {
        mName = bindView(R.id.edit_group_name);
        mDesc = bindView(R.id.edit_group_introduction);
        mCreat = bindView(R.id.btn_creat);
    }

    @Override
    protected void initData() {
        mCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().length() == 0) {
                    Toast.makeText(AddGroupActivity.this, "群组名不能为空", Toast.LENGTH_SHORT).show();

                } else {
                    creatGroupNew(mName.getText().toString(), mDesc.getText().toString());
                }
            }

        });
    }

    private void creatGroupNew(final String name, final String desc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("AddGroupMemberActivity", "group");
                    EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                    option.maxUsers = 200;
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    String[] strings = new String[1];
                    strings[0] = EMClient.getInstance().getCurrentUser();
                    EMClient.getInstance().groupManager().createGroup(name, desc, strings, null, option);
                    final List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("AddGroupActivity", "grouplist.size():" + grouplist.size());

                            for (int i = 0; i < grouplist.size(); i++) {
                                if (name.equals(grouplist.get(i).getGroupName())) {
                                    mGroupid = grouplist.get(i).getGroupId();
                                }
                            }
                            Log.d("AddGroupActivity", mGroupid);
                            Toast.makeText(AddGroupActivity.this, "建群成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddGroupActivity.this, AddGroupMemberActivity.class).putExtra("newgroupid", mGroupid));
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }
}