package com.dimovski.sportko.auth;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.dimovski.sportko.db.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthentication implements Authentication {

    private FirebaseAuth auth;
    private static FirebaseAuthentication authentication;
    private List<OnAuthCompleteListener> signInListeners;
    private List<OnAuthCompleteListener> signUpListeners;

    private FirebaseAuthentication() {
        auth = FirebaseAuth.getInstance();
        signInListeners = new ArrayList<>();
        signUpListeners = new ArrayList<>();

    }

    public static FirebaseAuthentication getInstance() {
        if (authentication==null)
            authentication = new FirebaseAuthentication();
        return authentication;
    }

    @Override
    public void signInWithEmailAndPassword(String user, String password, Activity a) {
        addOnSignInCompleteListener((OnAuthCompleteListener) a);
        auth.signInWithEmailAndPassword(user,password)
                .addOnCompleteListener(a, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        notifySignInListeners(task.isSuccessful());
                    }
                });
    }

    @Override
    public void createUserWithEmailAndPassword(String user, String password, Activity a) {
        addOnSignUpCompleteListener((OnAuthCompleteListener) a);
        auth.createUserWithEmailAndPassword(user,password)
                .addOnCompleteListener(a, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        notifySignUpListeners(task.isSuccessful());
                    }
                });
    }

    @Override
    public void signOut() {
        auth.signOut();
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser u = auth.getCurrentUser();
        if (u==null) return null;
        else {
            User user = new User(u.getEmail(),u.getDisplayName());
            return user;
        }
    }


    public void addOnSignInCompleteListener(OnAuthCompleteListener listener) {
        signInListeners.add(listener);
    }

    public void removeOnSignInCompleteListener(OnAuthCompleteListener listener) {
        signInListeners.remove(listener);
    }

    public void addOnSignUpCompleteListener(OnAuthCompleteListener listener) {
        signUpListeners.add(listener);
    }


    public void removeOnSignUpCompleteListener(OnAuthCompleteListener listener) {
        signUpListeners.remove(listener);
    }

    @Override
    public void reloadCurrentUser(final InvalidUserListener listener) {
        if (auth.getCurrentUser() != null)
        auth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.invalidUser(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException)
                    listener.invalidUser(true);
            }
        });
    }

    private void notifySignInListeners(Boolean result){
        for (OnAuthCompleteListener listener:
             signInListeners) {
            listener.onComplete(result);
        }
        signInListeners.clear();
    }


    private void notifySignUpListeners(Boolean result){
        for (OnAuthCompleteListener listener:
                signUpListeners) {
            listener.onComplete(result);
        }
        signUpListeners.clear();
    }
}
