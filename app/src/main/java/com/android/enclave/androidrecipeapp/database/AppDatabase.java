package com.android.enclave.androidrecipeapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android.enclave.androidrecipeapp.daos.GredientDao;
import com.android.enclave.androidrecipeapp.daos.RecipeDao;
import com.android.enclave.androidrecipeapp.daos.StepDao;
import com.android.enclave.androidrecipeapp.entities.Ingredient;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.entities.Step;

@Database(entities = {Recipe.class, Step.class, Ingredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    public abstract GredientDao gredientDao();

    public abstract StepDao stepDao();

}
