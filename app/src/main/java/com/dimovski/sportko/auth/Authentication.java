package com.dimovski.sportko.auth;

import android.app.Activity;

import com.dimovski.sportko.db.model.User;

import java.util.List;

public interface Authentication {


    void signInWithEmailAndPassword(String user, String password, Activity a);
    void createUserWithEmailAndPassword (String user, String password, Activity a);
    void signOut();
    User getCurrentUser();
    void reloadCurrentUser(InvalidUserListener listener);
}
