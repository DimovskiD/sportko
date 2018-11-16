package com.dimovski.sportko.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove status bar
    }

    protected void startListActivity() {
        Intent i = new Intent(this,ListActivity.class);
        startActivity(i);
        this.finish();
    }

    protected void startRegisterActivity() {
        Intent startActivity = new Intent(this,RegisterActivity.class);
        startActivity(startActivity);
        this.finish();
    }

    protected void startLoginActivity() {
        Intent startActivity = new Intent(this,LoginActivity.class);
        startActivity(startActivity);
        this.finish();
    }

}
