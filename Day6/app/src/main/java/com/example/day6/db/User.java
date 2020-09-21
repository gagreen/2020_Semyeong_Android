package com.example.day6.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Raw의 형태를 객체화하는 것
@Entity(tableName = "userInfo")
public class User {
    public User(String name) {
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;
}
