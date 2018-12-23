package com.dimovski.sportko.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.auth.Authentication;
import com.dimovski.sportko.auth.FirebaseAuthentication;
import com.dimovski.sportko.auth.OnAuthCompleteListener;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.Repository;
import com.dimovski.sportko.utils.StringUtils;


public class RegisterActivity extends BaseActivity implements View.OnClickListener, OnAuthCompleteListener {

    private Authentication authentication;
    private Repository repo = Repository.getInstance();

    private Unbinder unbinder;


    @BindView(R.id.back_to_login)
    Button login;
    @BindView(R.id.signUp)
    Button signup;
    @BindView(R.id.input_email_register)
    TextInputEditText email;
    @BindView(R.id.input_password_register)
    TextInputEditText password;
    @BindView(R.id.input_username)
    TextInputEditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove status bar
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        authentication = FirebaseAuthentication.getInstance();
        signup.setOnClickListener(this);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_login:
                Intent i = new Intent(this,LoginActivity.class);
                navigateUpTo(i);
                break;
            case R.id.signUp:
                signUpUser();
                break;
        }
    }

    private void signUpUser() {
        boolean error=false;
        if (email.getText() == null || StringUtils.isEmpty(password.getText().toString())) {
            email.setError(getString(R.string.required_field));
            error=true;
        }
        if (password.getText() == null || StringUtils.isEmpty(password.getText().toString())) {
            password.setError(getString(R.string.required_field));
            error=true;
        }
        if (username.getText() == null || StringUtils.isEmpty(username.getText().toString())){
            username.setError(getString(R.string.required_field));
            error=true;
        }

        if(!error) {
            authentication.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString(), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onComplete(boolean result) {
        if (result) {
            // Sign in success, update UI with the signed-in user's information
            Log.d("CREATE_ACC", "createUserWithEmail:success");
            User user = authentication.getCurrentUser();
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.EMAIL, user.getEmail());
            editor.putString(Constants.USER,username.getText().toString());
            editor.apply();
            repo.insertUser(new User(user.getEmail(),username.getText().toString()));
            startListActivity();
        } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(RegisterActivity.this, R.string.failed_create_user,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
