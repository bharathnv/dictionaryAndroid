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
import android.support.v7.widget.Toolbar;
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

public class AddEditDelete extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private String URL_DATA;
    private Toolbar toolbar;
    public static RecyclerView mRecyclerViewAddEditDelete;
    public static RecyclerView.Adapter mAdapterAddEditDelete;
    public static List<PremiumWord> premiumWordsList, premiumWordsList1;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private String meaning;
    private UserObject userObject;
    private Intent intent;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_delete);
        context = this;
        URL_DATA = context.getResources().getString(R.string.url)+"/getWordsList";
        toolbar = findViewById(R.id.toolbarAddEditDelete);
        toolbar.setTitle("Add Edit or Delete");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        userObject = new UserObject(intent.getStringExtra("username").toString(), intent.getStringExtra("password").toString(), intent.getBooleanExtra("isAdmin",false));
        mRecyclerViewAddEditDelete = findViewById(R.id.recyclerViewAddEditDelete);
        mRecyclerViewAddEditDelete.setHasFixedSize(true);
        mRecyclerViewAddEditDelete.setLayoutManager(new LinearLayoutManager(this));
        premiumWordsList = new ArrayList<PremiumWord>();
        premiumWordsList1 = new ArrayList<PremiumWord>();
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
                                mAdapterAddEditDelete = new AddEditDeleteAdapter(premiumWordsList,AddEditDelete.this);
                                mRecyclerViewAddEditDelete.setAdapter(mAdapterAddEditDelete);
                            }else if(jsonObject.getString("status").equals("failure")){
                                GetFromLocalDatabase.getInstance(getApplicationContext(), null, null, "addEditDelete").execute("getPremiumWords");
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
                        GetFromLocalDatabase.getInstance(getApplicationContext(), null, null, "addEditDelete").execute("getPremiumWords");
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Slow network or check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_or_delete, menu);
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
        GetFromLocalDatabase.getInstance(AddEditDelete.this, null, "%"+newText.toString()+"%", "addEditDelete").execute("getSearchedPremiumWords");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.logoutDropDown:
                DeleteFromLocalDatabase.getInstance(getApplicationContext(), userObject, null).execute("deleteUser");
                intent = new Intent(AddEditDelete.this, SampleWords.class);
                startActivity(intent);
                finish();
                break;
            case R.id.addDropDown:
                intent = new Intent(AddEditDelete.this, AddOrEdit.class);
                intent.putExtra("url","/addNewWord");
                intent.putExtra("id",userObject.getId());
                intent.putExtra("username", userObject.getUsername());
                intent.putExtra("password", userObject.getPassword());
                startActivity(intent);
                finish();
                break;
            case R.id.registerUser:
                intent = new Intent(AddEditDelete.this, Register.class);
                intent.putExtra("fromAdmin","fromAdmin");
                startActivity(intent);
                break;
        }
        return true;
    }
}
