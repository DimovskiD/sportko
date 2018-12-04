package com.dimovski.sportko.db.repository.repos.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.db.LocalDatabase;
import com.dimovski.sportko.db.dao.EventDao;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.repos.EventRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class LocalEventRepositoryImpl implements EventRepository {

    private EventDao eventDao;

    private static LocalEventRepositoryImpl repository;

    public static LocalEventRepositoryImpl getInstance() {
        if (repository == null)
            repository = new LocalEventRepositoryImpl();
        return repository;
    }

    private LocalEventRepositoryImpl() {
        LocalDatabase db = LocalDatabase.getDatabase(BaseApp.getContext().getApplicationContext());
        eventDao = db.eventDao();
    }

    @Override
    public LiveData<List<Event>> getUpcomingEvents() {
        final LiveData<List<Event>> events = new MutableLiveData<>();
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ((MutableLiveData<List<Event>>) events).postValue(eventDao.getUpcoming(Calendar.getInstance().getTime()));
                return null;
            }
        }.execute();
        return events;
    }

    @Override
    public LiveData<List<Event>> getUpcomingEventsForCity(final String city) {

        final LiveData<List<Event>> events = new MutableLiveData<>();
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ((MutableLiveData<List<Event>>) events).postValue(eventDao.getUpcomingEventsForCity(Calendar.getInstance().getTime(),city));
                return null;
            }
        }.execute();
        return events;

    }

    @Override
    public LiveData<List<Event>> getMyEvents(@NotNull final String email) {

        final LiveData<List<Event>> events = new MutableLiveData<>();
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ((MutableLiveData<List<Event>>) events).postValue(eventDao.getEventsForUser(String.format("%%%s%%", email)));
                return null;
            }
        }.execute();
        return events;
    }

    @Override
    public void insert(final Event entity) {
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                eventDao.insert(entity);
                return null;
            }
        }.execute();
    }

    @Override
    public void update(final Event entity) {
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                eventDao.update(entity);
                return null;
            }
        }.execute();
    }

    @Override
    public void delete(final Event entity) {
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                eventDao.delete(entity);
                return null;
            }
        }.execute();
    }

    @Override
    public LiveData<Event> get(final String id) {
        final LiveData<Event> event = new MutableLiveData<>();
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ((MutableLiveData<Event>) event).postValue(eventDao.getEvent(id));
                return null;
            }
        }.execute();
        return event;

    }

    @Override
    public LiveData<List<Event>> getAll() {
        final LiveData<List<Event>> events = new MutableLiveData<>();
        AsyncTask<Void,Void,Void> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                ((MutableLiveData<List<Event>>) events).postValue(eventDao.getAll());
                return null;
            }
        }.execute();
        return events;

    }
}
