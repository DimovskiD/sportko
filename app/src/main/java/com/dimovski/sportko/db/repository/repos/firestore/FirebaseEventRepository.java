package com.dimovski.sportko.db.repository.repos.firestore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dimovski.sportko.data.DbConstants;
import com.dimovski.sportko.db.model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class FirebaseEventRepository {

    private static final String TAG = "FIREBASE_EVENT_REPO";

    private final FirebaseFirestore firestoreDatabase = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = firestoreDatabase.collection(DbConstants.USERS);
    private final CollectionReference eventsRef = firestoreDatabase.collection(DbConstants.EVENTS);

    private static FirebaseEventRepository repository;

    public static FirebaseEventRepository getInstance() {
        if (repository==null)
            repository= new FirebaseEventRepository();
        return repository;
    }

    private FirebaseEventRepository() {
    }


    public void updateEvent(Event event) {
        eventsRef.document(event.getId()).set(event);
    }

    public String insertEvent(Event event) {

        DocumentReference e = firestoreDatabase.collection(DbConstants.EVENTS).document();
        event.setId(e.getId());
        eventsRef.document(e.getId())
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG,"event added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding event", e);
                    }
                });
        return e.getId();
    }



    public LiveData<List<Event>> getUpcomingEvents() {
            final MutableLiveData<List<Event>> events;

            events = new MutableLiveData<List<Event>>();
//            final Query collectionReference = city!=null ? firestoreDatabase.collection("events").whereGreaterThan("scheduled",Calendar.getInstance().getTime())
//                   .whereEqualTo("city",city).orderBy("scheduled") : firestoreDatabase.collection("events").whereGreaterThan("scheduled",Calendar.getInstance().getTime())
//                   .orderBy("scheduled");
            final Query collectionReference = firestoreDatabase.collection(DbConstants.EVENTS).whereGreaterThan(DbConstants.SCHEDULED,Calendar.getInstance().getTime())
                    .orderBy(DbConstants.SCHEDULED);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e(TAG,e.getMessage());
                        return;
                    }

                    if (snapshot != null) {
                        events.postValue(snapshot.toObjects(Event.class));
                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });


        return events;
    }

    public LiveData<List<Event>> getUpcomingEventsForCity(String city) {
        final MutableLiveData<List<Event>> events;

            events = new MutableLiveData<List<Event>>();
            final Query collectionReference = firestoreDatabase.collection(DbConstants.EVENTS).whereGreaterThan(DbConstants.SCHEDULED,Calendar.getInstance().getTime())
                    .whereEqualTo(DbConstants.CITY,city).orderBy(DbConstants.SCHEDULED);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e(TAG,e.getMessage());
                        return;
                    }

                    if (snapshot != null) {
                        events.postValue(snapshot.toObjects(Event.class));
                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });


        return events;
    }

    public LiveData<List<Event>> getMyEvents(@NotNull String email) {
            final MutableLiveData<List<Event>> myEvents = new MutableLiveData<List<Event>>();
            final Query query = firestoreDatabase.collection(DbConstants.EVENTS).whereArrayContains(DbConstants.ATTENDEES,email);
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e(TAG,e.getMessage());
                        return;
                    }

                    if (snapshot != null) {
                        myEvents.postValue(snapshot.toObjects(Event.class));
                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });

        return myEvents;
    }

    public LiveData<Event> getEvent(String eventId) {
            final MutableLiveData<Event> eventLiveData = new MutableLiveData<Event>();
            final Query collectionReference = firestoreDatabase.collection(DbConstants.EVENTS).whereEqualTo(DbConstants.ID,eventId);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( @Nullable QuerySnapshot snapshot,
                                     @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e(TAG,e.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        if (snapshot.toObjects(Event.class).size()>0)
                            eventLiveData.postValue(snapshot.toObjects(Event.class).get(0));
                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });

        return eventLiveData;
    }

    public LiveData<List<Event>> getAllEvents() {
        final MutableLiveData<List<Event>> events;

        events = new MutableLiveData<List<Event>>();
        final Query collectionReference = firestoreDatabase.collection(DbConstants.EVENTS).orderBy(DbConstants.SCHEDULED);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( @Nullable QuerySnapshot snapshot,
                                 @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG,e.getMessage());
                    return;
                }

                if (snapshot != null) {
                    events.postValue(snapshot.toObjects(Event.class));
                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });


        return events;
    }

    public void deleteEvent(Event entity) {
        eventsRef.document(entity.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG,"event deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting event", e);
                    }
                });
    }
}
