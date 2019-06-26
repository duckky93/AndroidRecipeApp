package com.android.enclave.androidrecipeapp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recipes")
public class Recipe implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    @ColumnInfo(name = "recipe_content")
    private String recipeContent;

    @ColumnInfo(name = "recipe_image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeContent() {
        return recipeContent;
    }

    public void setRecipeContent(String recipeContent) {
        this.recipeContent = recipeContent;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
