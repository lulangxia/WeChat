package com.zjl.mywechat.mvp.model;


import java.util.ArrayList;

public interface OnFinishListener {

    <T> void onSuccess(ArrayList<T> arrayList);
    void onError();


}
