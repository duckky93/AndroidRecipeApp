package com.android.enclave.androidrecipeapp.presenters.impl;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.android.enclave.androidrecipeapp.constant.AppConstants;
import com.android.enclave.androidrecipeapp.database.AppDatabase;
import com.android.enclave.androidrecipeapp.entities.Category;
import com.android.enclave.androidrecipeapp.entities.Ingredient;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.entities.Step;
import com.android.enclave.androidrecipeapp.presenters.BasePresenter;
import com.android.enclave.androidrecipeapp.presenters.IRecipePresenter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class RecipePresenterImpl extends BasePresenter implements IRecipePresenter {

    private RecipeView view;
    private AppDatabase appDatabase;
    private ArrayList<Category> categories;

    public RecipePresenterImpl(Application application, RecipeView view) {
        super(application);
        this.view = view;
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        //pre-populated recipe
                        Recipe recipe = new Recipe();
                        recipe.setRecipeName("Crispy Falafel");
                        recipe.setRecipeContent("These falafels are golden brown and crispy on the outside. The insides are tender, delicious, and full of fresh herbs.\n" +
                                "They’re baked instead of fried, so they contain significantly less fat than fried falafel. And your house won’t smell like fried food for days. Winning!\n" +
                                "Once your chickpeas are sufficiently soaked, the falafel mixture comes together in no time. If you have someone to help shape the patties, they’ll come together even faster.\n" +
                                "These falafels are gluten free and vegan, so they’re a great party appetizer.\n" +
                                "These falafels freeze well, so they’re a fantastic protein-rich option to keep on hand for future salads and pita sandwiches.\n" +
                                "On that note, this recipe is easily doubled! See recipe notes.");
                        recipe.setCategoryId(1);
                        recipe.setId(appDatabase.recipeDao().insert(recipe));

                        Ingredient ingredient = new Ingredient();
                        ingredient.setRecipeId(recipe.getId());

                        ingredient.setName("tahini");
                        ingredient.setQuantity("1/4 cup");
                        appDatabase.gredientDao().insert(ingredient);

                        ingredient.setName("lemon");
                        ingredient.setQuantity("1 small");
                        appDatabase.gredientDao().insert(ingredient);

                        ingredient.setName("white miso");
                        ingredient.setQuantity("1 tablespoon");
                        appDatabase.gredientDao().insert(ingredient);

                        ingredient.setName("cloves, pressed");
                        ingredient.setQuantity("2 garlic");
                        appDatabase.gredientDao().insert(ingredient);

                        ingredient.setName("fresh dill, chopped");
                        ingredient.setQuantity("2 tablespoons");
                        appDatabase.gredientDao().insert(ingredient);

                        ingredient.setName("fresh parsley, chopped");
                        ingredient.setQuantity("1 tablespoon");
                        appDatabase.gredientDao().insert(ingredient);

                        ingredient.setName("water");
                        ingredient.setQuantity("1/3 cup");
                        appDatabase.gredientDao().insert(ingredient);

                        Step step = new Step();
                        step.setRecipeId(recipe.getId());

                        step.setStepName("Step 1");
                        step.setStepDescription("With an oven rack in the middle position, preheat oven to 375 degrees Fahrenheit. Pour ¼ cup of the olive oil into a large, rimmed baking sheet and turn until the pan is evenly coated.");
                        appDatabase.stepDao().insert(step);

                        step.setStepName("Step 2");
                        step.setStepDescription("In a food processor, combine the soaked and drained chickpeas, onion, parsley, cilantro, garlic, salt, pepper, cumin, cinnamon, and the remaining 1 tablespoon of olive oil. Process until smooth, about 1 minute.");
                        appDatabase.stepDao().insert(step);

                        step.setStepName("Step 3");
                        step.setStepDescription("Using your hands, scoop out about 2 tablespoons of the mixture at a time. Shape the falafel into small patties, about 2 inches wide and ½ inch thick. Place each falafel on your oiled pan.");
                        appDatabase.stepDao().insert(step);

                        step.setStepName("Step 4");
                        step.setStepDescription("Bake for 25 to 30 minutes, carefully flipping the falafels halfway through baking, until the falafels are deeply golden on both sides. These falafels keep well in the refrigerator for up to 4 days, or in the freezer for several months.");
                        appDatabase.stepDao().insert(step);

                        getRecipe(1);
                    }
                });
            }
        };

        appDatabase = Room.databaseBuilder(getApplication(),
                AppDatabase.class, AppConstants.APP_DATABASE_NAME).allowMainThreadQueries()
                .addCallback(rdc)
                .build();
    }

    public void getCategoryResource(){
        categories = getCategoriesFromXMLFile();
        view.updateCategories(categories);
    }

    private ArrayList<Category> getCategoriesFromXMLFile() {
        ArrayList<Category> categories = new ArrayList<>();

        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getApplication().getAssets().open("recipetypes.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            categories = processCategories(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    private ArrayList<Category> processCategories(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Category> categories = new ArrayList<>();
        int eventType = parser.getEventType();
        Category category = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if ("recipetype".equals(eltName)) {
                        category = new Category();
                        categories.add(category);
                        category.setId(Integer.parseInt(parser.getAttributeValue(null, "id")));
                    } else if (category != null) {
                        if ("name".equals(eltName)) {
                            category.setName(parser.nextText());
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

        return categories;
    }

    @Override
    public void getRecipe(int categoryId) {
        List<Recipe> recipes = appDatabase.recipeDao().getAll(categoryId);
        view.onLoadRecipes(recipes);
    }

    @Override
    public void deleteRecipe(Recipe recipe, int position) {
        appDatabase.recipeDao().delete(recipe);
        view.onRecipeDeleted(recipe, position);
    }

    public interface RecipeView {
        void onLoadRecipes(List<Recipe> recipes);

        void onRecipeDeleted(Recipe recipe, int position);

        void updateCategories(ArrayList<Category> categories);
    }
}
