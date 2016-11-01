package com.zjl.mywechat.socalfriend;

import android.content.Context;
import android.content.SharedPreferences;

import com.zjl.mywechat.app.MyApp;

/**
 * Created by dllo on 16/11/1.
 */

public class PreferenceManager {

    public static final String PREFERENCE_NAME = "saveInfo";
    private static String SHARED_KEY_CURRENTUSER_USERNAME = "SHARED_KEY_CURRENTUSER_USERNAME";

    private static PreferenceManager mPreferenceManager;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static PreferenceManager getIntance(){
        if (mPreferenceManager == null) {
            synchronized (PreferenceManager.class){
                if (mPreferenceManager == null) {
                    mPreferenceManager = new PreferenceManager(MyApp.getmContext());
                }
            }
        }
        return mPreferenceManager;
    }

    public String getCurrentUserName(){
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_USERNAME,null);
    }
    public void setCurrentUserName(String username){
        mEditor.putString(SHARED_KEY_CURRENTUSER_USERNAME,username);
        mEditor.commit();
    }

}
