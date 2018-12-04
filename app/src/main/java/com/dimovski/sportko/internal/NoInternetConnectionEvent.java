package com.dimovski.sportko.internal;


/**
 * Dummy class used by @{@link org.greenrobot.eventbus.EventBus} in order to indicate there is no Internet connection
 */
public class NoInternetConnectionEvent {

    private String message;

    public NoInternetConnectionEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}