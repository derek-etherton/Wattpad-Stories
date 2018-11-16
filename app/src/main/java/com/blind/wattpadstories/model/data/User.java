package com.blind.wattpadstories.model.data;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String avatar;
    private String fullname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return name;
    }
}
