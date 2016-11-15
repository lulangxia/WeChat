package com.zjl.mywechat.me;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.litesuits.orm.LiteOrm;
import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseFragment;
import com.zjl.mywechat.database.DBTools;
import com.zjl.mywechat.database.PersonBean;

import java.util.ArrayList;


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
                getActivity().startActivity(new Intent(getActivity(), OptionActivity.class));
            }
        });

        LiteOrm litorm = DBTools.getInstance().getmLiteOrm();
        ArrayList<PersonBean> myinfo = litorm.query(PersonBean.class);



        if (myinfo.size() != 0) {
            mPerson = myinfo.get(0);
            if (mPerson.getNickName() != null) {
                mUsername.setText(mPerson.getNickName());
            } else {
                mUsername.setText(mPerson.getName());
            }
            if (mPerson.getImgUrl() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(mPerson.getImgUrl());
                mHeadview.setImageBitmap(bitmap);
            }
        }

        String name = EMClient.getInstance().getCurrentUser();
        mUsername.setText(name);
    }



}
