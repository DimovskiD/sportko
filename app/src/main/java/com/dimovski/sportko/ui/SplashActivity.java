package com.dimovski.sportko.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import com.dimovski.sportko.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SplashActivity extends BaseActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

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
            auth.getCurrentUser().reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        startLoginActivity();
                    }
                }
            });
            startListActivity();
        }
    }
}
