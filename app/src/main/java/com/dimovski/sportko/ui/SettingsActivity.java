package com.dimovski.sportko.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.FirebaseRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.input_username)
    TextInputEditText username;
    @BindView(R.id.toolbar_settings)
    Toolbar toolbar;
    @BindView(R.id.done)
    ImageButton done;
    @BindView(R.id.locationSwitch)
    SwitchCompat switchCompat;

    private FirebaseRepository repo;

    SharedPreferences sharedPreferences;
    Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        String email = sharedPreferences.getString(Constants.EMAIL,"");
        String user = sharedPreferences.getString(Constants.USER,"");
        if (user.equals("")) username.setText(email);
        else
            username.setText(user);
        done.setOnClickListener(this);
        repo = new FirebaseRepository();

        switchCompat.setChecked(sharedPreferences.getBoolean(Constants.LOCATION,false));

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)) {
                    sharedPreferences.edit().putBoolean(Constants.LOCATION,isChecked).apply();
                }  else {
                    switchCompat.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(Constants.locPermission, Constants.LOCATION_PERMISSION);
                    }
                }


            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                if (validateInput()) {
                    savePreferences();
                    navigateUpTo(new Intent(this, ListActivity.class));
                }
        }
    }

    private boolean validateInput() {
        boolean valid = true;
        if (username.getText()==null || username.getText().toString().equals(""))
        {
            username.setError("This field cannot be empty");
            valid=false;
        }
        return valid;
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER,username.getText().toString());
        editor.apply();
        User u = new User(sharedPreferences.getString(Constants.EMAIL,""));
        u.setUsername(username.getText().toString());
        repo.upSertUser(u);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED) {
//                    FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                startAutoCompleteFragment(location);
//                            }
//                        });
//
//                    }
                    switchCompat.setChecked(true);
                    sharedPreferences.edit().putBoolean(Constants.LOCATION,true).apply();
                } else {
                    Toast.makeText(SettingsActivity.this,R.string.denied_pemission,Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}
