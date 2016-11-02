package com.zjl.mywechat.socalfriend;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.zjl.mywechat.R;
import com.zjl.mywechat.base.BaseFragment;

public class FragmentFind  extends BaseFragment implements View.OnClickListener {


	private RelativeLayout mFriend;
	private RelativeLayout mErweima;
	private RelativeLayout mYaoyiyao;
	private RelativeLayout mYouxi;
	private RelativeLayout mGouwu;
	private RelativeLayout mFujian;
	private RelativeLayout mPiaoliuping;

	@Override
	protected int setLayout() {
		return R.layout.fragment_find;
	}

	@Override
	protected void initView() {
		mFriend = bindView(R.id.re_friends);
		mErweima = bindView(R.id.re_erweima);
		mYaoyiyao = bindView(R.id.re_yaoyiyao);
		mYouxi = bindView(R.id.re_youxi);
		mGouwu = bindView(R.id.re_gouwu);
		mFujian = bindView(R.id.re_fujin);
		mPiaoliuping = bindView(R.id.re_piaoliuping);
	}

	@Override
	protected void initData() {
		mErweima.setOnClickListener(this);
		mFriend.setOnClickListener(this);
		mYaoyiyao.setOnClickListener(this);
		mYouxi.setOnClickListener(this);
		mGouwu.setOnClickListener(this);
		mPiaoliuping.setOnClickListener(this);
		mFujian.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.re_friends:
				String userID = PreferenceManager.getIntance().getCurrentUserName();
				if (!TextUtils.isEmpty(userID)) {
					startActivity(new Intent(getActivity(),SocialMainActivity.class).putExtra("userID",userID));
				}
				break;
			case R.id.re_erweima:
				break;
			case R.id.re_yaoyiyao:
				break;
			case R.id.re_youxi:
				break;
			case R.id.re_fujin:
				break;
			case R.id.re_piaoliuping:
				break;
			case R.id.re_gouwu:
				break;
		}
	}
}
