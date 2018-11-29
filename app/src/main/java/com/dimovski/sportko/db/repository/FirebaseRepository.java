package com.dimovski.sportko.db.repository;

import android.support.annotation.NonNull;
import android.util.Log;
import com.dimovski.sportko.data.DbConstants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.greenrobot.eventbus.EventBus;


public class FirebaseRepository {

    private static final String TAG = "FIREBASE_REPO";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection(DbConstants.USERS);
    private final CollectionReference eventsRef = db.collection(DbConstants.EVENTS);

    public void getEvent(int eventId) {
        eventsRef.document(Integer.toString(eventId)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    EventBus.getDefault().post(document.toObject(Event.class));
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        });
    }

    public void updateEvent(Event event) {
        eventsRef.document(event.getId()).set(event);
    }

    public void upSertUser(User user) {

        usersRef.document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG,"User added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public void insertEvent(Event event) {

        DocumentReference e = db.collection(DbConstants.EVENTS).document();
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
    }

    public void getUser(String email) {
        usersRef.document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    EventBus.getDefault().post(document.toObject(User.class));
                } else {
                    Log.d(TAG, "No such user");
                }
            }
        });
    }

}
