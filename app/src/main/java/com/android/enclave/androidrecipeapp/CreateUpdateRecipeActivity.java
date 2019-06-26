package com.android.enclave.androidrecipeapp;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.enclave.androidrecipeapp.adapters.IngredientAdapter;
import com.android.enclave.androidrecipeapp.adapters.StepAdapter;
import com.android.enclave.androidrecipeapp.constant.AppConstants;
import com.android.enclave.androidrecipeapp.dialogs.IngredientDialog;
import com.android.enclave.androidrecipeapp.dialogs.StepDialog;
import com.android.enclave.androidrecipeapp.entities.Category;
import com.android.enclave.androidrecipeapp.entities.Ingredient;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.entities.Step;
import com.android.enclave.androidrecipeapp.fragments.BottomSheetFragment;
import com.android.enclave.androidrecipeapp.presenters.ICreateUpdateRecipePresenter;
import com.android.enclave.androidrecipeapp.presenters.impl.CreateUpdateRecipePresenterImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateUpdateRecipeActivity extends BaseActivity implements IngredientAdapter.IngredientListener,
        IngredientDialog.IngredientDialogListener, CreateUpdateRecipePresenterImpl.CreateUpdateRecipeView,
        StepAdapter.StepListener, StepDialog.StepDialogListener {

    private static final int GALLERY_REQUEST_CODE = 0;

    @Override
    int getContentView() {
        return R.layout.activity_create_recipe;
    }

    @BindView(R.id.iv_recipe_image)
    ImageView ivRecipeImage;

    @BindView(R.id.ed_recipe_name)
    EditText edRecipeName;

    @BindView(R.id.ed_recipe_description)
    EditText edRecipeDescription;

    @BindView(R.id.rcv_ingredient)
    RecyclerView rcvIngredient;

    @BindView(R.id.rcv_step)
    RecyclerView rcvStep;

    @BindView(R.id.step_container)
    ConstraintLayout stepContainer;

    @BindView(R.id.ingredient_container)
    ConstraintLayout ingredientContainer;

    @BindView(R.id.bt_create)
    Button btnCreateUpdate;

    private Category category;

    private Recipe recipe;

    private ICreateUpdateRecipePresenter presenter;
    private BottomSheetFragment bottomSheetFragment;

    private IngredientAdapter ingredientAdapter;
    private IngredientDialog ingredientDialog;
    private StepAdapter stepAdapter;
    private StepDialog stepDialog;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CreateUpdateRecipePresenterImpl(getApplication(), this);
        ingredientAdapter = new IngredientAdapter(getApplicationContext());
        ingredientAdapter.setListener(this);
        stepAdapter = new StepAdapter(getApplicationContext());
        stepAdapter.setListener(this);
        ingredientDialog = new IngredientDialog();
        ingredientDialog.setListener(this);
        stepDialog = new StepDialog();
        stepDialog.setListener(this);
        bottomSheetFragment = new BottomSheetFragment();

        if (getIntent().hasExtra(AppConstants.CATEGORY)) {
            category = (Category) getIntent().getSerializableExtra(AppConstants.CATEGORY);
        }
        if (getIntent().hasExtra(AppConstants.RECIPE)) {
            recipe = (Recipe) getIntent().getSerializableExtra(AppConstants.RECIPE);
            if (recipe.getImage() != null) {
                imageBytes = recipe.getImage();
                Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivRecipeImage.setImageBitmap(bmp);
            }
            edRecipeName.setText(recipe.getRecipeName());
            edRecipeDescription.setText(recipe.getRecipeContent());
            presenter.getIngredients(recipe.getId());
            presenter.getSteps(recipe.getId());
        }

        stepContainer.setVisibility(recipe != null ? View.VISIBLE : View.GONE);
        ingredientContainer.setVisibility(recipe != null ? View.VISIBLE : View.GONE);
        btnCreateUpdate.setText(getString(recipe != null ? R.string.update : R.string.create));
        rcvIngredient.setLayoutManager(new LinearLayoutManager(this));
        rcvIngredient.setAdapter(ingredientAdapter);
        rcvStep.setLayoutManager(new LinearLayoutManager(this));
        rcvStep.setAdapter(stepAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImage = data.getData();
            ivRecipeImage.setImageURI(selectedImage);
            try {
                Bitmap bitmap =
                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
                bitmap.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @OnClick({R.id.iv_recipe_image, R.id.bt_cancel, R.id.bt_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_recipe_image:
                pickFromGallery();
                break;
            case R.id.bt_cancel:
                this.finish();
                break;
            case R.id.bt_create:
                String recipeName = edRecipeName.getText().toString().trim();
                String recipeDescription = edRecipeDescription.getText().toString().trim();
                if (recipeName == null || recipeDescription == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_message_error), Toast.LENGTH_LONG).show();
                }
                if (recipe == null) {
                    Recipe newRecipe = new Recipe();
                    newRecipe.setImage(imageBytes);
                    newRecipe.setCategoryId(category.getId());
                    newRecipe.setRecipeName(recipeName);
                    newRecipe.setRecipeContent(recipeDescription);
                    presenter.createRecipe(newRecipe);
                } else {
                    recipe.setImage(imageBytes);
                    recipe.setRecipeName(recipeName);
                    recipe.setRecipeContent(recipeDescription);
                    presenter.updateRecipe(recipe);
                }
                break;
        }
    }

    @Override
    public void onAddNewIngredientListener() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IngredientDialog.INGREDIENT, null);
        bundle.putSerializable(IngredientDialog.RECIPE, recipe);
        bundle.putInt(IngredientDialog.POSITION, 0);
        ingredientDialog.setArguments(bundle);
        ingredientDialog.show(getFragmentManager(), ingredientDialog.getTag());
    }

    @Override
    public void onIngredientListenerClickListener(Ingredient ingredient, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IngredientDialog.INGREDIENT, ingredient);
        bundle.putSerializable(IngredientDialog.RECIPE, recipe);
        bundle.putInt(IngredientDialog.POSITION, position);
        ingredientDialog.setArguments(bundle);
        ingredientDialog.show(getFragmentManager(), ingredientDialog.getTag());
    }


    @Override
    public void onIngredientLongClickListener(final Ingredient ingredient, final int position) {
        bottomSheetFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
        bottomSheetFragment.setListener(new BottomSheetFragment.BottomSheetListener() {
            @Override
            public void onBottomSheetClickListener() {
                presenter.deleteIngredient(ingredient, position);
            }
        });
    }

    @Override
    public void onUpdateIngredient(Ingredient ingredient, int position) {
        presenter.updateIngredient(ingredient, position);
    }

    @Override
    public void onCreateIngredient(Ingredient ingredient) {
        presenter.createIngredient(ingredient);
    }

    @Override
    public void onUpdateStep(Step step, int position) {
        presenter.updateStep(step, position);
    }

    @Override
    public void onCreateStep(Step step) {
        presenter.createStep(step);
    }

    @Override
    public void onRecipeCreated(Recipe recipe) {
        this.recipe = recipe;
        stepContainer.setVisibility(recipe != null ? View.VISIBLE : View.GONE);
        ingredientContainer.setVisibility(recipe != null ? View.VISIBLE : View.GONE);
        btnCreateUpdate.setText(getString(recipe != null ? R.string.update : R.string.create));
    }

    @Override
    public void onRecipeUpdated(Recipe recipe) {
        this.recipe = recipe;
        this.finish();
    }

    @Override
    public void onLoadIngredients(List<Ingredient> ingredients) {
        ingredientAdapter.setIngredients(ingredients);
    }

    @Override
    public void onIngredientCreated(Ingredient ingredient) {
        ingredientAdapter.addIngredient(ingredient);
    }

    @Override
    public void onIngredientUpdated(Ingredient ingredient, int position) {
        ingredientAdapter.updateIngredient(ingredient, position);
    }

    @Override
    public void onIngredientDeleted(Ingredient ingredient, int position) {
        ingredientAdapter.deleteIngredient(position);
    }

    @Override
    public void onLoadSteps(List<Step> steps) {
        stepAdapter.setSteps(steps);
    }

    @Override
    public void onStepCreated(Step step) {
        stepAdapter.addStep(step);
    }

    @Override
    public void onStepUpdated(Step step, int position) {
        stepAdapter.updateStep(step, position);
    }

    @Override
    public void onStepDeleted(Step step, int position) {
        stepAdapter.deleteStep(position);
    }

    @Override
    public void onAddNewStepListener() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(StepDialog.STEP, null);
        bundle.putSerializable(StepDialog.RECIPE, recipe);
        bundle.putInt(StepDialog.POSITION, 0);
        stepDialog.setArguments(bundle);
        stepDialog.show(getFragmentManager(), ingredientDialog.getTag());
    }

    @Override
    public void onStepListenerClickListener(Step step, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(StepDialog.STEP, step);
        bundle.putSerializable(StepDialog.RECIPE, recipe);
        bundle.putInt(StepDialog.POSITION, position);
        stepDialog.setArguments(bundle);
        stepDialog.show(getFragmentManager(), ingredientDialog.getTag());
    }

    @Override
    public void onStepLongClickListener(final Step step, final int position) {
        bottomSheetFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
        bottomSheetFragment.setListener(new BottomSheetFragment.BottomSheetListener() {
            @Override
            public void onBottomSheetClickListener() {
                presenter.deleteStep(step, position);
            }
        });
    }
}
