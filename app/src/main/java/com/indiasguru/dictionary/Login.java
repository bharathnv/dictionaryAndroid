package com.indiasguru.dictionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
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

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText userName, password;
    private AppCompatButton login, register;
    private String username, passwrd;
    private JSONObject jsonObject = new JSONObject();
    private JSONObject jsonObject1 = new JSONObject();
    private String url;
    private String json;
    private Toolbar toolbar;
    private AppDatabase appDatabase;
    private Intent intent;
    private UserObject userObject;
    private AppCompatTextView forgotPasswordText;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        url = context.getResources().getString(R.string.url)+"/loginPremiumUser";
        toolbar = findViewById(R.id.toolbarLogin);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        register = findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgotPasswordText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.login:
                username = userName.getText().toString();
                passwrd = password.getText().toString();
                if(username.isEmpty() || username == null){
                    Toast.makeText(getApplicationContext(), "Username should not be empty", Toast.LENGTH_LONG).show();
                }else if(passwrd.isEmpty() || username == null){
                    Toast.makeText(getApplicationContext(), "Password should not be empty", Toast.LENGTH_LONG).show();
                }else {
                    loginMethod(username, passwrd);
                }
                break;

            case R.id.register:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;

            case R.id.forgotPasswordText:
                Intent intent1 = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent1);
                break;
        }
    }

    private void loginMethod(final String username, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        try {
            jsonObject.put("email", username);
            jsonObject.put("password", password);
            jsonObject1.put("login", jsonObject);
            json = jsonObject1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.getString("status").equals("admin")){
                                Toast.makeText(getApplicationContext(),"Admin logged in successfully", Toast.LENGTH_LONG).show();
                                userObject = new UserObject(username, password, true);
                                intent = new Intent(Login.this, AddEditDelete.class);
                                intent.putExtra("isAdmin",true);
                                intent.putExtra("id",userObject.getId());
                                intent.putExtra("username", userObject.getUsername());
                                intent.putExtra("password", userObject.getPassword());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                InteractWithLocalDatabase.getInstance(getApplicationContext(),null, userObject, null).execute("insertUser");
                                finish();
                            }else if(jsonResponse.getString("status").equals("premium")){
                                Toast.makeText(getApplicationContext(),"User logged in successfully", Toast.LENGTH_LONG).show();
                                userObject = new UserObject(username, password, false);
                                intent = new Intent(Login.this, PremiumWords.class);
                                intent.putExtra("isAdmin",false);
                                intent.putExtra("id",userObject.getId());
                                intent.putExtra("username", userObject.getUsername());
                                intent.putExtra("password", userObject.getPassword());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                InteractWithLocalDatabase.getInstance(getApplicationContext(),null, userObject, null).execute("insertUser");
                                finish();
                            }else if(jsonResponse.getString("status").equals("credentials not found show sample words only")){
                                Toast.makeText(getApplicationContext(),"Username or password is incorrect", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Slow network or check your connection", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}