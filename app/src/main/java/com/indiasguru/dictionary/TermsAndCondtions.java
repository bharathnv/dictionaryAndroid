package com.indiasguru.dictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TermsAndCondtions extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condtions);
        toolbar = findViewById(R.id.toolbarTermsAndConditions);
        toolbar.setTitle("Terms and Conditions");
        setSupportActionBar(toolbar);
    }
}
