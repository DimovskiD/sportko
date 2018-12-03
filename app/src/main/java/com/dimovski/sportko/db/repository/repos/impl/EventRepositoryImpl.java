package com.dimovski.sportko.db.repository.repos.impl;

import android.arch.lifecycle.LiveData;
import android.util.Log;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.repos.firestore.FirebaseEventRepository;
import com.dimovski.sportko.db.repository.repos.EventRepository;
import com.dimovski.sportko.internal.NoInternetConnectionEvent;
import com.dimovski.sportko.utils.NetworkUtils;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    private FirebaseEventRepository firebaseEventRepository = FirebaseEventRepository.getInstance();
    private static EventRepository repository;

    public static EventRepository getInstance() {
        if (repository == null)
            repository = new EventRepositoryImpl();
        return repository;
    }


    @Override
    public LiveData<List<Event>> getUpcomingEvents() {
        return  NetworkUtils.checkInternetConnection(BaseApp.getContext()) ? firebaseEventRepository.getUpcomingEvents() : null; //todo fetch data from local database
    }

    @Override
    public LiveData<List<Event>> getUpcomingEventsForCity(String city) {
        return NetworkUtils.checkInternetConnection(BaseApp.getContext()) ? firebaseEventRepository.getUpcomingEventsForCity(city) : null; //todo fetch data from local database
    }

    @Override
    public LiveData<List<Event>> getMyEvents(@NotNull String email) {
        return NetworkUtils.checkInternetConnection(BaseApp.getContext()) ? firebaseEventRepository.getMyEvents(email) : null; //todo fetch data from local database
    }


    @Override
    public void insert(Event entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseEventRepository.insertEvent(entity);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent()); //todo create event locally, push to server later
        }
    }

    @Override
    public void update(Event entity) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
            firebaseEventRepository.updateEvent(entity);
        } else {
            EventBus.getDefault().post(new NoInternetConnectionEvent());
        }
    }

    @Override
    public void delete(Event entity) {
        //TODO implement
    }

    @Override
    public LiveData<Event> get(String id) {
        if (NetworkUtils.checkInternetConnection(BaseApp.getContext())) {
        return firebaseEventRepository.getEvent(id);
        } else {
            Log.e("REPO", "no connection"); //todo get event from local db
            return null;
        }
    }

    @Override
    public LiveData<List<Event>> getAll() {
        return firebaseEventRepository.getAllEvents();
    }
}
