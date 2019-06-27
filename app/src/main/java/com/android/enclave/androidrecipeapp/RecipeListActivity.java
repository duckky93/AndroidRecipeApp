package com.android.enclave.androidrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.enclave.androidrecipeapp.adapters.CategorySpinnerAdapter;
import com.android.enclave.androidrecipeapp.adapters.RecipeAdapter;
import com.android.enclave.androidrecipeapp.constant.AppConstants;
import com.android.enclave.androidrecipeapp.entities.Category;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.fragments.BottomSheetFragment;
import com.android.enclave.androidrecipeapp.presenters.IRecipePresenter;
import com.android.enclave.androidrecipeapp.presenters.impl.RecipePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeListActivity extends BaseActivity implements RecipePresenterImpl.RecipeView {

    private final static int REQUEST_CREATE_CODE = 1;
    private final static int REQUEST_UPDATE_CODE = 2;

    @Override
    int getContentView() {
        return R.layout.activity_recipe_list_activity;
    }

    @BindView(R.id.spn_category)
    Spinner spnCategory;

    @BindView(R.id.rcv_recipe)
    RecyclerView rcvRecipe;

    private IRecipePresenter presenter;
    private RecipeAdapter recipeAdapter;
    private Category category;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private BottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipePresenterImpl(getApplication(), this);
        recipeAdapter = new RecipeAdapter(getApplicationContext());

        bottomSheetFragment = new BottomSheetFragment();
        recipeAdapter.setListener(new RecipeAdapter.RecipeListener() {
            @Override
            public void onRecipeClickListener(Recipe recipe) {
                Intent intent = new Intent(RecipeListActivity.this, CreateUpdateRecipeActivity.class);
                intent.putExtra(AppConstants.CATEGORY, category);
                intent.putExtra(AppConstants.RECIPE, recipe);
                startActivity(intent);
            }

            @Override
            public void onRecipeLongClickListener(final Recipe recipe, final int position) {
                bottomSheetFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                bottomSheetFragment.setListener(new BottomSheetFragment.BottomSheetListener() {
                    @Override
                    public void onBottomSheetClickListener() {
                        presenter.deleteRecipe(recipe, position);
                    }
                });
            }

            @Override
            public void onClickNewRecipeListener() {
                Intent intent = new Intent(RecipeListActivity.this, CreateUpdateRecipeActivity.class);
                intent.putExtra(AppConstants.CATEGORY, category);
                startActivity(intent);
            }
        });

        rcvRecipe.setLayoutManager(new LinearLayoutManager(this));
        rcvRecipe.setAdapter(recipeAdapter);
        presenter.getCategoryResource();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(category!=null){
            presenter.getRecipe(category.getId());
        }
    }

    @Override
    public void onLoadRecipes(List<Recipe> recipes) {
        recipeAdapter.setRecipes(recipes);
    }

    @Override
    public void onRecipeDeleted(Recipe recipe, int position) {
        recipeAdapter.deleteRecipe(recipe, position);
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
                presenter.getRecipe(category.getId());
            }
        });
    }
}
