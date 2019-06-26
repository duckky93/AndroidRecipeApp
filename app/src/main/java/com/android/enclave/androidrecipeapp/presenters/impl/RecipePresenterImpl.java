package com.android.enclave.androidrecipeapp.presenters.impl;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.enclave.androidrecipeapp.constant.AppConstants;
import com.android.enclave.androidrecipeapp.database.AppDatabase;
import com.android.enclave.androidrecipeapp.entities.Category;
import com.android.enclave.androidrecipeapp.entities.Recipe;
import com.android.enclave.androidrecipeapp.presenters.BasePresenter;
import com.android.enclave.androidrecipeapp.presenters.IRecipePresenter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipePresenterImpl extends BasePresenter implements IRecipePresenter {

    private RecipeView view;
    private AppDatabase appDatabase;
    private ArrayList<Category> categories;

    public RecipePresenterImpl(Application application, RecipeView view) {
        super(application);
        this.view = view;
        appDatabase = Room.databaseBuilder(getApplication(),
                AppDatabase.class, AppConstants.APP_DATABASE_NAME).allowMainThreadQueries().build();
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
