package com.indiasguru.dictionary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/10/2018.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    public WordsAdapter(List<SampleWord> sampleWordList, Context context) {
        this.wordList = sampleWordList;
        this.context = context;
    }

    private List<SampleWord> wordList;
    private List positionsClicked = new ArrayList();
    private Context context;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SampleWord sampleWord = wordList.get(position);
        holder.word.setText(sampleWord.getWord());
        holder.meaning.setText(sampleWord.getMeaning());
        holder.meaning.setVisibility(View.GONE);
        if(position%2 != 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#9E9E9E"));
            holder.word.setBackgroundColor(Color.parseColor("#9E9E9E"));
        }else {
            holder.cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.word.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ShowWord.class).putExtra("word", holder.word.getText()).putExtra("meaning", holder.meaning.getText()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView word, meaning;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word);
            meaning = itemView.findViewById(R.id.meaning);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
