package com.indiasguru.dictionary;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by user on 3/10/2018.
 */

@Entity
public class PremiumWord {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String id;

    private String word;
    private String meaning;
    private String category;

    public PremiumWord(String id, String word, String meaning, String category) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
