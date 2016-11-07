package com.zjl.mywechat.group;

/**
 * Created by dllo on 16/11/4.
 */

public class GroupContactBean {
    private String name;
    private boolean isChecked=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public GroupContactBean(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }
}
