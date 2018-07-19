package com.indiasguru.dictionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddOrEdit extends AppCompatActivity implements View.OnClickListener{
    private String url;
    private Intent intent;
    private String id, json;
    private TextInputEditText addOrEditWord, addOrEditMeaning;
    private AppCompatButton addOrEditButton;
    private AppDatabase appDatabase;
    private static PremiumWord premiumWord;
    private JSONObject jsonObject = new JSONObject();
    private JSONObject jsonObject1 = new JSONObject();
    private Toolbar toolbar;
    private UserObject userObject;
    private static Context context;
    private GetPremiumWordToEdit getPremiumWordToEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit);
        context = this;
        url = context.getResources().getString(R.string.url);
        intent = getIntent();
        url = url+ intent.getStringExtra("url");
        id = intent.getStringExtra("id");
        addOrEditWord = findViewById(R.id.addEditWord);
        addOrEditMeaning = findViewById(R.id.addEditMeaning);
        addOrEditButton = findViewById(R.id.addEditButton);
        toolbar = findViewById(R.id.toolbarAddOrEdit);
        userObject = new UserObject(intent.getStringExtra("username").toString(), intent.getStringExtra("password").toString(), intent.getBooleanExtra("isAdmin",false));
        if(url.equals("http://18.188.14.223:8080/updateSelectedWord")){
            toolbar.setTitle("Edit Word");
            getPremiumWordToEdit =  new GetPremiumWordToEdit();
            getPremiumWordToEdit.execute(id);
            addOrEditMeaning.setHint("Edit Meaning");
            addOrEditWord.setHint("Edit Word");
            addOrEditButton.setText("Edit");
        }else {
            toolbar.setTitle("Add Word");
            addOrEditButton.setText("Add to database");
        }
        setSupportActionBar(toolbar);
        addOrEditButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addEditButton:
                if(url.equals("http://18.188.14.223:8080/updateSelectedWord")){
                    editWOrd();
                }else {
                    addWord();
                }
                break;
        }

    }

    private void editWOrd() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();
        String word = addOrEditWord.getText().toString();
        String meaning = addOrEditMeaning.getText().toString();
        try {
            jsonObject.put("word",word);
            jsonObject.put("meaning", meaning);
            jsonObject.put("id", premiumWord.getId());
            jsonObject.put("category", premiumWord.getCategory());
            jsonObject1.put("word", jsonObject);
            json = jsonObject1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonRespose = new JSONObject(response);
                            if (jsonRespose.get("status").equals("success")){
                                Toast.makeText(getApplicationContext(),"Word updated successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Solw network or check your connection", Toast.LENGTH_SHORT);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void addWord() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding data to database...");
        progressDialog.show();
        String word = addOrEditWord.getText().toString();
        String meaning = addOrEditMeaning.getText().toString();
        try {
            jsonObject.put("word",word);
            jsonObject.put("meaning", meaning);
            jsonObject.put("category", "miscellaneous");
            jsonObject1.put("word", jsonObject);
            json = jsonObject1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonRespose = new JSONObject(response);
                            if (jsonRespose.get("status").equals("success")){
                                Toast.makeText(getApplicationContext(),"Word added successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"Please try again in some time", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Solw network or check your connection", Toast.LENGTH_SHORT);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(AddOrEdit.this, AddEditDelete.class);
        intent.putExtra("id",userObject.getId());
        intent.putExtra("username", userObject.getUsername());
        intent.putExtra("password", userObject.getPassword());
        startActivity(intent);
        finish();
    }

    class GetPremiumWordToEdit extends AsyncTask<String, Void, PremiumWord>{
        private AppDatabase appDatabase;
        private PremiumWord premiumWord;
        @Override
        protected PremiumWord doInBackground(String... strings) {
            appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
            premiumWord = appDatabase.wordsDao().getPremiunWordToEdit(strings[0]);
            return premiumWord;
        }

        @Override
        protected void onPostExecute(PremiumWord premiumWord) {
            AddOrEdit.premiumWord = premiumWord;
            addOrEditWord.setText(premiumWord.getWord());
            addOrEditMeaning.setText(premiumWord.getMeaning());
        }
    }
}
