package com.dimovski.sportko.db.repository.repos.impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.repos.firestore.FirebaseUserRepository;
import com.dimovski.sportko.db.repository.repos.UserRepository;
import com.dimovski.sportko.internal.NoInternetConnectionEvent;
import com.dimovski.sportko.utils.NetworkUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private FirebaseUserRepository firebaseUserRepository = FirebaseUserRepository.getInstance();
    private static UserRepository repository;

    public static UserRepository getInstance() {
        if (repository == null)
            repository = new UserRepositoryImpl();
        return repository;
    }



    @Override
    public void insert(User entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseUserRepository.upSertUser(entity);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent()); //TODO update user locally, push to sever later
        }
    }

    @Override
    public void update(User entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseUserRepository.upSertUser(entity);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent()); //TODO update user locally, push to sever later
        }
    }

    @Override
    public void delete(User entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {

        } else {
            Log.e("REPO", "no connection"); //todo get user from local db
        }
        //TODO implement
    }

    @Override
    public LiveData<User> get(String id) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseUserRepository.getUser(id);
        } else {
            Log.e("REPO", "no connection"); //todo get user from local db and send via EventBus
        }
        return new MutableLiveData<>(); //returns empty live data - the requested result is posted via EventBus
    }

    @Override
    public LiveData<List<User>> getAll() {

        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            return firebaseUserRepository.getAllUsers();
        } else {
            Log.e("REPO", "no connection"); //todo get user from local db
            return null;
        }
    }
}
