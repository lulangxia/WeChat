package com.zjl.mywechat.useless;

import java.util.List;

/**
 * Created by dllo on 16/11/9.
 */

public class GroupDetailBean {
    private boolean owner;
    private List<String> names;

    public GroupDetailBean() {
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public GroupDetailBean(boolean owner, List<String> names) {
        this.owner = owner;
        this.names = names;
    }
}
