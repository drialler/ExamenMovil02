package com.example.examenmoviles;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenmoviles.database.DataConverter;
import com.example.examenmoviles.database.Word;
import com.example.examenmoviles.viewmodel.MainActivityViewModel;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WordListAdapter.HandleWordClick {

    private MainActivityViewModel viewModel;
    private TextView noResulttextView;
    private RecyclerView recyclerView;
    private WordListAdapter wordListAdapter;
    private Word wordForEdit;
    ImageView imageView;
    Bitmap bmpImage;
    private RequestOptions requestOptions;
    String file = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(false)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account);

        getSupportActionBar().setTitle("Lista de Palabras");
        //imageView = findViewById(R.id.imageView);
        bmpImage = null;
        noResulttextView  = findViewById(R.id.noResult);
        recyclerView  = findViewById(R.id.recyclerView);

        ImageView addNew = findViewById(R.id.addNewWordImageView);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });

        initViewModel();
        initRecyclerView();
        viewModel.getAllWordList();
    }

    private void initRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordListAdapter = new WordListAdapter(this, this);
        recyclerView.setAdapter(wordListAdapter);

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getListOfWordObserver().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if(words == null) {
                    noResulttextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    //show in the recyclerview
                    wordListAdapter.setWordsList(words);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResulttextView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddCategoryDialog(boolean isForEdit) {

        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate( R.layout.add_word_layout, null);

        EditText enterWordInput = dialogView.findViewById(R.id.enterWordInput);
        //Botones
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        if(isForEdit){
            createButton.setText("Actualizar");
            enterWordInput.setText(wordForEdit.word);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = enterWordInput.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Introduzca una palabra", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isForEdit){
                    wordForEdit.word = name;
                    viewModel.updateWord(wordForEdit);
                } else {
                    //Aqui se llama al modelo .

                    viewModel.insertWord(name,file);

                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Word word) {
        Intent intent = new Intent(MainActivity.this, ShowItemsListActivity.class);
        intent.putExtra("word_id", word.uid);
        intent.putExtra("word_name", word.word);
        intent.putExtra("word_image", word.image);
        startActivity(intent);
    }

    @Override
    public void removeItem(Word word) {

        viewModel.deleteWord(word);
    }

    @Override
    public void editItem(Word word) {
        this.wordForEdit = word;
        showAddCategoryDialog(true);
    }
    public final static int PICK_GALLERY = 2002;
    public void btn_image(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_GALLERY);

    }
    @Override
    public void OnActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        file = getRealPathFromUri(data.getData());

        loadImage(new File(file));
    }


    private void loadImage(File image) {
        if (image == null) return;

        Glide.with(this)
                .asBitmap()
                .apply(requestOptions)
                .load(image)
                .into(imageView);
    }


    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

}