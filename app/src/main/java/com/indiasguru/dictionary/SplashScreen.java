package com.indiasguru.dictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int TIME_OUT=1000;
    private static UserObject userObject;
    private Intent intent;
    private GetUserFromLocalDatabase getUserFromLocalDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new GetUserFromLocalDatabase().execute();
            }
        }, TIME_OUT);
    }

    class GetUserFromLocalDatabase extends AsyncTask<Void, Void, UserObject> {
        private  AppDatabase appDatabase;
        private  UserObject user;
        @Override
        protected UserObject doInBackground(Void... voids) {
            appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
            user = appDatabase.wordsDao().getLoggedInUser();
            return user;
        }

        @Override
        protected void onPostExecute(UserObject userObject) {
            SplashScreen.userObject = userObject;
            if(userObject != null){
                if(userObject.isAdmin()){
                    intent = new Intent(SplashScreen.this, AddEditDelete.class);
                }else {
                    intent = new Intent(SplashScreen.this, PremiumWords.class);
                }

                intent.putExtra("id", userObject.getId());
                intent.putExtra("username",userObject.getUsername());
                intent.putExtra("password",userObject.getPassword());
                startActivity(intent);
            }else {
                Intent intent = new Intent(SplashScreen.this, SampleWords.class);
                startActivity(intent);
            }
            finish();
        }
    }
}
