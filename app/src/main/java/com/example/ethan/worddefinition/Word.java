package com.example.ethan.worddefinition;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;
    private String mDef;

    public Word(@NonNull String word, @NonNull String def)
    {
        this.mWord = word;
        this.mDef = def;
    }

    public String getWord()
    {
        return this.mWord;
    }

    public String getDef() { return mDef; }

    @Override
    public String toString()
    {
        return String.format("%s: %n%s", this.getWord(), this.getDef());
    }

}
