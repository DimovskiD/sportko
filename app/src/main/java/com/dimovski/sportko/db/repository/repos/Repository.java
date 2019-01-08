package com.dimovski.sportko.db.repository.repos;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**Signature interface for basic CRUD operations that all implementations of the repository should implement*/
public interface Repository<T> {

    long insert(T entity);
    int update (T entity);
    int delete(T entity);
    LiveData<T> get(String id);
    LiveData<List<T>> getAll();

}
