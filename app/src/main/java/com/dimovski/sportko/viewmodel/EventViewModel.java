package com.dimovski.sportko.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventViewModel extends ViewModel {

    private Repository repository = Repository.getInstance();

    public LiveData<List<Event>> getUpcomingEvents() {
        return repository.getUpcomingEvents();
    }

    public LiveData<List<Event>> getUpcomingEventsForCity(String city) {
        return repository.getUpcomingEventsForCity(city);
    }

    public LiveData<List<Event>> getMyEvents(@NotNull String email) {
        return repository.getMyEvents(email);
    }



}
