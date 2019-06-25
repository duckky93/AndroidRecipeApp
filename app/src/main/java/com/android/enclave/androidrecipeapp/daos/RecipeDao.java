package com.android.enclave.androidrecipeapp.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.android.enclave.androidrecipeapp.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * from recipes")
    List<Recipe> getAll();

    @Query("SELECT * from recipes where category_id IN (:categoryIds)")
    List<Recipe> getAll(int[] categoryIds);

    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Update
    void update(Recipe recipe);

}
