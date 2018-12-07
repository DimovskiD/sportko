package com.dimovski.sportko.db.repository.repos;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface Repository<T> {

    long insert(T entity);
    int update (T entity);
    int delete(T entity);
    LiveData<T> get(String id);
    LiveData<List<T>> getAll();

}
