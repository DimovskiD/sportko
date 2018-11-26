package com.dimovski.sportko.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.dimovski.sportko.R;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.repository.FirebaseRepository;
import com.dimovski.sportko.utils.DateTimeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventDetailActivity extends BaseActivity implements View.OnClickListener {

    Unbinder unbinder;
    FirebaseRepository repository = new FirebaseRepository();

    @BindView(R.id.event_photo_edit)
    ImageView photo;
    @BindView(R.id.event_title)
    TextView title;
    @BindView(R.id.event_desc)
    TextView description;
    @BindView(R.id.event_type)
    TextView type;
    @BindView(R.id.event_date)
    TextView date;
    @BindView(R.id.attending)
    TextView attending;
    @BindView(R.id.attendEvent)
    Button attendEvent;

    Event event;
    int eventId;
    SharedPreferences sharedPreferences;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        unbinder = ButterKnife.bind(this);


        attendEvent.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        currentUser = sharedPreferences.getString(Constants.EMAIL,"");
    }



    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        this.event = event;
        EventBus.getDefault().removeStickyEvent(event);
        initUi();
    }

    private void initUi() {
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        type.setText(String.format("Type: %s", event.getTypeOfEvent()));
        Glide.with(this).load(event.getImgSrc()).into(photo);
        date.setText(String.format("%s %s", DateTimeUtils.formatDate(event.getScheduled(), this), DateTimeUtils.formatTime(event.getScheduled(), this)));
        int noOfAtendees = event.getAttendees().size();
        attending.setText(String.format("%d / %d", noOfAtendees, event.getMaxAttendees()));

        if (event.getAttendees().indexOf(currentUser)>-1) {
            attendEvent.setBackgroundResource(R.color.gray);
            attendEvent.setText(R.string.attending);
            attendEvent.setTextColor(getResources().getColor(R.color.black));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attendEvent:
                break;
        }
    }
}
