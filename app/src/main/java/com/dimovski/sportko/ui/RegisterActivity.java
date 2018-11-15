package com.dimovski.sportko.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private FirebaseAuth authentication;
    private Unbinder unbinder;

    @BindView(R.id.back_to_login)
    Button login;
    @BindView(R.id.signUp)
    Button signup;
    @BindView(R.id.input_email_register)
    TextInputEditText email;
    @BindView(R.id.input_password_register)
    TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        authentication = FirebaseAuth.getInstance();
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
            email.setError("This field is required");
            error=true;
        }
        if (password.getText() == null || StringUtils.isEmpty(password.getText().toString())) {
            password.setError("This field is required");
            error=true;
        }

        if(!error)
            authentication.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
            addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CREATE_ACC", "createUserWithEmail:success");
                        FirebaseUser user = authentication.getCurrentUser();
//                    updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("CREATE_ACC", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Account not created. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
