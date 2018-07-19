package com.indiasguru.dictionary;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by user on 3/10/2018.
 */
@Dao
public interface WordsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public List<Long> insertSampleWords(List<SampleWord> words);

    @Query("select * from SampleWord")
    public List<SampleWord> getSampleWordsFromDatabase();

    @Query("select * from SampleWord where word Like :word ")
    public List<SampleWord> getSearchedQuerySampleWordsFromDatabase(String word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertUserObject(UserObject... user);

    @Delete
    public void deleteUSer(UserObject... userObject);

    @Query("select * from UserObject limit 1")
    public UserObject getLoggedInUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public List<Long> insertPremiumWords(List<PremiumWord> words);

    @Query("select * from PremiumWord")
    public List<PremiumWord> getPremiumWordsFromDatabase();

    @Query("select * from PremiumWord where word Like :word ")
    public List<PremiumWord> getSearchedQueryPremiumWordsFromDatabase(String word);

    @Query("select * from PremiumWord where id= :id")
    public PremiumWord getPremiunWordToEdit(String id);

    @Delete
    public void deletePremiumWord(PremiumWord... premiumWord);
}
