package com.dimovski.sportko.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.Repository;

/**
 * Separate model for the @{@link com.dimovski.sportko.ui.EventDetailActivity} that provides updates to a single event.*/
public class EventDetailViewModel extends ViewModel {

    private Repository repository = Repository.getInstance();

    public LiveData<Event> getEvent(String eventId) {
        return repository.getEvent(eventId);
    }

}
