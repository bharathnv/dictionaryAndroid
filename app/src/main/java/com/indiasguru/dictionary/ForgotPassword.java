package com.indiasguru.dictionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{
    private TextInputEditText forgotUsername;
    private AppCompatButton submitForgotUsename;
    private String url;
    private Intent intent;
    private String forgotUsernameS, json;
    private JSONObject jsonObject = new JSONObject();
    private JSONObject jsonObject1 = new JSONObject();
    private Toolbar toolbar;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;
        url = context.getResources().getString(R.string.url)+"/forgetPassword";
        toolbar = findViewById(R.id.toolbarForgotPassword);
        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);
        forgotUsername = findViewById(R.id.forgotUsername);
        submitForgotUsename = findViewById(R.id.submitForgotUsernameButton);
        submitForgotUsename.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitForgotUsernameButton:
                submitUsername();
                break;
        }

    }

    private void submitUsername() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();
        forgotUsernameS = forgotUsername.getText().toString();
        try {
            jsonObject.put("email",forgotUsernameS);
            jsonObject1.put("userDetails", jsonObject);
            json = jsonObject1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if(jsonResponse.get("status").equals("success")){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Check your registered mail", Toast.LENGTH_LONG).show();
                            intent = new Intent(ForgotPassword.this,ResetPassword.class);
                            intent.putExtra("email",forgotUsernameS);
                            startActivity(intent);
                            finish();
                        }
                    }catch (Exception ex){
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Slow connection or check you internet connection", Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}