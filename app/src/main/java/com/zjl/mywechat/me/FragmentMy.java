package com.zjl.mywechat.me;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseFragment;
import com.zjl.mywechat.database.PersonBean;

public class FragmentMy extends BaseFragment {

    private RelativeLayout mOption;
    private TextView mUsername;
    private ImageView mHeadview;
    private PersonBean mPerson;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main_my;
    }

    @Override
    protected void initView() {
        mOption = bindView(R.id.re_option_my);
        mUsername = bindView(R.id.te_my_username);
        mHeadview = bindView(R.id.iv_my_headPic);
    }

    @Override
    protected void initData() {
        mOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TestOptionActivity.class));
            }
        });

        String name = EMClient.getInstance().getCurrentUser();
        mUsername.setText(name);
    }
}
