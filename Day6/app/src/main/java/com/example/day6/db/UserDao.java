package com.example.day6.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Data Access Object
@Dao
public interface UserDao {
    @Query("SELECT * FROM userInfo")
    List<User> getAll();

    @Query("SELECT * FROM userInfo WHERE name LIKE :keyword")
    List<User> searchUserByName(String keyword);

    @Insert
    void insert(User user);


}
