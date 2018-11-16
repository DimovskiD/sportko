package com.dimovski.sportko.ui;

import android.os.Bundle;
import android.os.Handler;
import com.dimovski.sportko.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends BaseActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        else startListActivity();
    }
}
