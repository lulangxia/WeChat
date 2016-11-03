package com.zjl.mywechat.bean;


import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

public class RequestBean {




//    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;


    @PrimaryKey(AssignType.BY_MYSELF)
    private String name;


//    private String img;
    private String reason;

    // 主动添加别人且对方仍未回复：1， 被其他人添加：0，对方同意了你的请求：2， 对方拒绝了你的请求：3
    private int isPositive = 0;


    // 未读：0，同意：1，拒绝：2
    private int isAgree = 0;








    // 判断是否已读（默认是0，来一条新消息就 + 1），总的未读消息数目是所有成员isRead数目之和。


    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }

    public int getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(int isRead) {
        this.isPositive = isRead;
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

//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
}
