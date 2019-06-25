package com.android.enclave.androidrecipeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateRecipeActivity extends BaseActivity {

    @Override
    int getContentView() {
        return R.layout.activity_create_recipe;
    }

    @BindView(R.id.ed_recipe_name)
    EditText edRecipeName;

    @BindView(R.id.ed_recipe_description)
    EditText edRecipeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.bt_cancel, R.id.bt_create})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_cancel:
                this.finish();
                break;
            case R.id.bt_create:
                break;
        }
    }
}
