package com.indiasguru.dictionary;

import android.content.Context;
import android.os.AsyncTask;

public class DeleteFromLocalDatabase extends AsyncTask<String, Void, Void> {
    private Context context;
    private AppDatabase appDatabase;
    private static DeleteFromLocalDatabase deleteFromLocalDatabase = null;
    private UserObject user;
    private PremiumWord premiumWord;
    private DeleteFromLocalDatabase(Context context, UserObject user, PremiumWord premiumWord) {
        this.context = context;
        this.user = user;
        this.premiumWord = premiumWord;
    }

    public static DeleteFromLocalDatabase getInstance(Context context, UserObject user, PremiumWord premiumWord){
        deleteFromLocalDatabase = null;
        deleteFromLocalDatabase = new DeleteFromLocalDatabase(context, user, premiumWord);
        return deleteFromLocalDatabase;
    }
    @Override
    protected Void doInBackground(String... strings) {
        appDatabase = AppDatabase.getAppDatabase(context);
        switch (strings[0]){
            case "deleteUser":
                appDatabase.wordsDao().deleteUSer(user);
                break;
            case "deletePremiumWord":
                appDatabase.wordsDao().deletePremiumWord(premiumWord);
                break;
        }
        return null;
    }
}
