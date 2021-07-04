package com.example.examenmoviles.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "image")
    public String image;

}
