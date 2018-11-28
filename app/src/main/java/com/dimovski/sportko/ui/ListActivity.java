package com.dimovski.sportko.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.adapter.EventAdapter;
import com.dimovski.sportko.adapter.ItemClickHandler;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.internal.DrawerItem;
import com.dimovski.sportko.utils.NetworkUtils;
import com.dimovski.sportko.viewmodel.EventViewModel;
import com.google.firebase.auth.FirebaseAuth;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ItemClickHandler {

    Unbinder unbinder;
    EventViewModel viewModel;
    EventAdapter adapter;
    Observer<List<Event>> eventObserver;
    SharedPreferences sharedPreferences;
    String currentUser;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rvList)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.emptyRv)
    TextView emptyRv;

    private DrawerItem selectedItem = DrawerItem.LIST_EVENTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        unbinder = ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        navigationView.getMenu().getItem(0).setChecked(true);
        fab.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(EventViewModel.class);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (selectedItem == DrawerItem.LIST_EVENTS){
                    viewModel.getUpcomingEvents().observe(ListActivity.this, eventObserver);
                    viewModel.getMyEvents(currentUser).removeObservers(ListActivity.this);
                }
                else {
                    viewModel.getMyEvents(currentUser).observe(ListActivity.this,eventObserver);
                    viewModel.getUpcomingEvents().removeObservers(ListActivity.this);
                }
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        currentUser = sharedPreferences.getString(Constants.EMAIL,"");


        if (!NetworkUtils.checkInternetConnection(this)){
            showSnackbar();
        }

    }

    private void showSnackbar() {
        Snackbar s  = Snackbar.make(refreshLayout,R.string.no_internet,BaseTransientBottomBar.LENGTH_LONG);
        View v = s.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        s.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        eventObserver = new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (progressBar.getVisibility()==View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    if (events.size()<1) {
                        emptyRv.setVisibility(View.VISIBLE);
                    } else emptyRv.setVisibility(View.GONE);
                }
                if (adapter!=null)
                    adapter.setEvents(events);
                else {
                    adapter = new EventAdapter(events,ListActivity.this);
                    recyclerView.setAdapter(adapter);
                }

                if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
            }
        };

        if (selectedItem == DrawerItem.LIST_EVENTS) {
            toolbar.setTitle(R.string.upcoming_events);
            viewModel.getUpcomingEvents().observe(this, eventObserver);
        }
        else if (selectedItem == DrawerItem.MY_EVENTS) {
            toolbar.setTitle(R.string.my_events);
            viewModel.getMyEvents(currentUser).observe(this,eventObserver);
        } else if (selectedItem ==DrawerItem.SETTINGS) {
            viewModel.getUpcomingEvents().removeObservers(this);
            viewModel.getMyEvents(currentUser).removeObservers(this);
            toolbar.setTitle(R.string.action_settings);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        navigationView.setCheckedItem(menuItem);
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
                sharedPreferences.edit().putString(Constants.EMAIL,"").apply();
                navigateToLoginActivity();
                break;
            case R.id.List:
                viewModel.getMyEvents(currentUser).removeObservers(this);
                viewModel.getUpcomingEvents().observe(this,eventObserver);
                selectedItem = DrawerItem.LIST_EVENTS;
                toolbar.setTitle(R.string.upcoming_events);
                break;
            case R.id.myEvents:
                viewModel.getUpcomingEvents().removeObservers(this);
                viewModel.getMyEvents(currentUser).observe(this,eventObserver);
                selectedItem = DrawerItem.MY_EVENTS;
                toolbar.setTitle(R.string.my_events);
                break;
            case R.id.settings:
                selectedItem = DrawerItem.SETTINGS;
                viewModel.getUpcomingEvents().removeObservers(this);
                viewModel.getMyEvents(currentUser).removeObservers(this);
                toolbar.setTitle(R.string.action_settings);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startCreateNewActivity();
                break;
        }
    }


    private void navigateToLoginActivity() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        this.finish();
    }

    private void startCreateNewActivity() {
        Intent i = new Intent(this,AddEventActivity.class);
        startActivity(i);
    }

    @Override
    public void itemClicked(Event event) {
        Intent i;
        if (event.getCreatedBy().equals(currentUser)) {
             i = new Intent(this, AddEventActivity.class);
        }
        else {
            i = new Intent(this,EventDetailActivity.class);
        }
        startActivity(i);
        EventBus.getDefault().postSticky(event);
    }
}
