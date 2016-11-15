package com.zjl.mywechat.contacts.group;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.bean.GroupContactBean;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.contacts.adapter.AddGroupAdapter;
import com.zjl.mywechat.conversation.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroupMemberActivity extends BaseAty {


    private ImageView mBack;

    private ImageView iv_search;
    private TextView tv_checked;
    private ListView listView;
    //是否新建群
    protected boolean isCreatingNewGroup = true;
    // private PickContactAdapter contactAdapter;
    private List<String> exitingMembers = new ArrayList<String>();
    // 可滑动的显示选中用户的View
    private LinearLayout menuLinerLayout;
    // 选中用户总数,右上角显示
    private int userId = 0;

    private String groupname;
    // 添加的列表
    private List<String> addList = new ArrayList<String>();
    private List<EaseUser> friendList;
    private EMGroup group;
    private EditText mEditText;
    private HashMap<String, EaseUser> mMap;
    private List<RequestBean> mContacts;
    private List<GroupContactBean> mMembers;
    private ListView mListView;
    private AddGroupAdapter mListAdapter;
    private String mGroupId;

    private boolean newgroup;

    @Override
    protected int setLayout() {
        return R.layout.activity_add_group;
    }

    protected void initView() {
        mBack = bindView(R.id.iv_back);
        tv_checked = bindView(R.id.tv_save);
        listView = bindView(R.id.list);

        mEditText = bindView(R.id.et_search);
        mListView = bindView(R.id.list);

    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("newgroupid");
        //   Log.d("AddGroupMemberActivity", mGroupId);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newgroup = intent.getBooleanExtra("newgroupboo",true);


        mMembers = new ArrayList<>();
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(final List<String> strings) {
                mMap = new HashMap<String, EaseUser>();
                for (String s : strings) {
                    EaseUser user = new EaseUser(s);
                    mMap.put(s, user);
                    mMembers.add(new GroupContactBean(s, false));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter = new AddGroupAdapter(AddGroupMemberActivity.this);
                        mListAdapter.setFriends(mMembers);
                        mListView.setAdapter(mListAdapter);
                    }

                });

            }

            @Override
            public void onError(int i, String s) {

            }
        });
        // Log.d("AddGroupMemberActivity", mMembers.get(0));


        tv_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList.clear();
                Log.d("AddGroupMemberActivity", "mMembers.size():" + mMembers.size());
                for (int i = 0; i < mMembers.size(); i++) {
                    if (mMembers.get(i).isChecked() == true) {
                        addList.add(mMembers.get(i).getName());
                    }
                }
                Log.d("AddGroupMemberActivity", "mMembers.size():" + addList.size());
                save();

            }


        });

    }

    private void save() {
        if (addList.size() == 0) {
            Toast.makeText(AddGroupMemberActivity.this, "请选择好友", Toast.LENGTH_LONG).show();
            return;
        }
        if (isCreatingNewGroup) {
            //只有一個人，直接进入聊天界面
            if (addList.size() == 1 && newgroup == true) {
                String userId = addList.get(0);
                startActivity(new Intent(getApplicationContext(),
                        ChatActivity.class).putExtra("userId", userId));
                finish();
                return;
            } else {
                //否则进入添加成员
                addMember(addList);
            }
        }
    }

    private void addMember(final List<String> addList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String[] strings = new String[addList.size()];
                    for (int i = 0; i < addList.size(); i++) {
                        strings[i] = addList.get(i);
                        Log.d("AddGroupMemberActivity", strings[i]);
                    }
                    Log.d("AddGroupMemberActivity", mGroupId);
                    group = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                    if (EMClient.getInstance().getCurrentUser().equals(group.getGroupName())) {
                        EMClient.getInstance().groupManager().addUsersToGroup(mGroupId, strings);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddGroupMemberActivity.this, "添加成员", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

//    private void creatGroupNew(final List<String> addList) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d("AddGroupMemberActivity", "group");
//                    EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
//                    option.maxUsers = 200;
//                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
//                    String[] strings = new String[addList.size()];
//                    for (int i = 0; i < addList.size(); i++) {
//                        strings[i] = addList.get(i);
//                        Log.d("AddGroupMemberActivity", strings[i]);
//                    }
//                    EMClient.getInstance().groupManager().createGroup("aaaaa", null, strings, null, option);
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Log.d("AddGroupMemberActivity", "finish");
//                            Toast.makeText(AddGroupMemberActivity.this, "建群成功", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } catch (final HyphenateException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//
//    }


}



