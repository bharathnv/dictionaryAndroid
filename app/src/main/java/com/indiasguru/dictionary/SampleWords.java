package com.indiasguru.dictionary;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
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

public class SampleWords extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private String URL_DATA;
    public static RecyclerView mRecyclerViewSampleWords;
    public static RecyclerView.Adapter mAdapterSamplewords;
    public static List<SampleWord> sampleWordsList, sampleWordsList1;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private String meaning;
    private android.support.v7.widget.Toolbar toolbar;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_words);
        context = this;
        URL_DATA = context.getResources().getString(R.string.url)+"/getSampleWordsList";
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sample Words");
        setSupportActionBar(toolbar);
        mRecyclerViewSampleWords = findViewById(R.id.recyclerView);
        mRecyclerViewSampleWords.setHasFixedSize(true);
        mRecyclerViewSampleWords.setLayoutManager(new LinearLayoutManager(this));
        sampleWordsList = new ArrayList<SampleWord>();
        sampleWordsList1 = new ArrayList<SampleWord>();
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
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("success")){
                                JSONArray wordsList = jsonObject.getJSONArray("wordsList");
                                for (int i=0; i<wordsList.length(); i++){
                                    JSONObject word = wordsList.getJSONObject(i);
                                    meaning= word.getString("meaning").replace("\\n", "\n");
                                    SampleWord sampleWord = new SampleWord(word.getString("id"),
                                            word.getString("word"),
                                            meaning,
                                            word.getString("category"));
                                    sampleWordsList.add(sampleWord);
                                }
                                InteractWithLocalDatabase.getInstance(getApplicationContext(), null, null, sampleWordsList).execute("insertSampleWords");
                                mAdapterSamplewords = new WordsAdapter(sampleWordsList,SampleWords.this);
                                mRecyclerViewSampleWords.setAdapter(mAdapterSamplewords);
                            }else if(jsonObject.getString("status").equals("failure")){
                                GetFromLocalDatabase.getInstance(getApplicationContext(), null, null, "SampleWords").execute("getSampleWords");
                                mAdapterSamplewords = new WordsAdapter(sampleWordsList, SampleWords.this);
                                mRecyclerViewSampleWords.setAdapter(mAdapterSamplewords);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GetFromLocalDatabase.getInstance(getApplicationContext(), null, null, "SampleWords").execute("getSampleWords");
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Slow network or check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_words_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
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
        GetFromLocalDatabase.getInstance(getApplicationContext(), "%"+newText.toString()+"%", null, "SampleWords").execute("getSearchedSampleWords");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginDropDown:
                Intent intentLogin = new Intent(SampleWords.this, Login.class);
                startActivity(intentLogin);
                break;
            case R.id.registerDropdown:
                Intent intentRegister = new Intent(SampleWords.this, Register.class);
                startActivity(intentRegister);
                break;
            case R.id.aboutUsDropDown:
                Intent intent1 = new Intent(SampleWords.this, AboutUs.class);
                startActivity(intent1);
                break;
            case R.id.privacyPolicyDropDown:
                Intent intent2 = new Intent(SampleWords.this, PrivacyPolicy.class);
                startActivity(intent2);
                break;
            case R.id.refundPolicyDropDown :
                Intent intent3 = new Intent(SampleWords.this, RefundPolicy.class);
                startActivity(intent3);
                break;
            case R.id.termsAndConditionsDropDown:
                Intent intent4 = new Intent(SampleWords.this, TermsAndCondtions.class);
                startActivity(intent4);
                break;
            case R.id.paymentOptionsDropDown :
                Intent intent5 = new Intent(SampleWords.this, PaymentOptions.class);
                startActivity(intent5);
                break;
        }
        return true;
    }
}
