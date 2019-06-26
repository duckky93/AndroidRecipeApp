package com.android.enclave.androidrecipeapp.presenters;

import com.android.enclave.androidrecipeapp.entities.Ingredient;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.entities.Step;

public interface ICreateUpdateRecipePresenter {

    void createRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);

    void getIngredients(long recipeId);

    void createIngredient(Ingredient ingredient);

    void updateIngredient(Ingredient ingredient, int position);

    void deleteIngredient(Ingredient ingredient, int position);

    void getSteps(long recipeId);

    void createStep(Step step);

    void updateStep(Step step, int position);

    void deleteStep(Step step, int position);

}
