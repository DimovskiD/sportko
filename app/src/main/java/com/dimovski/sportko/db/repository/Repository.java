package com.dimovski.sportko.db.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.repos.EventRepository;
import com.dimovski.sportko.db.repository.repos.UserRepository;
import com.dimovski.sportko.db.repository.repos.impl.EventRepositoryImpl;
import com.dimovski.sportko.db.repository.repos.impl.UserRepositoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Repository {


    private MutableLiveData<List<Event>> events;
    private MutableLiveData<List<Event>> myEvents;

    private static Repository repository;
    private EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private UserRepository userRepository = UserRepositoryImpl.getInstance();

    public static Repository getInstance() {
        if (repository == null)
            repository = new Repository();
        return repository;
    }

    private Repository() {
        events = new MutableLiveData<>();
        myEvents = new MutableLiveData<>();
    }

    public LiveData<List<Event>> getUpcomingEvents() {
        LiveData<List<Event>> e = eventRepository.getUpcomingEvents();
        if (e != null)
            events.postValue(e.getValue());
        return e;
    }

    public LiveData<List<Event>> getUpcomingEventsForCity(String city) {
        LiveData<List<Event>> e = eventRepository.getUpcomingEventsForCity(city);
        if (e != null)
            events.postValue(e.getValue());
        return e;
    }

    public LiveData<List<Event>> getMyEvents(@NotNull String email) {
        LiveData<List<Event>> e = eventRepository.getMyEvents(email);
        if (e != null)
            myEvents.postValue(e.getValue());
        return e;
    }


    public void insertEvent(Event event) {
        eventRepository.insert(event);
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }


    public LiveData<Event> getEvent(String eventId) {
        return eventRepository.get(eventId);
    }

    public void updateEvent(Event event) {
        eventRepository.update(event);
    }

    public void upSertUser(User user) {
        userRepository.insert(user);
    }

    public void getUser(String email) {
        userRepository.get(email);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
