package com.indiasguru.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ShowWord extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView word, meaning;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
        toolbar = findViewById(R.id.toolbarShowWord);
        toolbar.setTitle("Word");
        setSupportActionBar(toolbar);
        word = findViewById(R.id.wordShowWord);
        meaning = findViewById(R.id.meaningShowWord);
        intent = getIntent();
        word.setText(intent.getStringExtra("word"));
        word.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        meaning.setText(intent.getStringExtra("meaning"));
    }
}
