package com.tokyo.beach.application.user;

public class User {
    private String name;

    @SuppressWarnings("unused")
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
