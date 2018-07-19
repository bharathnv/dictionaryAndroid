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

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends AppCompatActivity implements View.OnClickListener, PaytmPaymentTransactionCallback {
    private String url;
    private TextInputEditText username, password, confirmPassword, name;
    private String nameS,json;
    private AppCompatButton register;
    private String usernameS, passwordS, confirmPasswordS;
    private JSONObject jsonObject = new JSONObject();
    private JSONObject jsonObject1 = new JSONObject();
    private Toolbar toolbar;
    private static Context context;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Intent intent;
    private static Paytm paytm;
    private static String checksumhash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intent = getIntent();
        context = this;
        url = context.getResources().getString(R.string.url)+"/getPremiumRegistration";
        toolbar = findViewById(R.id.toolbarRegister);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        username = findViewById(R.id.usernameRegister);
        password = findViewById(R.id.passwordRegister);
        confirmPassword = findViewById(R.id.confirmPasswordRegister);
        name = findViewById(R.id.nameRegister);
        register = findViewById(R.id.submitButton);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitButton:
                if(intent.hasExtra("fromAdmin")){
                    RegisterMethod.registerPremiumUser(name.getText().toString(), username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString(), Register.this, url);
                }else {
                    generateCheckSum();
                }
                break;
        }
    }

    private void generateCheckSum() {
        usernameS = username.getText().toString();
        passwordS = password.getText().toString();
        confirmPasswordS = confirmPassword.getText().toString();
        if(usernameS.isEmpty() || usernameS == null || passwordS.isEmpty() || passwordS == null || confirmPasswordS.isEmpty() || confirmPasswordS == null){
            Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        if(!(passwordS.equals(confirmPasswordS))){
            Toast.makeText(context, "Passwords does not match", Toast.LENGTH_LONG).show();
            return;
        }

        if(!usernameS.matches(emailPattern)){
            Toast.makeText(context, "Username is not valid", Toast.LENGTH_LONG).show();
            return;
        }

        //getting the tax amount first.
        String txnAmount = "120";

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://18.188.14.223:8082/paytm/")//Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Processing Request...");
        progressDialog1.show();

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getMID(),
                paytm.getORDER_ID(),
                paytm.getCUST_ID(),
                paytm.getCHANNEL_ID(),
                paytm.getTXN_AMOUNT(),
                paytm.getWEBSITE(),
                paytm.getCALLBACK_URL()+paytm.getORDER_ID(),
                paytm.getINDUSTRY_TYPE_ID()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {

            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {
                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                progressDialog1.dismiss();
                checksumhash = response.body().getChecksumHash();
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                progressDialog1.dismiss();
                Toast.makeText(getApplicationContext(),"FAILURE",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
//        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getORDER_ID());
        paramMap.put("CUST_ID", paytm.getCUST_ID());
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getINDUSTRY_TYPE_ID());
        paramMap.put("CHANNEL_ID", paytm.getCHANNEL_ID());
        paramMap.put("TXN_AMOUNT", paytm.getTXN_AMOUNT());
        paramMap.put("WEBSITE", paytm.getWEBSITE());
        paramMap.put("CALLBACK_URL", paytm.getCALLBACK_URL()+paytm.getORDER_ID());
        paramMap.put("CHECKSUMHASH", checksumHash);


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        RegisterMethod.registerPremiumUser(name.getText().toString(), username.getText().toString(), password.getText().toString(), confirmPassword.getText().toString(), Register.this, url);
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }
}