package com.dimovski.sportko.db.repository.repos.impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.R;
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.repos.firestore.FirebaseUserRepository;
import com.dimovski.sportko.db.repository.repos.UserRepository;
import com.dimovski.sportko.internal.NoInternetConnectionEvent;
import com.dimovski.sportko.utils.NetworkUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**Implementation of user repository. Checks for logical conditions and calls the correct method to fetch data from online database or from local database*/
public class UserRepositoryImpl implements UserRepository {

    private FirebaseUserRepository firebaseUserRepository = FirebaseUserRepository.getInstance();
    private static UserRepository repository;

    public static UserRepository getInstance() {
        if (repository == null)
            repository = new UserRepositoryImpl();
        return repository;
    }

    @Override
    public long insert(User entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseUserRepository.upSertUser(entity);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent(BaseApp.getContext().getResources().getString(R.string.cant_register_offline)));
        }
        return 0;
    }

    @Override
    public int update(User entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseUserRepository.upSertUser(entity);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent(BaseApp.getContext().getResources().getString(R.string.no_internet)));
        }
        return 0;
    }

    @Override
    public int delete(User entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {

        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent(BaseApp.getContext().getResources().getString(R.string.no_internet)));
        }
        return 0;
    }

    @Override
    public LiveData<User> get(String id) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseUserRepository.getUser(id);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent(BaseApp.getContext().getResources().getString(R.string.no_internet)));
        }
        return new MutableLiveData<>(); //returns empty live data - the requested result is posted via EventBus
    }

    @Override
    public LiveData<List<User>> getAll() {

        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            return firebaseUserRepository.getAllUsers();
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent(BaseApp.getContext().getResources().getString(R.string.no_internet)));
            return new MutableLiveData<>();
        }
    }

    @Override
    public String getUsernameForEmail(String email) {
        return  firebaseUserRepository.getUsernameForEmail(email);
    }
}
