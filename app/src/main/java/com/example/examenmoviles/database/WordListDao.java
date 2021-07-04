package com.example.examenmoviles.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordListDao {

    @Query("Select * from Word")
    List<Word> getAllWordList();

    @Insert
    void insertWord(Word...words);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteWord(Word word);

    @Query("Select * from Items where wordId = :catId")
    List<Items> getAllItemsList(int catId);

    @Insert
    void insertItems(Items items);

    @Update
    void updateItems(Items items);

    @Delete
    void deleteItem(Items items);


}
