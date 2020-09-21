package com.example.day7_1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ISearchDao {

    @Query("SELECT * FROM wish_list")
    List<SearchItem> getAll();

    @Insert
    void insertItem(SearchItem item);

    @Delete
    void deleteItem(SearchItem item);
}
