package com.dimovski.sportko.adapter;

import com.dimovski.sportko.db.model.Event;

/**Called when an event from the @{@link com.dimovski.sportko.ui.ListActivity} has been clicked*/
public interface ItemClickHandler {
    void eventClicked(Event event);
}
