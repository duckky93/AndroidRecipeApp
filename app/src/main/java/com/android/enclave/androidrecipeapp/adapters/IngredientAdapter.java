package com.android.enclave.androidrecipeapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.enclave.androidrecipeapp.R;
import com.android.enclave.androidrecipeapp.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_NEW = 0;
    private static final int INGREDIENT = 1;

    private Context mContext;
    private List<Ingredient> ingredients;
    private IngredientListener listener;

    public IngredientAdapter(Context mContext) {
        this.mContext = mContext;
        ingredients = new ArrayList<>();
    }

    public void setListener(IngredientListener listener) {
        this.listener = listener;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        notifyDataSetChanged();
    }

    public void updateIngredient(Ingredient ingredient, int position) {
        this.ingredients.set(position, ingredient);
        notifyItemChanged(position);
    }

    public void deleteIngredient(int position) {
        this.ingredients.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == ingredients.size()) {
            return ADD_NEW;
        }
        return INGREDIENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case ADD_NEW:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_add_new, viewGroup, false);
                return new AddNewViewHolder(view);
            case INGREDIENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_ingredient, viewGroup, false);
                return new IngredientViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof IngredientViewHolder) {
            onBindViewHolder((IngredientViewHolder) viewHolder, i);
        } else if (viewHolder instanceof AddNewViewHolder) {
            onBindViewHolder((AddNewViewHolder) viewHolder, i);
        }
    }

    public void onBindViewHolder(@NonNull IngredientViewHolder viewHolder, final int i) {
        final Ingredient ingredient = ingredients.get(i);
        viewHolder.tvIngredientName.setText(ingredient.getName());
        viewHolder.tvIngredientQuantity.setText(ingredient.getQuantity());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onIngredientListenerClickListener(ingredient, i);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onIngredientLongClickListener(ingredient, i);
                }
                return false;
            }
        });
    }

    public void onBindViewHolder(@NonNull AddNewViewHolder viewHolder, int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onAddNewIngredientListener();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size() + 1;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient_name)
        TextView tvIngredientName;

        @BindView(R.id.tv_ingredient_quantity)
        TextView tvIngredientQuantity;

        public IngredientViewHolder(@NonNull View itemView) {
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

    public interface IngredientListener {
        void onAddNewIngredientListener();

        void onIngredientListenerClickListener(Ingredient ingredient, int position);

        void onIngredientLongClickListener(Ingredient ingredient, int position);
    }
}
