package com.dimovski.sportko.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dimovski.sportko.R;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "START_ACTIVITY";
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.input_email)
    TextInputEditText emailTV;
    @BindView(R.id.input_password)
    TextInputEditText passwordTV;

    private FirebaseAuth authentication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        authentication = FirebaseAuth.getInstance();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = authentication.getCurrentUser();
        if (currentUser!=null)
            startListActivity();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                validateLoginDetails();
                break;
            case R.id.register:
                startRegisterActivity();
                break;
        }
    }



    private void validateLoginDetails() {

        boolean error=false;
        if (emailTV.getText() == null || StringUtils.isEmpty(emailTV.getText().toString())) {
            emailTV.setError("This field is required");
            error=true;
        }
        if (passwordTV.getText() == null || StringUtils.isEmpty(passwordTV.getText().toString())) {
            passwordTV.setError("This field is required");
            error=true;
        }

        if(!error)
            authentication.signInWithEmailAndPassword(emailTV.getText().toString(), passwordTV.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = authentication.getCurrentUser();
                                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                                sharedPreferences.edit().putString(Constants.EMAIL, user.getEmail()).apply();
                                startListActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }
}
