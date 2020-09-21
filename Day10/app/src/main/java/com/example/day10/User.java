package com.example.day10;

public class User {
    public String name;
    public String tier;

    public User() {} // Default Constructor For firebase

    public User(String name, String tier) {
        this.name = name;
        this.tier = tier;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", tier='" + tier + '\'' +
                '}';
    }
}
