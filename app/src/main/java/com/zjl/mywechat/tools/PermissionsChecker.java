package com.zjl.mywechat.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.zjl.mywechat.app.MyApp;

/**
 * Created by dllo on 16/11/5.
 */

public class PermissionsChecker {

    private final Context mContext;

    public PermissionsChecker() {
        mContext = MyApp.getmContext();
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
