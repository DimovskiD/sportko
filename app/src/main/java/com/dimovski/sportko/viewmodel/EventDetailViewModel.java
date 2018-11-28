package com.dimovski.sportko.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dimovski.sportko.db.model.Event;
import com.google.firebase.firestore.*;

import java.util.Calendar;
import java.util.List;

public class EventDetailViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Event> eventLiveData;

    public LiveData<Event> getEvent(String eventId) {
        if (eventLiveData == null) {
            eventLiveData = new MutableLiveData<Event>();
            final Query collectionReference = db.collection("events").whereEqualTo("id",eventId);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("LIST",e.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        if (snapshot.toObjects(Event.class).size()>0)
                            eventLiveData.postValue(snapshot.toObjects(Event.class).get(0));
                    } else {
                        Log.d("LIST", "Current data: null");
                    }

                }
            });

        }

        return eventLiveData;
    }

}
