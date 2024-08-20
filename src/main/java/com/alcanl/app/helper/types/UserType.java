package com.alcanl.app.helper.types;

public enum UserType {
    ADMIN("Admin Kullanıcı"), USER("Normal Kullanıcı");
    private final String name;
    UserType(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
