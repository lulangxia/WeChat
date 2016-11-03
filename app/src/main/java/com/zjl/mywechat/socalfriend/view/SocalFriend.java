package com.zjl.mywechat.socalfriend.view;

/**
 * Created by dllo on 16/10/31.
 */

public class SocalFriend {
    private static final String TAG = "SocalFriend";
    private String username;
    private static final class SingletonHolder{
        private static final SocalFriend sInstance = new SocalFriend();
    }

    public static SocalFriend getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * set current username
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
        //demoModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName() {
        if (username == null) {
           // username = demoModel.getCurrentUsernName();
        }
        return username;
    }
}
