package com.dimovski.sportko.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dimovski.sportko.R;
import com.dimovski.sportko.auth.Authentication;
import com.dimovski.sportko.auth.FirebaseAuthentication;
import com.dimovski.sportko.auth.OnAuthCompleteListener;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.utils.StringUtils;


/**Activity that handles the login flow*/
public class LoginActivity extends BaseActivity implements View.OnClickListener, OnAuthCompleteListener {

    private static final String TAG = "START_ACTIVITY";
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.input_email)
    TextInputEditText emailTV;
    @BindView(R.id.input_password)
    TextInputEditText passwordTV;

    private String eventId;
    private Authentication authentication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove status bar
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        authentication = FirebaseAuthentication.getInstance();
        setOnClickListeners();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras()!=null) {
            eventId = getIntent().getStringExtra(Constants.EVENT_ID);
        }
        User currentUser = authentication.getCurrentUser();
        if (currentUser!=null)
            startListActivity();
    }


    private void setOnClickListeners() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
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

    /**Validates the fields*/
    private void validateLoginDetails() {
        boolean error=false;
        if (emailTV.getText() == null || StringUtils.isEmpty(emailTV.getText().toString())) {
            emailTV.setError(getString(R.string.required_field));
            error=true;
        }
        if (passwordTV.getText() == null || StringUtils.isEmpty(passwordTV.getText().toString())) {
            passwordTV.setError(getString(R.string.required_field));
            error=true;
        }

        if(!error)
            authentication.signInWithEmailAndPassword(emailTV.getText().toString(), passwordTV.getText().toString(), this);
    }

    /**Implementation of @{@link OnAuthCompleteListener}, which is called when @{@link Authentication} has finished authenticating the user details*/
    @Override
    public void onComplete(boolean result) {
        if (result) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            User user = authentication.getCurrentUser();
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
            sharedPreferences.edit().putString(Constants.EMAIL, user.getEmail()).apply();
            if (eventId==null || eventId.equals(""))
                startListActivity();
            else startDetailActivity(eventId);
        } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
