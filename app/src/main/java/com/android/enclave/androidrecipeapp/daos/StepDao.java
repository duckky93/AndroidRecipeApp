package com.android.enclave.androidrecipeapp.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.android.enclave.androidrecipeapp.entities.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * from step WHERE recipe_id = :recipeId")
    List<Step> getAll(int recipeId);

    @Insert
    void insert(Step step);

    @Delete
    void delete(Step step);

    @Update
    void update(Step step);

}
