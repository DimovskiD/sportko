package com.dimovski.sportko.auth;

import android.app.Activity;

import com.dimovski.sportko.db.model.User;

import java.util.List;

/**Authentication interface that provides signature for required methods that need to be implemented by any authentication provider*/
public interface Authentication {
    void signInWithEmailAndPassword(String user, String password, Activity a);
    void createUserWithEmailAndPassword (String user, String password, Activity a);
    void signOut();
    User getCurrentUser();
    void reloadCurrentUser(InvalidUserListener listener);
}
