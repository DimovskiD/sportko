package com.dimovski.sportko.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dimovski.sportko.adapter.EventAdapter;
import com.dimovski.sportko.db.model.Event;
import com.google.firebase.firestore.*;

import java.util.List;

public class EventViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Event>> events;


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




}
