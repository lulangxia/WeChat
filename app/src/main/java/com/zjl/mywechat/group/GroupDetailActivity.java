package com.zjl.mywechat.group;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.exceptions.HyphenateException;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.widget.ExpandGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GroupDetailActivity extends BaseAty implements GroupGridAdapter.OndeleteListener {


    private ExpandGridView mExpandGridView;
    private EaseSwitchButton mEaseSwitchButton;
    private ImageView mBack;
    private TextView mMembersnum;
    private List<String> mName;
    private GroupGridAdapter mAdapter;
    private EMGroup mGroup;
    private RelativeLayout mCleangroup;
    private RelativeLayout mChangename;
    private TextView mGroupname;
    private String mGroupId;

    private ProgressDialog progressDialog;
    private Button mExit;


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
        mCleangroup = bindView(R.id.clear_all_history);
        mChangename = bindView(R.id.rl_change_group_name);
        mGroupname = bindView(R.id.groupname_detail);
        mExit = bindView(R.id.btn_exit_grp);
    }

    @Override
    protected void initData() {


        Intent intent1 = getIntent();
        mGroupId = intent1.getStringExtra("groupId");
        Log.d("GroupDetailActivity", mGroupId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mGroup = null;
                try {
                    mGroup = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                    mName = mGroup.getMembers();
                    Log.d("GroupDetailActivity", "name.size():" + mName.size());
                    Log.d("GroupDetailActivity", "group.getMemberCount():" + mGroup.getMemberCount());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mGroup != null) {
                            mGroupname.setText(mGroup.getGroupName());
                        } else {
                            mGroupname.setText("未命名");
                        }
                        if (EMClient.getInstance().getCurrentUser().equals(mName.get(0))) {
                            mExit.setText("退出并解散群组");
                            mExit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().groupManager().destroyGroup(mGroupId);
                                            } catch (HyphenateException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startActivity(new Intent(GroupDetailActivity.this, GrouplistActivity.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    }).start();

                                }
                            });
                        } else {
                            mExit.setText("退出群组");
                            mExit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().groupManager().leaveGroup(mGroupId);
                                            } catch (HyphenateException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                    startActivity(new Intent(GroupDetailActivity.this, GrouplistActivity.class));
                                                    finish();
                                                }
                                            });

                                        }
                                    }).start();
                                }
                            });
                        }
                        mAdapter = new GroupGridAdapter(GroupDetailActivity.this);
                        mAdapter.setUsers(mName);
                        mAdapter.setGroupId(mGroupId);
                        mAdapter.setOndeleteListener(GroupDetailActivity.this);
                        mExpandGridView.setAdapter(mAdapter);
                        mMembersnum.setText("(" + mName.size() + ")");
                    }
                });


            }
        }).start();


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mEaseSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.get(0).equals(EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(GroupDetailActivity.this, "群主屏蔽不了聊天...why?", Toast.LENGTH_SHORT).show();
                } else {
                    toggleBlockGroup();
                }
            }
        });


        mCleangroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().chatManager().deleteConversation(mGroupId, true);
                Toast.makeText(GroupDetailActivity.this, "已清除该群聊天记录", Toast.LENGTH_SHORT).show();
            }
        });


        mChangename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mName.get(0).equals(EMClient.getInstance().getCurrentUser()))) {
                    Toast.makeText(GroupDetailActivity.this, "只有群主才可以修改群组名称", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder custom = new AlertDialog.Builder(GroupDetailActivity.this);
                    custom.setTitle("请输入新的群名称");
                    View view = LayoutInflater.from(GroupDetailActivity.this).inflate(R.layout.group_name_dialog, null);
                    final EditText newname = (EditText) view.findViewById(R.id.name_newgroup);
                    custom.setPositiveButton("取消修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    custom.setNegativeButton("确定修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EMClient.getInstance().groupManager().changeGroupName(mGroupId, newname.getText().toString());
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mGroupname.setText(newname.getText().toString());
                                            Toast.makeText(GroupDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                                            String s = "changegroupname";
                                            EventBus.getDefault().post(s);
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                    custom.setView(view);
                    custom.show();

                }
            }
        });


    }


    @Override
    public void delete(String s) {
        new AsyncTask<String, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {
                try {
                    EMClient.getInstance().groupManager().removeUserFromGroup(mGroup.getGroupId(), params[0]);
                    mName.remove(params[0]);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                return mName;
            }

            @Override
            protected void onPostExecute(List<String> list) {
                super.onPostExecute(list);
                mAdapter.setUsers(list);
                mMembersnum.setText("(" + list.size() + ")");
            }
        }.execute(s);
    }

    private void toggleBlockGroup() {
        //Log.d("GroupDetailActivity11", mGroupId);
        if (mEaseSwitchButton.isSwitchOpen()) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(getString(R.string.Is_unblock));
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().unblockGroupMessage(mGroupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mEaseSwitchButton.closeSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mEaseSwitchButton.closeSwitch();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.remove_group_of, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).start();

        } else {
            String st8 = getResources().getString(R.string.group_is_blocked);
            final String st9 = getResources().getString(R.string.group_of_shielding);
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(st8);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().blockGroupMessage(mGroupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d("GroupDetailActivity11", "y");
                                mEaseSwitchButton.openSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d("GroupDetailActivity11", "n");
                                mEaseSwitchButton.openSwitch();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }


}
