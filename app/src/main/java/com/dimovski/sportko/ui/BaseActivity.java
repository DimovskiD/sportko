package com.dimovski.sportko.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.dimovski.sportko.data.Constants;

public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    protected void startLoginActivity(String eventId) {
        Intent startActivity = new Intent(this,LoginActivity.class);
        startActivity.putExtra(Constants.EVENT_ID,eventId);
        startActivity(startActivity);
        this.finish();
    }

    protected void startDetailActivity(String eventId) {
        Intent startActivity = new Intent(this,EventDetailActivity.class);
        startActivity.putExtra(Constants.EVENT_ID,eventId);
        startActivity(startActivity);
        this.finish();
    }


    protected void startSettingsActivity() {
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }

    protected void startCreateNewActivity() {
        Intent i = new Intent(this,AddEventActivity.class);
        startActivity(i);
    }

}
