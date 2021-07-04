package com.example.examenmoviles.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.examenmoviles.database.AppDatabase;
import com.example.examenmoviles.database.Items;

import java.util.List;

;

public class ShowItemListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel(Application application) {
        super(application);
        listOfItems = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Items>>  getItemsListObserver() {
        return listOfItems;
    }

    public void getAllItemsList(int wordID) {
        List<Items> itemsList=  appDatabase.wordListDao().getAllItemsList(wordID);
        if(itemsList.size() > 0)
        {
            listOfItems.postValue(itemsList);
        }else {
            listOfItems.postValue(null);
        }
    }

    public void insertItems(Items item) {

        appDatabase.wordListDao().insertItems(item);
        getAllItemsList(item.wordId);
    }

    public void updateItems(Items item) {
        appDatabase.wordListDao().updateItems(item);
        getAllItemsList(item.wordId);
    }

    public void deleteItems(Items item) {
        appDatabase.wordListDao().deleteItem(item);
        getAllItemsList(item.wordId);
    }

}
