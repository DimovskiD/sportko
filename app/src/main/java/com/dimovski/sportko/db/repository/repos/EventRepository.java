package com.dimovski.sportko.db.repository.repos;

import android.arch.lifecycle.LiveData;
import com.dimovski.sportko.db.model.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EventRepository extends Repository<Event>{

    LiveData<List<Event>> getUpcomingEvents();
    LiveData<List<Event>> getUpcomingEventsForCity(String city);
    LiveData<List<Event>> getMyEvents(@NotNull String email);


}
