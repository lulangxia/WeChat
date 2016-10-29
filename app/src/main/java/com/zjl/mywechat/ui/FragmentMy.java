package com.zjl.mywechat.ui;


import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseFragment;

public class FragmentMy extends BaseFragment {

    private RelativeLayout mOption;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main_my;
    }

    @Override
    protected void initView() {
        mOption = bindView(R.id.re_option_my);
    }

    @Override
    protected void initData() {
        mOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(),TestOptionActivity.class));
            }
        });


    }
}
