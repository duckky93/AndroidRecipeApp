package com.android.enclave.androidrecipeapp.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.android.enclave.androidrecipeapp.entities.Ingredient;

import java.util.List;

@Dao
public interface GredientDao {

    @Query("SELECT * from ingredients")
    List<Ingredient> getAll();

    @Query("SELECT * from ingredients WHERE id = :ingredientId")
    List<Ingredient> getAll(int ingredientId);

    @Query("SELECT * from ingredients WHERE recipe_id = :recipeId")
    List<Ingredient> getAllByRecipeId(long recipeId);

    @Insert
    long insert(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);
}
