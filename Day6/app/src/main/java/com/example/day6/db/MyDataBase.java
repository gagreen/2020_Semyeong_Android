package com.example.day6.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1) // Entitiy들과 버전 지정
public abstract class MyDataBase extends RoomDatabase {
    public abstract UserDao userDao(); // UserDao를 반환해주는 메서드
}
