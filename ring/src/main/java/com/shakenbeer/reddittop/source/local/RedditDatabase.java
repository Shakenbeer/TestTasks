package com.shakenbeer.reddittop.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.shakenbeer.reddittop.model.ChildData;

@Database(entities = {ChildData.class}, version = 1)
public abstract class RedditDatabase extends RoomDatabase {
    public abstract ChildDao childDao();
}
