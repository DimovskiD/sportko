package com.dimovski.sportko.db.repository.repos.firestore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.dimovski.sportko.data.DbConstants;
import com.dimovski.sportko.db.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FirebaseUserRepository {

    private static final String TAG = "FIREBASE_USER_REPO";

    private final FirebaseFirestore firestoreDatabase = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = firestoreDatabase.collection(DbConstants.USERS);

    private static FirebaseUserRepository repository;

    public static FirebaseUserRepository getInstance() {
        if (repository==null)
            repository= new FirebaseUserRepository();
        return repository;
    }

    private FirebaseUserRepository() {}

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

    public String getUsernameForEmail(String email) {
        return ""; //TODO
    }

    public LiveData<List<User>> getAllUsers() {
        final MutableLiveData<List<User>> users;

        users = new MutableLiveData<List<User>>();
        final Query collectionReference = firestoreDatabase.collection(DbConstants.USERS);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( @Nullable QuerySnapshot snapshot,
                                 @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG,e.getMessage());
                    return;
                }

                if (snapshot != null) {
                    users.postValue(snapshot.toObjects(User.class));
                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });
        return users;
    }

}
