package com.zjl.mywechat.addfriends;


public class RequestBean {

    private String name;
    private String img;
    private String reason;

    private int isRead = 0;

    // 判断是否已读（默认是0，来一条新消息就 + 1），总的未读消息数目是所有成员isRead数目之和。



    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public RequestBean() {
    }

    public RequestBean(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
