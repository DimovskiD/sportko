package com.dimovski.sportko.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
import com.dimovski.sportko.db.repository.Repository;
import com.dimovski.sportko.internal.FirebaseTopic;
import com.dimovski.sportko.internal.Mode;
import com.dimovski.sportko.internal.NoInternetConnectionEvent;
import com.dimovski.sportko.rest.ApiInterface;
import com.dimovski.sportko.rest.Client;
import com.dimovski.sportko.rest.SendMessageResponse;
import com.dimovski.sportko.utils.*;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


public class AddEventActivity extends BaseActivity implements View.OnClickListener {


    Unbinder unbinder;
    Repository repository = Repository.getInstance();


    @BindView(R.id.input_event_title)
    TextInputEditText title;
    @BindView(R.id.inpuut_description)
    TextInputEditText description;
    @BindView(R.id.createEvent)
    Button createEvent;
    @BindView(R.id.deleteEvent)
    Button deleteEvent;
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
        setListeners();
        setUpSpinner();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        mode = Mode.CREATE;


    }

    private void setListeners() {
        createEvent.setOnClickListener(this);
        scheduledTime.setOnClickListener(this);
        maxAtendees.setOnClickListener(this);
        autoCompletePlaces.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoInternetConnectionEvent noInternetConnectionEvent) {
        Toast.makeText(this,noInternetConnectionEvent.getMessage(),Toast.LENGTH_LONG).show();
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
        deleteEvent.setText(R.string.delete_event);
        deleteEvent.setVisibility(View.VISIBLE);
//        deleteEvent.setClickable(true);
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
                callPlacesApi();
                break;
            case R.id.deleteEvent:
                deleteButtonClicked();
                break;
        }
    }

    private void deleteButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder( this, android.R.style.Widget_Material_ButtonBar_AlertDialog)
                .setTitle(getString(R.string.delete_event))
                .setMessage(R.string.cannot_be_undone_delete)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int res = repository.deleteEvent(event);
                        if (res!=0) {
                            FirebaseUtils.sendFCM(event, Constants.DELETED);
                            navigateUpTo(new Intent(AddEventActivity.this, ListActivity.class));
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.show();
        alertDialog.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        alertDialog.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    private void callPlacesApi() {
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
            Toast.makeText(this,R.string.error,Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this,R.string.google_play_not_available,Toast.LENGTH_SHORT).show();
        }

    }

    private void crEditButtonClicked() {
        if (validateInput()) {
            long res = crEditEvent();
            if (res!=0) {
                if (mode == Mode.UPDATE) FirebaseUtils.sendFCM(event,Constants.EDITED);
                navigateUpTo(new Intent(AddEventActivity.this, ListActivity.class));
            }
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

    private long crEditEvent() {
        long res = 0;
        String createdBy = sharedPreferences.getString(Constants.EMAIL, "");
        Resources resources = getResources();
        Uri uri = PhotoUtils.getUriForId(resources, photoResourceId);
        Event e;
        if (mode == Mode.CREATE) {
            String city = LocationUtils.getCityForLocation(this,lat,lon);
            e = new Event(title.getText().toString(), description.getText().toString(), Calendar.getInstance().getTime(), scheduled,
                    lat, lon, autoCompletePlaces.getText().toString(), uri.toString(), Integer.parseInt(maxAtendees.getText().toString()),
                    categorySpinner.getSelectedItem().toString(), createdBy, city);
            res = repository.insertEvent(e);
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
            res = repository.updateEvent(event);
        }
        return res;

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
            maxAtendees.setError(getString(R.string.number_position_greater_zero));
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
                    if (data!=null) {
                        Place p = PlaceAutocomplete.getPlace(AddEventActivity.this, data);
                        if (p != null) {
                            autoCompletePlaces.setText(p.getName());
                            lat = p.getLatLng().latitude;
                            lon = p.getLatLng().longitude;
                        }
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    if (data!=null) {
                        Status status = PlaceAutocomplete.getStatus(this, data);
                        Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR", status.getStatusMessage());
                    }
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
                                sharedPreferences.edit().putBoolean(Constants.LOCATION,true).apply();
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
