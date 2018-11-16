package com.dimovski.sportko.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.database.Observable;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dimovski.sportko.adapter.EventAdapter;
import com.dimovski.sportko.db.model.Event;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Event>> events;
    private MutableLiveData<List<Event>> myEvents;


    public LiveData<List<Event>> getEvents() {
        if (events == null) {
            events = new MutableLiveData<List<Event>>();
            final CollectionReference collectionReference = db.collection("events");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("LIST",e.getMessage());
                        return;
                    }

                    if (snapshot != null) {
                        events.postValue(snapshot.toObjects(Event.class));
                    } else {
                        Log.d("LIST", "Current data: null");
                    }

                }
            });

        }

        return events;
    }

    public LiveData<List<Event>> getMyEvents(@NotNull String email) {
        if (myEvents == null) {
            myEvents = new MutableLiveData<List<Event>>();
            final Query query = db.collection("events").whereArrayContains("usersAttending",email);
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("LIST",e.getMessage());
                        return;
                    }

                    if (snapshot != null) {
                        myEvents.postValue(snapshot.toObjects(Event.class));
                    } else {
                        Log.d("LIST", "Current data: null");
                    }

                }
            });

        }

        return myEvents;
    }



}
