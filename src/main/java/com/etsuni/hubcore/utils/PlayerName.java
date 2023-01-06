package com.etsuni.hubcore.utils;

import java.sql.Timestamp;

public class PlayerName {

    public String name;

    public Boolean isNickname;

    public Timestamp createdAt;

    public PlayerName(String name, Boolean isNickname, Timestamp createdAt) {
        this.name = name;
        this.isNickname = isNickname;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNickname() {
        return isNickname;
    }

    public void setNickname(Boolean nickname) {
        isNickname = nickname;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }




}
