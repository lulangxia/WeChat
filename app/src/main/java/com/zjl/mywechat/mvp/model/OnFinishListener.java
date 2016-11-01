package com.zjl.mywechat.mvp.model;


import com.zjl.mywechat.bean.RequestBean;

import java.util.ArrayList;

public interface OnFinishListener {

    void onSuccess(ArrayList<RequestBean> arrayList);
    void onError();


}
