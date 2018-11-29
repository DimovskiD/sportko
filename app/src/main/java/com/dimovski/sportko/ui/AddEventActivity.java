package com.dimovski.sportko.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.dimovski.sportko.R;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.FirebaseRepository;
import com.dimovski.sportko.internal.Mode;
import com.dimovski.sportko.utils.DateTimeUtils;
import com.dimovski.sportko.utils.LocationUtils;
import com.dimovski.sportko.utils.PhotoUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddEventActivity extends BaseActivity implements View.OnClickListener {


    Unbinder unbinder;
    FirebaseRepository repository = new FirebaseRepository();


    @BindView(R.id.input_event_title)
    TextInputEditText title;
    @BindView(R.id.inpuut_description)
    TextInputEditText description;
    @BindView(R.id.createEvent)
    Button createEvent;
    @BindView(R.id.event_photo_create)
    ImageView photo;
    @BindView(R.id.category_spinner)
    Spinner categorySpinner;
    @BindView(R.id.input_date_scheduled)
    TextInputEditText scheduledTime;
    @BindView(R.id.input_max_atendees)
    TextInputEditText maxAtendees;
    @BindView(R.id.input_auto_complete)
    TextInputEditText autoCompletePlaces;


    private Date scheduled;
    private int photoResourceId;
    ArrayAdapter<CharSequence> adapter;
    SharedPreferences sharedPreferences;
    private Event event;
    private Mode mode;
    private double lat;
    private double lon;
    private SingleDateAndTimePickerDialog dateTimeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);
        unbinder = ButterKnife.bind(this);
        createEvent.setOnClickListener(this);
        scheduledTime.setOnClickListener(this);
        maxAtendees.setOnClickListener(this);
        autoCompletePlaces.setOnClickListener(this);
        setUpSpinner();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        mode = Mode.CREATE;


    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        this.event = event;
        EventBus.getDefault().removeStickyEvent(event);
        mode = Mode.UPDATE;
        initUi();
    }

    private void initUi() {
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        categorySpinner.setSelection(adapter.getPosition(event.getTypeOfEvent()));
        Glide.with(this).load(event.getImgSrc()).into(photo);
        autoCompletePlaces.setText(event.getLocationName());
        lat = event.getLat();
        lon = event.getLon();
        scheduledTime.setText(String.format("%s %s", DateTimeUtils.formatDate(event.getScheduled(), this), DateTimeUtils.formatTime(event.getScheduled(), this)));
        maxAtendees.setText(String.format("%d", event.getMaxAttendees()));
        createEvent.setText(R.string.edit_event);
    }

    private void setUpSpinner() {
        adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id1 = categorySpinner.getSelectedItem().toString().toLowerCase();
                Log.i("ADD_EVENT", id1);
                final int resourceId = getResources().getIdentifier(id1, "drawable",
                        getPackageName());
                AddEventActivity.this.photoResourceId = resourceId;
                Glide.with(parent.getContext()).load(resourceId).into(photo);
                photo.setImageDrawable(getResources().getDrawable(resourceId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setSelection(0);

    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createEvent:
                crEditButtonClicked();
                break;
            case R.id.event_photo_create:
                break;
            case R.id.input_date_scheduled:
                showDateTimePicker();
                break;
            case R.id.input_max_atendees:
                break;
            case R.id.input_auto_complete:
                if (sharedPreferences.getBoolean(Constants.LOCATION,false)) {
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)) {
                        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                startAutoCompleteFragment(location);
                            }
                        });
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(Constants.locPermission, Constants.LOCATION_PERMISSION);
                    }
                } } else startAutoCompleteFragment(null);
                break;
        }
    }

    private void startAutoCompleteFragment(Location location) {
        try {
            LatLngBounds bounds = null;
            if (location!=null) {
                bounds = new LatLngBounds(new LatLng(location.getLatitude(),location.getLongitude()),new LatLng(location.getLatitude(),location.getLongitude()));
            }
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();

            Intent intent;
            if (bounds!=null)
                intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter)
                    .setBoundsBias(bounds)
                    .build(this);
            else intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter)
                    .build(this);

            startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    private void crEditButtonClicked() {
        if (validateInput()) {
            crEditEvent();
            navigateUpTo(new Intent(AddEventActivity.this, ListActivity.class));
        }

    }


    private void showDateTimePicker() {
        dateTimeDialog = new SingleDateAndTimePickerDialog.Builder(AddEventActivity.this)
                .bottomSheet()
                .curved()
                .mustBeOnFuture()
                .title(getString(R.string.pick_date_time))
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        scheduled = date;
                        scheduledTime.setText(String.format("%s %s", DateTimeUtils.formatDate(date, AddEventActivity.this), DateTimeUtils.formatTime(date, AddEventActivity.this)));
                    }
                }).build();
        dateTimeDialog.display();
    }

    @Override
    public void onBackPressed() {
        if (dateTimeDialog!=null && dateTimeDialog.isDisplaying())
            dateTimeDialog.close();
        else super.onBackPressed();
    }

    private void crEditEvent() {

        String createdBy = sharedPreferences.getString(Constants.EMAIL, "");

        Resources resources = getResources();
        Uri uri = PhotoUtils.getUriForId(resources, photoResourceId);

        Event e;
        if (mode == Mode.CREATE) {

            String city = LocationUtils.getCityForLocation(this,lat,lon);

            e = new Event(title.getText().toString(), description.getText().toString(), Calendar.getInstance().getTime(), scheduled,
                    lat, lon, autoCompletePlaces.getText().toString(), uri.toString(), Integer.parseInt(maxAtendees.getText().toString()),
                    categorySpinner.getSelectedItem().toString(), createdBy, city);
            repository.insertEvent(e);
        } else {
            event.setTitle(title.getText().toString());
            event.setDescription(description.getText().toString());
            if (scheduled != null)
                event.setScheduled(scheduled);
            event.setTypeOfEvent(categorySpinner.getSelectedItem().toString());
            event.setImgSrc(uri.toString());
            event.setLat(lat);
            event.setLon(lon);
            event.setLocationName(autoCompletePlaces.getText().toString());
            String maxAtt = maxAtendees.getText().toString();
            Log.i("MAX_ATT", maxAtt);
            event.setMaxAttendees(Integer.parseInt(maxAtt));
            repository.updateEvent(event);
        }

    }

    private boolean validateInput() {
        boolean valid = true;
        if (title.getText() == null || title.getText().toString().equals("")) {
            valid = false;
            title.setError(getString(R.string.required_field));
        } else title.setError(null);
        if (description.getText() == null || description.getText().toString().equals("")) {
            valid = false;
            description.setError(getString(R.string.required_field));
        } else description.setError(null);
        if (scheduledTime.getText() == null || scheduledTime.getText().toString().equals("")) {
            valid = false;
            scheduledTime.setError(getString(R.string.required_field));
        } else scheduledTime.setError(null);
        if (maxAtendees.getText() == null || maxAtendees.getText().toString().equals("")) {
            valid = false;
            maxAtendees.setError(getString(R.string.required_field));
        } else if (Integer.parseInt(maxAtendees.getText().toString()) <= 0) {
            valid = false;
            maxAtendees.setError("The number of open positions must be greater than 0");
        } else maxAtendees.setError(null);

        if (autoCompletePlaces.getText() == null || autoCompletePlaces.getText().toString().equals("")) {
            valid = false;
            autoCompletePlaces.setError(getResources().getString(R.string.required_field));
        } else autoCompletePlaces.setError(null);

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place p = PlaceAutocomplete.getPlace(AddEventActivity.this, data);
                    if (p != null) {
                        autoCompletePlaces.setText(p.getName());
                        lat = p.getLatLng().latitude;
                        lon = p.getLatLng().longitude;
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.i("ERROR", status.getStatusMessage());
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED) {
                    FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                startAutoCompleteFragment(location);
                            }
                        });

                    }

                } else {
                    Toast.makeText(AddEventActivity.this,R.string.denied_pemission,Toast.LENGTH_LONG).show();
                    startAutoCompleteFragment(null);
                }
            }
        }
    }
}
