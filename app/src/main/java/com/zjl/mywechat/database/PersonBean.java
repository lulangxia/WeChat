package com.zjl.mywechat.database;


import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

// 存放自己的信息
// 存放好友信息


public class PersonBean {


    // id自增,没必要写set方法
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private String name;
    private String nickName;
    private String imgUrl;
    private String password;// 存个人信息


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
