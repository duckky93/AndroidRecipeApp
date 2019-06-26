package com.android.enclave.androidrecipeapp.presenters.impl;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.enclave.androidrecipeapp.constant.AppConstants;
import com.android.enclave.androidrecipeapp.database.AppDatabase;
import com.android.enclave.androidrecipeapp.entities.Ingredient;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.entities.Step;
import com.android.enclave.androidrecipeapp.presenters.BasePresenter;
import com.android.enclave.androidrecipeapp.presenters.ICreateUpdateRecipePresenter;

import java.util.List;

public class CreateUpdateRecipePresenterImpl extends BasePresenter implements ICreateUpdateRecipePresenter {

    private CreateUpdateRecipeView view;
    private AppDatabase appDatabase;

    public CreateUpdateRecipePresenterImpl(Application application, CreateUpdateRecipeView view) {
        super(application);
        this.view = view;
        appDatabase = Room.databaseBuilder(getApplication(),
                AppDatabase.class, AppConstants.APP_DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Override
    public void createRecipe(Recipe recipe) {
        long id = appDatabase.recipeDao().insert(recipe);
        recipe.setId(id);
        view.onRecipeCreated(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        appDatabase.recipeDao().update(recipe);
        view.onRecipeUpdated(recipe);
    }

    @Override
    public void getIngredients(long recipeId) {
        List<Ingredient> ingredients = appDatabase.gredientDao().getAllByRecipeId(recipeId);
        view.onLoadIngredients(ingredients);
    }

    @Override
    public void createIngredient(Ingredient ingredient) {
        long id = appDatabase.gredientDao().insert(ingredient);
        ingredient.setId(id);
        view.onIngredientCreated(ingredient);
    }

    @Override
    public void updateIngredient(Ingredient ingredient, int position) {
        appDatabase.gredientDao().update(ingredient);
        view.onIngredientUpdated(ingredient, position);
    }

    @Override
    public void deleteIngredient(Ingredient ingredient, int position) {
        appDatabase.gredientDao().delete(ingredient);
        view.onIngredientDeleted(ingredient, position);
    }

    @Override
    public void getSteps(long recipeId) {
        List<Step> steps = appDatabase.stepDao().getAll(recipeId);
        view.onLoadSteps(steps);
    }


    @Override
    public void createStep(Step step) {
        long id = appDatabase.stepDao().insert(step);
        step.setId(id);
        view.onStepCreated(step);
    }

    @Override
    public void updateStep(Step step, int position) {
        appDatabase.stepDao().update(step);
        view.onStepUpdated(step, position);
    }

    @Override
    public void deleteStep(Step step, int position) {
        appDatabase.stepDao().delete(step);
        view.onStepDeleted(step, position);
    }

    public interface CreateUpdateRecipeView{

        void onRecipeCreated(Recipe recipe);

        void onRecipeUpdated(Recipe recipe);

        void onLoadIngredients(List<Ingredient> ingredients);

        void onIngredientCreated(Ingredient ingredient);

        void onIngredientUpdated(Ingredient ingredient, int position);

        void onIngredientDeleted(Ingredient ingredient, int position);

        void onLoadSteps(List<Step> steps);

        void onStepCreated(Step step);

        void onStepUpdated(Step step, int position);

        void onStepDeleted(Step step, int position);
    }
}
