package com.example.examenmoviles;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.examenmoviles.database.Word;
import java.io.File;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.MyViewHolder> {

    private Context context;
    private List<Word> wordsList;
    private HandleWordClick clickListener;

    public WordListAdapter(Context context, HandleWordClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setWordsList(List<Word> wordsList) {
        this.wordsList = wordsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    /*
       AQUI SE CAMBIA LOS EVENTOS PARA IMPLEMENTAR


   */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvWordName.setText(this.wordsList.get(position).word);


        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            clickListener.editItem(wordsList.get(position));
                            return super.onDoubleTap(e);
                        }
                    });

                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            gestureDetector.onTouchEvent(motionEvent);
                            return true;
                        }

        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            clickListener.editItem(wordsList.get(position));
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            clickListener.removeItem(wordsList.get(position));
                            super.onLongPress(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            Toast.makeText(context, "Dialogo actualizar y campo para subir imagen", Toast.LENGTH_LONG).show();
                            return super.onSingleTapConfirmed(e);
                        }
                    });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }

        });



    }

    @Override
    public int getItemCount() {
        if(wordsList == null || wordsList.size() == 0)
            return 0;
        else
            return wordsList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvWordName;
        ImageView imageView;
        RequestOptions requestOptions;
        public MyViewHolder(View view) {
            super(view);
            requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.ic_account)
                    .error(R.drawable.ic_account);
            tvWordName = view.findViewById(R.id.tvWordName);
            imageView = view.findViewById(R.id.image);

        }
        private void loadImage(File image) {
            if (image == null) return;

            Glide.with(itemView.getContext())
                    .asBitmap()
                    .apply(requestOptions)
                    .load(image)
                    .into(imageView);
        }
    }

    public interface  HandleWordClick {
        void itemClick(Word word);
        void removeItem(Word word);
        void editItem(Word word);

        void OnActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }


}
