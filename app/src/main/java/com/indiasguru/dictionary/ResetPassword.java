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

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    private String url;
    private TextInputEditText password, confirmPassword, otp;
    private AppCompatButton resetButton;
    private JSONObject jsonObject = new JSONObject();
    private JSONObject jsonObject1 = new JSONObject();
    private String json, email;
    private Intent intent;
    private Toolbar toolbar;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        context = this;
        url = context.getResources().getString(R.string.url)+"/resetPassword";
        intent = getIntent();
        toolbar = findViewById(R.id.toolbarResetPassword);
        toolbar.setTitle("Reset Password");
        setSupportActionBar(toolbar);
        email = intent.getStringExtra("email");
        otp = findViewById(R.id.otp);
        password = findViewById(R.id.passwordReset);
        confirmPassword = findViewById(R.id.confirmPasswordReset);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetButton:
                if(!(password.getText().toString().equals(confirmPassword.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Please enter same passwords", Toast.LENGTH_LONG).show();
                    break;
                }
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        try {
            jsonObject.put("email", email);
            jsonObject.put("otp", otp.getText().toString());
            jsonObject.put("password", password.getText().toString());
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
                            if(jsonResponse.getString("status").equals("success")){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), jsonResponse.getString("message").toString(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ResetPassword.this, Login.class);
                                startActivity(intent);
                                finish();
                            }else if (jsonResponse.getString("status").equals("failure")){
                                Toast.makeText(getApplicationContext(), jsonResponse.getString("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please try after some time", Toast.LENGTH_LONG).show();
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
