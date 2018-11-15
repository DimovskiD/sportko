package com.dimovski.sportko.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        unbinder = ButterKnife.bind(this);

        createEvent.setOnClickListener(this);
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
        }
    }

    private void createEvent() {

        Event e = new Event(1,title.getText().toString(),description.getText().toString(), Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),1000,100,"Here");
        repository.insertEvent(e);
    }
}
