package com.dimovski.sportko.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.adapter.EventAdapter;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.viewmodel.EventViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Unbinder unbinder;
    EventViewModel viewModel;
    EventAdapter adapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (adapter!=null)
                    adapter.setEvents(events);
                else {
                    adapter = new EventAdapter(events);
                    recyclerView.setAdapter(adapter);
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
                navigateToLoginActivity();
                break;
            case R.id.List:
                break;
            case R.id.myEvents:
                break;
            case R.id.settings:
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
}
