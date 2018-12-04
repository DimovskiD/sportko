package com.dimovski.sportko.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.dimovski.sportko.db.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail (String email);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername (String username);
}
