package com.shakenbeer.reddittop.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.shakenbeer.reddittop.model.ChildData;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ChildDao {
    @Query("select * from children where page = :page")
    public List<ChildData> getChildren(int page);

    @Insert
    public void addChildren(List<ChildData> children);

    @Query("delete from children where page = :page")
    void cleanPage(int page);
}
