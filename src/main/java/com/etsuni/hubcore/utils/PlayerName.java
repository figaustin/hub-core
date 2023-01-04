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
}
