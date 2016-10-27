package com.zjl.mywechat.bean;


public class TelListBean {

    private String img;
    private String name;
    private String pinYinName;

    public String getPinYinName() {
        return pinYinName;
    }

    public void setPinYinName(String pinYinName) {
        this.pinYinName = pinYinName;
    }

    public TelListBean() {
    }

    public TelListBean(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
