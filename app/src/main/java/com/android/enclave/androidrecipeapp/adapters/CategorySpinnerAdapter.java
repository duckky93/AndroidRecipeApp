package com.android.enclave.androidrecipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.enclave.androidrecipeapp.R;
import com.android.enclave.androidrecipeapp.entities.Category;

import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext;
    private List<Category> categories;

    public CategorySpinnerAdapter(Context mContext, List<Category> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return this.categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Category category = categories.get(position);
        View mView = View.inflate(mContext, R.layout.layout_category, null);
        TextView textView = mView.findViewById(R.id.tv_category);
        textView.setText(category.getName());
        ImageView imBackground = mView.findViewById(R.id.iv_category_background);
        switch (category.getId()) {
            case 1:
                imBackground.setImageResource(R.drawable.vegetarian);
                break;
            case 2:
                imBackground.setImageResource(R.drawable.fastfood);
                break;
            case 3:
                imBackground.setImageResource(R.drawable.healthyfood);
                break;
            case 4:
                imBackground.setImageResource(R.drawable.nocook);
                break;
            case 5:
                imBackground.setImageResource(R.drawable.makeahead);
                break;
        }
        return mView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Category category = categories.get(position);
        View mView = View.inflate(mContext, R.layout.layout_category, null);
        TextView textView = mView.findViewById(R.id.tv_category);
        textView.setText(category.getName());
        ImageView imBackground = mView.findViewById(R.id.iv_category_background);
        switch (category.getId()) {
            case 1:
                imBackground.setImageResource(R.drawable.vegetarian);
                break;
            case 2:
                imBackground.setImageResource(R.drawable.fastfood);
                break;
            case 3:
                imBackground.setImageResource(R.drawable.healthyfood);
                break;
            case 4:
                imBackground.setImageResource(R.drawable.nocook);
                break;
            case 5:
                imBackground.setImageResource(R.drawable.makeahead);
                break;
        }
        return mView;
    }
}
