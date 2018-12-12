package com.example.ethan.worddefinition;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities ={Word.class}, version= 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase
{

    public abstract WordDao wordDao();
    private static WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object.
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db)
        {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>
    {

        private final WordDao mDao;
        String[] words = {"bears", "beets", "battlestar galactica"};
        String[] defs = {"Various usually omnivorous mammals of the family Ursidae", "A biennial Eurasian plant grown as a crop plant", "An American science fiction media franchise created by Glen A. Larson."};

        PopulateDbAsync(WordRoomDatabase db)
        {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params)
        {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();

            for (int i = 0; i <= words.length - 1; i++)
            {
                Word word = new Word(words[i], defs[i]);
                mDao.insert(word);
            }
            return null;
        }
    }
}