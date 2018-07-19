package com.indiasguru.dictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class PrivacyPolicy extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        toolbar = findViewById(R.id.toolbarPrivacyPolicy);
        toolbar.setTitle("Privacy Policy");
        setSupportActionBar(toolbar);
    }
}
