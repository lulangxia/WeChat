package com.zjl.mywechat.register.presenter;

import android.util.Log;

import com.zjl.mywechat.register.model.IRegisterModel;
import com.zjl.mywechat.register.model.OnFinishedListener;
import com.zjl.mywechat.register.model.RegisterModelImp;
import com.zjl.mywechat.register.view.IRegisterView;

/**
 * Created by dllo on 16/10/31.
 */

public class RegisterPresenter {

    private IRegisterModel mModel;
    private IRegisterView mView;

    public RegisterPresenter(IRegisterView view) {
        mView = view;
        mModel = new RegisterModelImp();

    }

    public void startRequest(String username, String password, final String path, final String nickname) {
        mView.showDialog();

        Log.d("RegisterPresenter", "aaa1");
        mModel.startRegister(username, password, new OnFinishedListener() {
            @Override
            public void onFinished(final String username, final String password) {
                mView.dismissDialog();
                Log.d("RegisterPresenter", "aaa2");
                mView.onResponse(username, password);
                Log.d("RegisterPresenter", "aaa3");
            }

            @Override
            public void onError(Throwable error) {
                mView.dismissDialog();
                mView.onError(error);
            }
        });
    }
}

