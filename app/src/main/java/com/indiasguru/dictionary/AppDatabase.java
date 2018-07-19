package com.indiasguru.dictionary;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by user on 3/10/2018.
 */

@Database(entities = {SampleWord.class, UserObject.class, PremiumWord.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract WordsDao wordsDao();

    public static AppDatabase getAppDatabase(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user_database")
//                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static void destroyDatabase(){
        instance = null;
    }
}
