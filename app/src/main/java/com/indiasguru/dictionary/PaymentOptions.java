package com.indiasguru.dictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class PaymentOptions extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        toolbar = findViewById(R.id.toolbarPaymentOptions);
        toolbar.setTitle("Payment Options");
        setSupportActionBar(toolbar);
    }
}
