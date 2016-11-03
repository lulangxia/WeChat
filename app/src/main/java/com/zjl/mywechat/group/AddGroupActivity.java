package com.zjl.mywechat.group;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.EaseUser;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseAty;
import com.zjl.mywechat.base.BaseListViewAdapter;
import com.zjl.mywechat.base.BaseListViewHolder;
import com.zjl.mywechat.bean.RequestBean;
import com.zjl.mywechat.database.DBTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroupActivity extends BaseAty {


    private ImageView mBack;

    private ImageView iv_search;
    private TextView tv_checked;
    private ListView listView;
    //是否新建群
    protected boolean isCreatingNewGroup;
    // private PickContactAdapter contactAdapter;
    private List<String> exitingMembers = new ArrayList<String>();
    // 可滑动的显示选中用户的View
    private LinearLayout menuLinerLayout;
    // 选中用户总数,右上角显示
    private String userId = null;
    private String groupId = null;
    private String groupname;
    // 添加的列表
    private List<String> addList = new ArrayList<String>();
    private List<EaseUser> friendList;
    private EMGroup group;
    private EditText mEditText;
    private HashMap<String, EaseUser> mMap;
    private List<RequestBean> mContacts;
    private List<String> mMembers;
    private ListView mListView;
    private ListAdapter mListAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_add_group;
    }

    protected void initView() {
        mBack = bindView(R.id.iv_back);
        tv_checked = (TextView) this.findViewById(R.id.tv_save);
        listView = (ListView) findViewById(R.id.list);
        iv_search = (ImageView) this.findViewById(R.id.iv_search);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        menuLinerLayout = (LinearLayout) this
                .findViewById(R.id.linearLayoutMenu);

        mEditText = bindView(R.id.et_search);
        mListView = bindView(R.id.list);

    }


    @Override
    protected void initData() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_checked.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // save();
            }

        });
        mMembers = new ArrayList<>();
        mContacts = DBTools.getInstance().getmLiteOrm().query(RequestBean.class);
        for (int i = 0; i < mContacts.size(); i++) {
            Log.d("AddGroupActivity", mContacts.get(i).getName());
            if (mContacts.get(i).getIsAgree() == 1) {

                Log.d("AddGroupActivity", mContacts.get(i).getName());
                mMembers.add(mContacts.get(i).getName());
            }
        }
       // Log.d("AddGroupActivity", mMembers.get(0));

        mListAdapter = new BaseListViewAdapter<String>(this, mMembers, R.layout.item_group) {
            @Override
            public void convent(BaseListViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.name, s);
            }
        };
        mListView.setAdapter(mListAdapter);


    }



}
