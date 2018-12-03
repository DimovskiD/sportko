package com.dimovski.sportko.db.repository.repos;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface Repository<T> {

    void insert(T entity);
    void update (T entity);
    void delete(T entity);
    LiveData<T> get(String id);
    LiveData<List<T>> getAll();

}
