package com.zjl.mywechat.tools;


import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class CreateAndDeleteMap {

    // 需要调用方法给Activities添加初值，退出登录时finish掉多余的Activities
    private static Map<String, Activity> mActivities = new HashMap<>();

    // 每一次创建新Activity都要调用？
    public static void onCre(Activity act) {
        mActivities.put(act.getClass().getSimpleName(), act);

    }


    public static Activity onDel(String str) {
        // 先移除，后返回被移除的对象
        return mActivities.remove(str);
    }

    public static Activity getOneActivity(String str){
        return mActivities.get(str);
    }

}
