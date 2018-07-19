package com.indiasguru.dictionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SuggestWord extends AppCompatActivity implements View.OnClickListener{
    private TextInputEditText word;
    private String wordS, url;
    private AppCompatButton submit;
    private Context context;
    private android.app.AlertDialog.Builder builder;
    private UserObject userObject;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_word);
        context = this;
        url = context.getResources().getString(R.string.url)+"/sendSuggestionWordMailToAdmin";
        intent = getIntent();
        userObject = new UserObject(intent.getStringExtra("username").toString(), intent.getStringExtra("password").toString(), intent.getBooleanExtra("isAdmin",false));
        word = findViewById(R.id.suggestWord);
        submit = findViewById(R.id.suggestButton);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.suggestButton:
                wordS = word.getText().toString();
                suggestWord();
                break;
        }
    }

    private void suggestWord() {
        url = url+"?word="+wordS;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("status").equals("success")){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new android.app.AlertDialog.Builder(context);
                                }
                                progressDialog.dismiss();
                                builder.setTitle("Thank you for suggesting.")
                                        .setMessage("Your word is under review")
                                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Some internal error occured. Please try agin after some time", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Slow network or check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuggestWord.this, PremiumWords.class);
        intent.putExtra("isAdmin",false);
        intent.putExtra("id",userObject.getId());
        intent.putExtra("username", userObject.getUsername());
        intent.putExtra("password", userObject.getPassword());
        startActivity(intent);
        finish();
    }
}
