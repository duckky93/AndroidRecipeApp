package com.android.enclave.androidrecipeapp.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.android.enclave.androidrecipeapp.entities.Gredient;

import java.util.List;

@Dao
public interface GredientDao {

    @Query("SELECT * from gredients")
    List<Gredient> getAll();

    @Query("SELECT * from gredients WHERE id IN (:gredientIds)")
    List<Gredient> getAll(int[] gredientIds);

    @Insert
    void insert(Gredient gredient);

    @Delete
    void delete(Gredient gredient);

    @Update
    void update(Gredient gredient);
}
