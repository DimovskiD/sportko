package com.dimovski.sportko.db.repository.repos.local;

import android.arch.lifecycle.LiveData;

import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.repos.UserRepository;

import java.util.List;


/**Single implementation of local event database - Room database in particular*/
//not used, TODO implement if using it
    //currently not storing any users locally, because there is no need at this point, but this serves a demonstration purpose for the Repository pattern
public class LocalUserRepository implements UserRepository {
    @Override
    public String getUsernameForEmail(String email) {
        return null;
    }

    @Override
    public long insert(User entity) {
        return 0;
    }

    @Override
    public int update(User entity) {
        return 0;
    }

    @Override
    public int delete(User entity) {
        return 0;
    }

    @Override
    public LiveData<User> get(String id) {
        return null;
    }

    @Override
    public LiveData<List<User>> getAll() {
        return null;
    }
}
