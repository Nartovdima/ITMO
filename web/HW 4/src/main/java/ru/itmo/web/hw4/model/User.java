package ru.itmo.web.hw4.model;

public class User {
    public enum UserColors {
        RED,
        GREEN,
        BLUE
    }
    private final long id;
    private final String handle;
    private final String name;
    private final UserColors color;
    public User(long id, String handle, String name, UserColors color) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.color = color;
    }

    public UserColors getColor() {
        return color;
    }
    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }
}
