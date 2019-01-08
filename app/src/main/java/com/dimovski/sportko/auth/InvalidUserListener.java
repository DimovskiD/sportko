package com.dimovski.sportko.auth;

/**Called when validating user token*/
public interface InvalidUserListener {
    void invalidUser(boolean isInvalid);
}
