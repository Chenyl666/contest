package com.contest.entity.user;

import java.io.Serializable;

public enum UserType implements Serializable {
    PARTICIPANT("参赛者"),
    ORGANIZER("举办方"),
    ADMIN("管理者");

    public final String typeName;

    UserType(String typeName) {
        this.typeName = typeName;
    }
}
