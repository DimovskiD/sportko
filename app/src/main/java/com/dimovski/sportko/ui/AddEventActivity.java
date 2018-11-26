package com.dimovski.sportko.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import com.dimovski.sportko.utils.DateTimeUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.Calendar;
import java.util.Date;


public class AddEventActivity extends BaseActivity implements View.OnClickListener {

    enum Mode {UPDATE, CREATE}

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


    private Date scheduled;
    private int photoResourceId;
    ArrayAdapter<CharSequence> adapter;
    SharedPreferences sharedPreferences;
    private Event event;
    private Mode mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);
        unbinder = ButterKnife.bind(this);
        createEvent.setOnClickListener(this);
        scheduledTime.setOnClickListener(this);
        maxAtendees.setOnClickListener(this);
        setUpSpinner();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
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
        scheduledTime.setText(String.format("%s %s", DateTimeUtils.formatDate(event.getScheduled(), this), DateTimeUtils.formatTime(event.getScheduled(), this)));
        maxAtendees.setText(String.format("%d", event.getMaxAttendees()));
        createEvent.setText(R.string.edit_event);
    }

    private void setUpSpinner() {
       adapter= ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id1 = categorySpinner.getSelectedItem().toString().toLowerCase();
                Log.i("ADD_EVENT",id1);
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
        switch (v.getId()){
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
        }
    }

    private void crEditButtonClicked() {
        if (validateInput()) {
            crEditEvent();
            navigateUpTo(new Intent(AddEventActivity.this, ListActivity.class));
        }

    }


    private void showDateTimePicker() {
        new SingleDateAndTimePickerDialog.Builder(AddEventActivity.this)
                .bottomSheet()
                .curved()
                .mustBeOnFuture()
                .title(getString(R.string.pick_date_time))
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        scheduled=date;
                        scheduledTime.setText(String.format("%s %s", DateTimeUtils.formatDate(date, AddEventActivity.this), DateTimeUtils.formatTime(date, AddEventActivity.this)));
                    }
                }).display();
    }

    private void crEditEvent() {



        String createdBy = sharedPreferences.getString(Constants.EMAIL,"");

        Resources resources = getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(photoResourceId))
                .appendPath(resources.getResourceTypeName(photoResourceId))
                .appendPath(resources.getResourceEntryName(photoResourceId))
                .build();

        Event e;
        if (mode==Mode.CREATE) {
            e = new Event(title.getText().toString(), description.getText().toString(), Calendar.getInstance().getTime(), scheduled,
                    1000, 100, "Here", uri.toString(), Integer.parseInt(maxAtendees.getText().toString()),
                    categorySpinner.getSelectedItem().toString(), createdBy); //TODO replace default values
            repository.insertEvent(e);
        }
        else {
            event.setTitle(title.getText().toString());
            event.setDescription(description.getText().toString());
            if (scheduled!=null)
                event.setScheduled(scheduled);
            event.setTypeOfEvent(categorySpinner.getSelectedItem().toString());
            event.setImgSrc(uri.toString());
            String maxAtt = maxAtendees.getText().toString();
            Log.i("MAX_ATT",maxAtt);
            event.setMaxAttendees(Integer.parseInt(maxAtt));
            repository.updateEvent(event);
        }

    }

    private boolean validateInput() {
        boolean valid =true;
        if (title.getText()==null || title.getText().toString().equals("")) {
            valid=false;
            title.setError(getString(R.string.required_field));
        }
        else title.setError(null);
        if (description.getText()==null || description.getText().toString().equals("")) {
            valid=false;
            description.setError(getString(R.string.required_field));
        } else description.setError(null);
        if (scheduledTime.getText()==null || scheduledTime.getText().toString().equals("")) {
            valid=false;
            scheduledTime.setError(getString(R.string.required_field));
        } else scheduledTime.setError(null);
        if (maxAtendees.getText()==null || maxAtendees.getText().toString().equals("")) {
            valid=false;
            maxAtendees.setError(getString(R.string.required_field));
        }
        else if  (Integer.parseInt(maxAtendees.getText().toString())<=0)  {
            valid=false;
            maxAtendees.setError("The number of open positions must be greater than 0");
        }
        else maxAtendees.setError(null);
        return valid;
    }
}
