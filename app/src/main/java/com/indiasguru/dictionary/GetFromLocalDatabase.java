package com.indiasguru.dictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GetFromLocalDatabase extends AsyncTask<String, Void, List> {
    private Context context;
    private AppDatabase appDatabase;
    private static GetFromLocalDatabase getFromLocalDatabase = null;
    private List genericList = new ArrayList();
    private String searchedSampleWord, searchedPremiumWord;
    private String query, activity;
    private int i;
    private GetFromLocalDatabase(Context context, String searchedSampleWord, String searchedPremiumWord, String activity) {
        this.context = context;
        this.searchedSampleWord = searchedSampleWord;
        this.searchedPremiumWord = searchedPremiumWord;
        this.activity = activity;
    }

    public static GetFromLocalDatabase getInstance(Context context, String searchedSampleWord, String searchedPremiumWord, String activity){
        getFromLocalDatabase = null;
        getFromLocalDatabase = new GetFromLocalDatabase(context,searchedSampleWord, searchedPremiumWord, activity);
        return getFromLocalDatabase;
    }

    @Override
    protected List doInBackground(String... strings) {
        appDatabase = AppDatabase.getAppDatabase(context);
        query = strings[0];
        switch (strings[0]){
            case "getSampleWords":
                genericList = appDatabase.wordsDao().getSampleWordsFromDatabase();
                break;
            case "getSearchedSampleWords":
                genericList = appDatabase.wordsDao().getSearchedQuerySampleWordsFromDatabase(searchedSampleWord);
                break;
            case "getPremiumWords":
                genericList = appDatabase.wordsDao().getPremiumWordsFromDatabase();
                break;
            case "getSearchedPremiumWords":
                genericList = appDatabase.wordsDao().getSearchedQueryPremiumWordsFromDatabase(searchedPremiumWord);
                break;
        }
        return genericList;
    }

    @Override
    protected void onPostExecute(List list) {
        switch (query){
            case "getSampleWords":
                SampleWords.sampleWordsList = list;
                if(SampleWords.sampleWordsList.size() == 0) {
                    SampleWord sampleWord = new SampleWord("20000",
                            "Sorry",
                            "Check your internet connection",
                            "Error");
                    SampleWords.sampleWordsList.add(sampleWord);
                }
                SampleWords.mAdapterSamplewords = new WordsAdapter(SampleWords.sampleWordsList, context);
                SampleWords.mRecyclerViewSampleWords.setAdapter(SampleWords.mAdapterSamplewords);
                break;

            case "getSearchedSampleWords":
                SampleWords.sampleWordsList1.clear();
                SampleWords. sampleWordsList1 = list;
                if(searchedSampleWord == null || searchedSampleWord.isEmpty()){
                    SampleWords.mAdapterSamplewords = new WordsAdapter(SampleWords.sampleWordsList, context);
                }else {
                    if(SampleWords.sampleWordsList1.size() == 0){
                        SampleWord sampleWord = new SampleWord("200000",
                                "Register and subscribe for premium version to get all physical terminologies",
                                "",
                                "Error");
                        SampleWords.sampleWordsList1.add(sampleWord);
                    }
                    SampleWords.mAdapterSamplewords = new WordsAdapter(SampleWords.sampleWordsList1, context);
                }

                SampleWords.mRecyclerViewSampleWords.setAdapter(SampleWords.mAdapterSamplewords);
                break;
            case "getPremiumWords":
                if(activity.equals("premium")){
                    PremiumWords.premiumWordsList = list;
                    if(PremiumWords.premiumWordsList.size() == 0) {
                        PremiumWord premiumWord = new PremiumWord("20000",
                                "Sorry",
                                "Check your internet connection",
                                "Error");
                        PremiumWords.premiumWordsList.add(premiumWord);
                    }
                    PremiumWords.mAdapter = new PremiumWordsAdapter(PremiumWords.premiumWordsList, context);
                    PremiumWords.mRecyclerView.setAdapter(PremiumWords.mAdapter);
                }else {
                    AddEditDelete.premiumWordsList = list;
                    if(AddEditDelete.premiumWordsList.size() == 0) {
                        PremiumWord premiumWord = new PremiumWord("20000",
                                "Sorry",
                                "Check your internet connection",
                                "Error");
                        AddEditDelete.premiumWordsList.add(premiumWord);
                    }
                    AddEditDelete.mAdapterAddEditDelete = new AddEditDeleteAdapter(AddEditDelete.premiumWordsList, context);
                    AddEditDelete.mRecyclerViewAddEditDelete.setAdapter(AddEditDelete.mAdapterAddEditDelete);
                }
                break;
            case "getSearchedPremiumWords":
                if(activity.equals("premium")){
                    PremiumWords.premiumWordsList1.clear();
                    PremiumWords.premiumWordsList1 = list;
                    if(searchedPremiumWord == null || searchedPremiumWord.isEmpty()){
                        PremiumWords.mAdapter = new PremiumWordsAdapter(PremiumWords.premiumWordsList, context);
                        PremiumWords.suggestButton.setVisibility(View.GONE);
                    }else {
                        PremiumWords.mAdapter = new PremiumWordsAdapter(PremiumWords.premiumWordsList1, context);
                        if (PremiumWords.premiumWordsList1.size() == 0){
                            PremiumWords.suggestButton.setVisibility(View.VISIBLE);
                        }
                    }
                    PremiumWords.mRecyclerView.setAdapter(PremiumWords.mAdapter);
                }else {
                    AddEditDelete.premiumWordsList1.clear();
                    AddEditDelete.premiumWordsList1 = list;
                    if(searchedPremiumWord.toString() == null || searchedPremiumWord.isEmpty()){
                        AddEditDelete.mAdapterAddEditDelete = new AddEditDeleteAdapter(AddEditDelete.premiumWordsList, context);
                    }else {
                        AddEditDelete.mAdapterAddEditDelete = new AddEditDeleteAdapter(AddEditDelete.premiumWordsList1, context);
                    }
                    AddEditDelete.mRecyclerViewAddEditDelete.setAdapter(AddEditDelete.mAdapterAddEditDelete);
                }
                break;
        }
    }
}
