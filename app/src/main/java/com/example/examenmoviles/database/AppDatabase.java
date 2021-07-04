package com.example.examenmoviles.database;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.ByteArrayOutputStream;

@Database(entities =  {Word.class, Items.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WordListDao wordListDao();

    public static AppDatabase INSTANCE;

    public static AppDatabase getDBinstance(Context context) {
        if(INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "WordDb")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}
