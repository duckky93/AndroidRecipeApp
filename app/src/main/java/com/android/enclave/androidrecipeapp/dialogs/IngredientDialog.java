package com.android.enclave.androidrecipeapp.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.enclave.androidrecipeapp.R;
import com.android.enclave.androidrecipeapp.entities.Ingredient;
import com.android.enclave.androidrecipeapp.entities.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IngredientDialog extends DialogFragment {

    public static final String INGREDIENT = "INGREDIENT";
    public static final String RECIPE = "RECIPE";
    public static final String POSITION = "POSITION";

    private IngredientDialogListener listener;

    @BindView(R.id.edt_ingredient_name)
    EditText edtIngredientName;

    @BindView(R.id.edt_ingredient_quantity)
    EditText edtIngredientQuantity;

    @BindView(R.id.bt_ok)
    Button btOk;

    private Ingredient ingredient;
    private Recipe recipe;
    private int position;
    private Unbinder unbinder;

    public void setListener(IngredientDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ingredient_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        ingredient = (Ingredient) bundle.getSerializable(INGREDIENT);
        recipe = (Recipe) bundle.getSerializable(RECIPE);
        position = bundle.getInt(POSITION);
        if (ingredient != null) {
            edtIngredientName.setText(ingredient.getName());
            edtIngredientQuantity.setText(ingredient.getQuantity());
        }
    }

    @OnClick(R.id.bt_ok)
    public void onOKClicked() {
        String name = edtIngredientName.getText().toString().trim();
        String quantity = edtIngredientQuantity.getText().toString().trim();
        if (!name.isEmpty() && !quantity.isEmpty()) {
            if (listener != null) {
                if (ingredient == null) {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(name);
                    newIngredient.setQuantity(quantity);
                    newIngredient.setRecipeId(recipe.getId());
                    listener.onCreateIngredient(newIngredient);
                } else {
                    ingredient.setName(name);
                    ingredient.setQuantity(quantity);
                    listener.onUpdateIngredient(ingredient, position);
                }
            }
            edtIngredientName.setText("");
            edtIngredientQuantity.setText("");
            dismiss();
        } else {
            Toast.makeText(getActivity(), getString(R.string.empty_message_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public interface IngredientDialogListener {
        void onUpdateIngredient(Ingredient ingredient, int position);

        void onCreateIngredient(Ingredient ingredient);
    }
}
