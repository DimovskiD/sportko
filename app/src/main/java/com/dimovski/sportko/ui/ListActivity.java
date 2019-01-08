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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.adapter.event.EventAdapter;
import com.dimovski.sportko.adapter.ItemClickHandler;
import com.dimovski.sportko.auth.FirebaseAuthentication;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.auth.Authentication;
import com.dimovski.sportko.internal.DrawerItem;
import com.dimovski.sportko.utils.NetworkUtils;
import com.dimovski.sportko.viewmodel.EventViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**Shows a list of events that are interesting to the user*/
public class ListActivity extends BaseActivity implements  NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ItemClickHandler {

    Unbinder unbinder;
    EventViewModel viewModel;
    EventAdapter adapter;
    Observer<List<Event>> eventObserver;
    SharedPreferences sharedPreferences;
    String currentUser;
    String currentQuery="";
    Authentication auth;

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
    @BindView(R.id.searchView)
    SearchView searchView;

    private DrawerItem selectedItem = DrawerItem.LIST_EVENTS;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        unbinder = ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        currentUser = sharedPreferences.getString(Constants.EMAIL,"");
        TextView tv = navigationView.getHeaderView(0).findViewById(R.id.greeting);
        tv.setText(String.format("%s %s", getString(R.string.hello), currentUser));

        viewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        searchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));
        searchView.setPadding(0, 2, 0, 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        if (!NetworkUtils.checkInternetConnection(this)){
            showSnackbar();
        }
        setListeners();

        auth = FirebaseAuthentication.getInstance();
    }

    /**Updates the existing observers in accordance to which category the user has selected
     * */
    private void updateObservers() {
        try {
        if (selectedItem == DrawerItem.LIST_EVENTS){
            viewModel.getUpcomingEvents().observe(ListActivity.this, eventObserver);
            viewModel.getMyEvents(currentUser).removeObservers(ListActivity.this);
        }
        else {
            viewModel.getMyEvents(currentUser).observe(ListActivity.this,eventObserver);
            viewModel.getUpcomingEvents().removeObservers(ListActivity.this);
        } }
        catch (NullPointerException e) {
            Log.d("LIST",e.getMessage());
        }
    }

    private void showSnackbar() {
        Snackbar s  = Snackbar.make(drawerLayout,R.string.no_internet,BaseTransientBottomBar.LENGTH_LONG);
        View v = s.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        s.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setObserver();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Constants.SEARCH_QUERY,currentQuery);
        outState.putSerializable(Constants.SELECTED_ITEMS,selectedItem);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
         selectedItem = (DrawerItem) savedInstanceState.getSerializable(Constants.SELECTED_ITEMS);
         if (selectedItem == DrawerItem.LIST_EVENTS) {
             navigationView.getMenu().getItem(0).setChecked(true);
             navigationView.getMenu().getItem(1).setChecked(false);
         }
        else if (selectedItem == DrawerItem.MY_EVENTS) {
             navigationView.getMenu().getItem(1).setChecked(true);
             navigationView.getMenu().getItem(0).setChecked(false);
         }
        setObserver();
        if (currentQuery!=null && !currentQuery.equals("")){
            searchView.setQuery(currentQuery,false);
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

    /**Handles side navigation
     * @param menuItem - the item of the side navigation that has been clicked
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        checkItem(menuItem);
        switch (menuItem.getItemId()) {
            case R.id.logOut:
                auth.signOut();
                clearPreferences();
                startLoginActivity();
                break;
            case R.id.List:
                selectedItem = DrawerItem.LIST_EVENTS;
                updateObservers();
                toolbar.setTitle(R.string.upcoming_events);
                currentQuery = "";
                searchView.setIconified(true);
                break;
            case R.id.myEvents:
                selectedItem = DrawerItem.MY_EVENTS;
                toolbar.setTitle(R.string.my_events);
                currentQuery = "";
                searchView.setIconified(true);
                updateObservers();
                break;
            case R.id.settings:
                startSettingsActivity();
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    private void checkItem(MenuItem menuItem) {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        navigationView.setCheckedItem(menuItem);
        if (menuItem.getItemId()!=R.id.settings)
            menuItem.setChecked(true);
        else {
            if (selectedItem == DrawerItem.LIST_EVENTS) navigationView.getMenu().getItem(0).setChecked(true);
            else if (selectedItem == DrawerItem.MY_EVENTS) navigationView.getMenu().getItem(1).setChecked(true);
        }
    }

    private void clearPreferences() {
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.USER,"");
        editor.apply();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startCreateNewActivity();
                break;
        }
    }


    /**Called when there was a click on some of the events on the list
     * @param event - event that was clicked on
     * Starts @{@link AddEventActivity} or @{@link EventDetailActivity}, depending on the fact if the user has created the event in question, or not
     * If the user is the creator, we are opening the @{@link AddEventActivity}, which will be in UPDATE mode because we post the event in question on the EventBus, which is handled in @{@link AddEventActivity}
     * Else, we open the @{@link EventDetailActivity} and post the event on the event bus, so the @{@link EventDetailActivity} can fill the UI with the details of the event*/
    @Override
    public void eventClicked(Event event) {
        Intent i;
        if (event.getCreatedBy().equals(currentUser)) i = new Intent(this, AddEventActivity.class);
        else i = new Intent(this, EventDetailActivity.class);
        startActivity(i);
        EventBus.getDefault().postSticky(event);
    }

    /**Sets up listeners*/
    private void setListeners() {
        fab.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter!=null && adapter.getFilter()!=null)
                    adapter.getFilter().filter(query);
                currentQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter!=null && adapter.getFilter()!=null)
                    adapter.getFilter().filter(newText);
                currentQuery = newText;

                return false;
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateObservers();
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);

            }
        });
    }

    /**Sets up observers an actions on observer update*/
    private void setObserver() {
        eventObserver = new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (progressBar.getVisibility()==View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    if (events!=null && events.size()<1) {
                        emptyRv.setVisibility(View.VISIBLE);
                    } else emptyRv.setVisibility(View.GONE);
                } else {
                    if (events!=null && events.size()<1) {
                        emptyRv.setVisibility(View.VISIBLE);
                    } else emptyRv.setVisibility(View.GONE);
                }
                if (adapter!=null)
                    adapter.setEvents(events);
                else {
                    adapter = new EventAdapter(events,ListActivity.this);
                    recyclerView.setAdapter(adapter);

                }
                eventList = events;
                if (currentQuery!=null && !currentQuery.equals("")) {
                    searchView.setQuery(currentQuery, false);
                    adapter.getFilter().filter(currentQuery);
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
}
