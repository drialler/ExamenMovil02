package com.example.examenmoviles.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.examenmoviles.database.AppDatabase;
import com.example.examenmoviles.database.Word;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Word>> listOfWord;
    private AppDatabase appDatabase;

    public MainActivityViewModel(Application application) {
        super(application);
        listOfWord = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Word>>  getListOfWordObserver() {
        return listOfWord;
    }

    public void getAllWordList() {
        List<Word> wordsList =  appDatabase.wordListDao().getAllWordList();
        if(wordsList.size() > 0)
        {
            listOfWord.postValue(wordsList);
        }else {
            listOfWord.postValue(null);
        }
    }

    public void insertWord(String name, String image) {
        Word word = new Word();
        word.word = name;
        word.image = image;
        appDatabase.wordListDao().insertWord(word);
        getAllWordList();
    }

    public void updateWord(Word word) {
        appDatabase.wordListDao().updateWord(word);
        getAllWordList();
    }

    public void deleteWord(Word word) {
        appDatabase.wordListDao().deleteWord(word);
        getAllWordList();
    }

}
