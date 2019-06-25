package com.android.enclave.androidrecipeapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.enclave.androidrecipeapp.R;
import com.android.enclave.androidrecipeapp.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NEW_TYPE = 0;
    private static final int RECIPE_TYPE = 1;

    private Context mContext;
    private List<Recipe> recipes;
    private RecipeListener listener;

    public RecipeAdapter(Context mContext) {
        this.mContext = mContext;
        this.recipes = new ArrayList<>();
    }

    public void setListener(RecipeListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == recipes.size()) {
            return NEW_TYPE;
        }
        return RECIPE_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case NEW_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_add_new, viewGroup, false);
                return new AddNewViewHolder(view);
            case RECIPE_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_recipe, viewGroup, false);
                return new RecipeViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RecipeViewHolder) {
            onBindViewHolder((RecipeViewHolder) viewHolder, i);
        } else if (viewHolder instanceof AddNewViewHolder) {
            onBindViewHolder((AddNewViewHolder) viewHolder, i);
        }
    }

    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        Recipe recipe = recipes.get(i);
        if (recipe.getImage() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[1024 * 32];

            Bitmap bm = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length, options);
            recipeViewHolder.ivRecipeBackground.setImageBitmap(bm);
        } else {
            recipeViewHolder.ivRecipeBackground.setImageResource(R.drawable.foodplaceholder);
        }
        recipeViewHolder.tvRecipeName.setText(recipe.getRecipeName());
    }

    public void onBindViewHolder(@NonNull AddNewViewHolder addNewViewHolder, int i) {
        addNewViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickNewRecipeListener();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size() + 1;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_image)
        ImageView ivRecipeBackground;

        @BindView(R.id.tv_recipe_name)
        TextView tvRecipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class AddNewViewHolder extends RecyclerView.ViewHolder {

        public AddNewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface RecipeListener {
        void onRecipeClickListener(Recipe recipe);

        void onClickNewRecipeListener();
    }
}
