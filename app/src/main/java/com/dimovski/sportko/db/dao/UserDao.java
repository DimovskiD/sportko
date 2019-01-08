package com.dimovski.sportko.db.dao;

import android.arch.persistence.room.*;
import com.dimovski.sportko.db.model.User;

import java.util.List;

/**Data Access Object for Users - Used for local @{@link RoomDatabase}*/

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
