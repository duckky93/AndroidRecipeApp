package com.android.enclave.androidrecipeapp.presenters;

import com.android.enclave.androidrecipeapp.entities.Recipe;

public interface IRecipePresenter {

    void getCategoryResource();

    void getRecipe(int categoryId);

    void deleteRecipe(Recipe recipe, int position);

}
