package com.dimovski.sportko.ui;


import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.dimovski.sportko.db.model.User;
import com.dimovski.sportko.db.repository.Repository;
import com.dimovski.sportko.internal.DynamicLinkListner;
import com.dimovski.sportko.ui.dialog.AttendeesDialog;
import com.dimovski.sportko.utils.DateTimeUtils;
import com.dimovski.sportko.utils.FirebaseUtils;
import com.dimovski.sportko.viewmodel.EventDetailViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventDetailActivity extends BaseActivity implements View.OnClickListener, DynamicLinkListner {

    Unbinder unbinder;
    Repository repository = Repository.getInstance();
    EventDetailViewModel viewModel;

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
    @BindView(R.id.where)
    TextView location;
    @BindView(R.id.attendingEvent)
    Button attendingEvent;
    @BindView(R.id.createdBy)
    TextView createdBy;
    @BindView(R.id.whos_going)
    TextView whosGoing;
    @BindView(R.id.share)
    FloatingActionButton share;

    Event event;
    SharedPreferences sharedPreferences;
    String currentUser;
    String eventId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        unbinder = ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(EventDetailViewModel.class);

        attendEvent.setOnClickListener(this);
        attendingEvent.setOnClickListener(this);
        share.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        currentUser = sharedPreferences.getString(Constants.EMAIL,"");

        eventId = getIntent().getStringExtra(Constants.EVENT_ID);

        if (savedInstanceState!=null)
            eventId = savedInstanceState.getString(Constants.EVENT_ID);


        String json = getIntent().getStringExtra(Constants.EVENT);
        if (json!= null && !json.equals("")) {
            event = new GsonBuilder().create().fromJson(json, Event.class);
            eventId =event.getId();
        }

        handleDynamicLink();

    }

    private void handleDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            eventId = deepLink.getQueryParameter("id");
                            if (FirebaseAuth.getInstance().getCurrentUser()!=null)
                                setUpObserver();
                            else {
                                startLoginActivity(eventId);
                                EventDetailActivity.this.finish();
                            }
                            Log.i("DYNAMIC",eventId);
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DYNAMIC", "getDynamicLink:onFailure", e);
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (event!=null) {
            eventId = event.getId();
        }
        if (eventId!=null && !eventId.equals(""))
            setUpObserver();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String json = getIntent().getStringExtra("event");
        event= new Gson().fromJson(json, Event.class);
        eventId = event.getId();
    }

    private void setUpObserver() {
        viewModel.getEvent(eventId).observe(this, new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
                if (event != null) {
                    initUi(event);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putString(Constants.EVENT_ID,eventId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        eventId = savedInstanceState.getString(Constants.EVENT_ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (event!=null) eventId = event.getId();
        viewModel.getEvent(eventId).removeObservers(this);
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
        viewModel.getEvent(event.getId()).observe(this, new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
                if (event != null) {
                    eventId = event.getId();
                    initUi(event);
                }
            }
        });
//        initUi(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(User user) {
        createdBy.setText(String.format("%s %s", getString(R.string.created_by), user.getUsername()));
    }





    private void initUi(Event event) {
        this.event=event;
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        type.setText(String.format("Type: %s", event.getTypeOfEvent()));
        Glide.with(this).load(event.getImgSrc()).into(photo);
        date.setText(String.format("%s %s", DateTimeUtils.formatDate(event.getScheduled(), this), DateTimeUtils.formatTime(event.getScheduled(), this)));
        int noOfAtendees = event.getAttendees().size();
        attending.setText(String.format("%d / %d", noOfAtendees, event.getMaxAttendees()));
        location.setText(event.getLocationName());
        if (event.getAttendees().indexOf(currentUser)>-1) {
            setAttending(true);
        } else setAttending(false);
        repository.getUser(event.getCreatedBy());
        whosGoing.setOnClickListener(this);

    }

    private void setAttending(boolean shouldAttend) {
            if (shouldAttend) {
                attendingEvent.setVisibility(View.VISIBLE);
                attendEvent.setVisibility(View.GONE);
            } else {
                attendingEvent.setVisibility(View.GONE);
                attendEvent.setVisibility(View.VISIBLE);
            }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attendEvent:
                if (event.getAttendees().indexOf(currentUser) < 0) {

                    if (event.getAttendees().size() < event.getMaxAttendees()) {
                        event.addAttendee(currentUser);
                        setAttending(true);
                        repository.updateEvent(event);
                        FirebaseUtils.sendFCM(event, Constants.NEW_ATTENDEE);
                        FirebaseUtils.subscribeToTopic(event.getId(), true);
                    } else {
                        Toast.makeText(this, R.string.cannot_attend_full_event, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.attendingEvent:
                if (event.getAttendees().indexOf(currentUser) >= 0) {
                    event.removeAtendee(currentUser);
                    setAttending(false);
                    repository.updateEvent(event);
                    FirebaseUtils.sendFCM(event, Constants.ATTENDEE_CANCELLED);
                    FirebaseUtils.subscribeToTopic(event.getId(), false);
                }
                break;
            case R.id.whos_going:
                showAttendeesDialog();
                break;
            case R.id.share:
                FirebaseUtils.createDynamicLink(eventId, this);
                showProgressDialog();
                break;
        }
    }



    private void showAttendeesDialog() {
        AttendeesDialog dialog = AttendeesDialog.newInstance(event.getAttendees());
        dialog.show(getSupportFragmentManager(),"DIALOG");
    }

    private void showProgressDialog() {
        if (progressDialog==null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
    }

    @Override
    public void shortDynamicLinkCreated(ShortDynamicLink shortDynamicLink) {
        if (progressDialog!=null && progressDialog.isShowing()) progressDialog.hide();
        Intent sendIntent = new Intent();
        String msg = String.format("%s %s", getString(R.string.check_out_event), shortDynamicLink.getShortLink());
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,getString(R.string.share_event)));
    }
}
