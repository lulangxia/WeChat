package com.zjl.mywechat.login.presenter;

import android.util.Log;

import com.zjl.mywechat.login.modle.ILoginModel;
import com.zjl.mywechat.login.modle.LoginModelImp;
import com.zjl.mywechat.login.modle.OnFinishListener;
import com.zjl.mywechat.login.view.ILoginView;
import com.zjl.mywechat.socalfriend.modle.PreferenceManager;

/**
 * Created by dllo on 16/10/31.
 */

public class LoginPresrnter {
    private ILoginView mLoginView;
    private ILoginModel mLoginModel;

    public LoginPresrnter(ILoginView loginView) {
        mLoginView = loginView;
        mLoginModel = new LoginModelImp();
    }

    public void startRequest(String username, String password){
        mLoginView.showDialog();


        mLoginModel.startLogin(username, password, new OnFinishListener() {
            @Override
            public void onFinished(String username, String password) {
                Log.d("LoginPresrnter", username);
                PreferenceManager.getIntance().setCurrentUserName(username);
                mLoginView.dismissDialog();
                mLoginView.onResponse(username, password);
            }

            @Override
            public void onError(int i, String s) {
                mLoginView.onError(i,s);
            }


        });
    }
}
