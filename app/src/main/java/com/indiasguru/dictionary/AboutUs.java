package com.indiasguru.dictionary;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

public class AboutUs extends AppCompatActivity {
    private Toolbar toolbar;
    private SpannableStringBuilder stringBuilder = new SpannableStringBuilder("Mr. Yogesh P. Thakur (1987), SET, B.A. , B.P.Ed., M.P.Ed. , M. Phill (Physical Education) is a well acknowledged physical educationist highly specialized in Research,Physiology , Sports Psychology and Management.");
    private AppCompatTextView aboutUsFirstLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar = findViewById(R.id.toolbarAboutUs);
        toolbar.setTitle("CONTACT US");
        setSupportActionBar(toolbar);
        stringBuilder.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, 210, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        aboutUsFirstLine = findViewById(R.id.aboutUsText);
        aboutUsFirstLine.setText(stringBuilder);
    }
}
