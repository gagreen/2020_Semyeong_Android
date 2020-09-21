package com.example.day7_1.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wish_list")
public class SearchItem {


    @PrimaryKey
    long productId;

    @ColumnInfo(name="title")
    String title;

    @ColumnInfo(name="link")
    String link;

    @ColumnInfo(name="image")
    String image;

    @ColumnInfo(name="lprice")
    int lprice;

    @ColumnInfo(name="hprice")
    int hprice;

    @ColumnInfo(name="mallName")
    String mallName;

    public long getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public int getLprice() {
        return lprice;
    }

    public int getHprice() {
        return hprice;
    }

    public String getMallName() {
        return mallName;
    }
}
