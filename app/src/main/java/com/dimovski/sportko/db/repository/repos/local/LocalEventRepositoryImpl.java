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
import java.util.concurrent.ExecutionException;

/**Single implementation of local event database - Room database in particular*/

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
    public long insert(final Event entity) {
        AsyncTask<Void,Void,Long> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                return eventDao.insert(entity);
            }
        };
        long result = 0;
        try {
            result = getEvents.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(final Event entity) {
        AsyncTask<Void,Void,Integer> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                return eventDao.update(entity);
            }
        };
        int result = 0;
        try {
            result = getEvents.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(final Event entity) {
        AsyncTask<Void,Void,Integer> getEvents = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                return eventDao.delete(entity);
            }
        };
        int result = 0;
        try {
             result = getEvents.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
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
