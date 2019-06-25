package com.android.enclave.androidrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.enclave.androidrecipeapp.adapters.CategorySpinnerAdapter;
import com.android.enclave.androidrecipeapp.adapters.RecipeAdapter;
import com.android.enclave.androidrecipeapp.entities.Category;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.presenters.RecipePresenter;
import com.android.enclave.androidrecipeapp.presenters.impl.RecipePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeListActivity extends BaseActivity implements RecipePresenterImpl.RecipeView {

    private final static int REQUEST_CREATE_CODE = 1;

    @Override
    int getContentView() {
        return R.layout.activity_recipe_list_activity;
    }

    @BindView(R.id.spn_category)
    Spinner spnCategory;

    @BindView(R.id.rcv_recipe)
    RecyclerView rcvRecipe;

    private RecipePresenter presenter;
    private RecipeAdapter recipeAdapter;
    private Category category;
    CategorySpinnerAdapter categorySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipePresenterImpl(getApplication(), this);
        recipeAdapter = new RecipeAdapter(getApplicationContext());
        recipeAdapter.setListener(new RecipeAdapter.RecipeListener() {
            @Override
            public void onRecipeClickListener(Recipe recipe) {

            }

            @Override
            public void onClickNewRecipeListener() {
                Toast.makeText(getApplicationContext(), category.getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CreateRecipeActivity.class);
                startActivityForResult(intent,REQUEST_CREATE_CODE);
            }
        });

        rcvRecipe.setLayoutManager(new LinearLayoutManager(this));
        rcvRecipe.setAdapter(recipeAdapter);
        presenter.getCategoryResource();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CREATE_CODE){
            presenter.getRecipe(category.getId());
        }
    }

    @Override
    public void onLoadRecipes(List<Recipe> recipes) {
        recipeAdapter.setRecipes(recipes);
    }

    @Override
    public void updateCategories(ArrayList<Category> categories) {
        categorySpinnerAdapter = new CategorySpinnerAdapter(this, categories);
        spnCategory.setAdapter(categorySpinnerAdapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (Category) categorySpinnerAdapter.getItem(i);
                presenter.getRecipe(category.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
