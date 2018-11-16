package com.dimovski.sportko.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.dimovski.sportko.R;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.FirebaseRepository;

import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends Activity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        unbinder = ButterKnife.bind(this);

        createEvent.setOnClickListener(this);
        setUpSpinner();
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createEvent:
                createEvent();
                navigateUpTo(new Intent(AddEventActivity.this,ListActivity.class));
                break;
            case R.id.event_photo_create:
                break;
        }
    }

    private void createEvent() {

        Event e = new Event(1,title.getText().toString(),description.getText().toString(), Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),
                1000,100,"Here",""); //TODO replace default values
        repository.insertEvent(e);
    }
}
