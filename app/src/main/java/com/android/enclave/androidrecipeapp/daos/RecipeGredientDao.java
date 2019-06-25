package com.android.enclave.androidrecipeapp.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.android.enclave.androidrecipeapp.entities.RecipeGredient;

import java.util.List;

@Dao
public interface RecipeGredientDao {

    @Query("SELECT * from recipe_gredient WHERE recipe_id = :recipeId")
    List<RecipeGredient> getAll(int recipeId);

    @Insert
    void insert(RecipeGredient recipeGredient);

    @Delete
    void delete(RecipeGredient recipeGredient);

    @Update
    void update(RecipeGredient recipeGredient);

}
