package com.dimovski.sportko.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.dimovski.sportko.R;
import com.dimovski.sportko.auth.Authentication;
import com.dimovski.sportko.auth.FirebaseAuthentication;
import com.dimovski.sportko.auth.InvalidUserListener;

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

    private void checkUserLoggedIn() {

        if (auth.getCurrentUser()==null)
            startLoginActivity();
        else {
            auth.reloadCurrentUser(this);
        }
    }

    @Override
    public void invalidUser(boolean isInvalid) {
        if (isInvalid)
            startLoginActivity();
        else startListActivity();
    }
}
