package com.example.day7_1.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SearchItem.class}, version = 1)
public abstract class SearchDB extends RoomDatabase {
    public abstract ISearchDao dao();
}
