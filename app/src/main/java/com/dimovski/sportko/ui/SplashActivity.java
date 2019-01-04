package com.dimovski.sportko.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.dimovski.sportko.R;
import com.dimovski.sportko.auth.Authentication;
import com.dimovski.sportko.auth.FirebaseAuthentication;
import com.dimovski.sportko.auth.InvalidUserListener;

/** Splash activity shown before the app is started*/
public class SplashActivity extends BaseActivity implements InvalidUserListener {

    Authentication auth = FirebaseAuthentication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove status bar
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserLoggedIn();
            }
        },1000);

    }

    /**If there is a logged in user, validate the token
     * If there is no logged in user, start login activity*/
    private void checkUserLoggedIn() {

        if (auth.getCurrentUser()==null)
            startLoginActivity();
        else {
            auth.reloadCurrentUser(this);
        }
    }

    /**Implementation of @{@link InvalidUserListener}
     * @param isInvalid - is false if the token validation was valid, true if the token is invalid
     * Start proper activity in accordance to the param*/
    @Override
    public void invalidUser(boolean isInvalid) {
        if (isInvalid)
            startLoginActivity();
        else startListActivity();
    }
}
