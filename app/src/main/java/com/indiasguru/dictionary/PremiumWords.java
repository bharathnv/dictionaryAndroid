package com.indiasguru.dictionary;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PremiumWords extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private String URL_DATA;
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static List<PremiumWord> premiumWordsList, premiumWordsList1;
    private AppDatabase appDatabase;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private String meaning;
    private UserObject userObject;
    private Toolbar toolbar;
    public static AppCompatButton suggestButton;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_words);
        context = this;
        URL_DATA = context.getResources().getString(R.string.url)+"/getWordsList";
        Intent intent = getIntent();
        userObject = new UserObject(intent.getStringExtra("username").toString(), intent.getStringExtra("password").toString(), intent.getBooleanExtra("isAdmin",false));
        toolbar = findViewById(R.id.toolbarPremium);
        toolbar.setTitle("Premium Words");
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recyclerViewPremium);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        premiumWordsList = new ArrayList<PremiumWord>();
        premiumWordsList1 = new ArrayList<PremiumWord>();
        suggestButton = findViewById(R.id.suggestButton);
        suggestButton.setOnClickListener(this);
        loadRecyclerViewData();
    }

    public void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("success")){
                                JSONArray wordsList = jsonObject.getJSONArray("wordsList");
                                for (int i=0; i<wordsList.length(); i++){
                                    JSONObject word = wordsList.getJSONObject(i);
                                    meaning= word.getString("meaning").replace("\\n", "\n");
                                    PremiumWord premiumWord = new PremiumWord(word.getString("id"),
                                            word.getString("word"),
                                            meaning,
                                            word.getString("category"));
                                    premiumWordsList.add(premiumWord);
                                }
                                InteractWithLocalDatabase.getInstance(getApplicationContext(), premiumWordsList, null, null).execute("insertPremiumWords");
                                mAdapter = new PremiumWordsAdapter(premiumWordsList, PremiumWords.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }else if(jsonObject.getString("status").equals("failure")){
                                GetFromLocalDatabase.getInstance(getApplicationContext(), null, null, "premium").execute("getPremiumWords");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GetFromLocalDatabase.getInstance(getApplicationContext(), null, null, "premium").execute("getPremiumWords");
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Slow network or check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.premium_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.searchPremium);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        GetFromLocalDatabase.getInstance(getApplicationContext(), null, "%"+newText.toString()+"%", "premium").execute("getSearchedPremiumWords");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutDropDown:
                DeleteFromLocalDatabase.getInstance(getApplicationContext(), userObject, null).execute("deleteUser");
                Intent intent = new Intent(PremiumWords.this, SampleWords.class);
                startActivity(intent);
                finish();
                break;

            case R.id.aboutUsDropDown:
                Intent intent1 = new Intent(PremiumWords.this, AboutUs.class);
                startActivity(intent1);
                break;
            case R.id.privacyPolicyDropDown:
                Intent intent2 = new Intent(PremiumWords.this, PrivacyPolicy.class);
                startActivity(intent2);
                break;
            case R.id.refundPolicyDropDown :
                Intent intent3 = new Intent(PremiumWords.this, RefundPolicy.class);
                startActivity(intent3);
                break;
            case R.id.termsAndConditionsDropDown:
                Intent intent4 = new Intent(PremiumWords.this, TermsAndCondtions.class);
                startActivity(intent4);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.suggestButton:
                Intent intent = new Intent(PremiumWords.this, SuggestWord.class);
                intent.putExtra("isAdmin",false);
                intent.putExtra("id",userObject.getId());
                intent.putExtra("username", userObject.getUsername());
                intent.putExtra("password", userObject.getPassword());
                startActivity(intent);
                finish();
                break;
        }

    }
}
