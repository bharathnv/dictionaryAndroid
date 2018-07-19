package com.indiasguru.dictionary;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class InteractWithLocalDatabase extends AsyncTask<String, String, String> {
    private Context context;
    private AppDatabase appDatabase;
    private List<PremiumWord> premiumWordList;
    private UserObject user;
    private List<SampleWord> sampleWords;
    private static InteractWithLocalDatabase interactWithLocalDatabase = null;
    private InteractWithLocalDatabase(Context context, List<PremiumWord> premiumWordList, UserObject user, List<SampleWord> sampleWords) {
        this.context = context;
        this.premiumWordList = premiumWordList;
        this.user = user;
        this.sampleWords = sampleWords;
    }

    public static InteractWithLocalDatabase getInstance(Context context, List<PremiumWord> premiumWordList, UserObject user, List<SampleWord> sampleWords){
        interactWithLocalDatabase = null;
        interactWithLocalDatabase = new InteractWithLocalDatabase(context, premiumWordList, user, sampleWords);
        return interactWithLocalDatabase;
    }

    @Override
    protected String doInBackground(String... strings) {
        appDatabase = AppDatabase.getAppDatabase(context);
        switch (strings[0]){
            case "insertPremiumWords":
                appDatabase.wordsDao().insertPremiumWords(premiumWordList);
                break;
            case "insertUser":
                appDatabase.wordsDao().insertUserObject(user);
                break;
            case "insertSampleWords":
                appDatabase.wordsDao().insertSampleWords(sampleWords);
                break;
        }
        return null;
    }
}
