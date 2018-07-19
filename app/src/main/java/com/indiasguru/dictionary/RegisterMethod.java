package com.indiasguru.dictionary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class RegisterMethod {
    private static JSONObject jsonObject1 = new JSONObject();
    private static JSONObject jsonObject = new JSONObject();
    private static String json;
    public static boolean status = false;


    protected static void registerPremiumUser(String nameS, String usernameS, String passwordS, String confirmPasswordS, final Context context, String url) {
        final ProgressDialog progressDialog1 = new ProgressDialog(context);
        progressDialog1.setMessage("Processing Request...");
        progressDialog1.show();

        try {
            jsonObject1.put("name", nameS);
            jsonObject1.put("userName",usernameS);
            jsonObject1.put("password", passwordS);
            jsonObject1.put("email", usernameS);
            jsonObject.put("userDetails", jsonObject1);
            json = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        try {
                            JSONObject object =new JSONObject(response);
                            if(object.getString("status").equals("success")){
                                Toast.makeText(context,"User Registered successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, Login.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                                return;
                            }else if(object.getString("status").equals("failure")){
                                Toast.makeText(context,"Internal server error. Try after some time.",Toast.LENGTH_SHORT).show();
                                ((Activity)context).finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                ((Activity)context).finish();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    protected static void checkTransactionStatus(String MID, String ORDER_ID, String CHECKSUM, final Context context, final String name, final String username, final String password, final String confirmPassword, final String url){
        String urlCheck = "https://securegw-stage.paytm.in/merchant-status/getTxnStatus?JsonData={\"MID\":\""+MID+"\",\"ORDERID\":\""+ORDER_ID+"\",\"CHECKSUMHASH\":\""+CHECKSUM+"\"}";
        final ProgressDialog progressDialog1 = new ProgressDialog(context);
        progressDialog1.setMessage("Processing Request...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseFromPaytmServer = new JSONObject(response);
                            if(responseFromPaytmServer.getString("STATUS") == "TXN_SUCCESS"){
                                status = true;
                                RegisterMethod.registerPremiumUser(name, username, password, confirmPassword, context, url);
                            }else {
                                status = false;
                            }
                        }catch (Exception ex){
                            status = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status = false;
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}