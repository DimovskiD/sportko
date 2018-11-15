package com.dimovski.sportko.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.dimovski.sportko.R;
import com.dimovski.sportko.adapter.EventAdapter;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.viewmodel.EventViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.List;

public class ListActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Unbinder unbinder;

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

        EventViewModel model = ViewModelProviders.of(this).get(EventViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("events");
        collectionReference.get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Event>events =task.getResult().toObjects(Event.class);
                Log.i("LIST", events.get(0).getTitle());
                EventAdapter adapter = new EventAdapter(events);
                recyclerView.setAdapter(adapter);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("LIST",e.getMessage());
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( @Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("LIST",e.getMessage());
                    return;
                }

                if (snapshot != null) {
                    List<Event>events =snapshot.toObjects(Event.class);
                    Log.i("LIST", events.get(0).getTitle());
                    EventAdapter adapter = new EventAdapter(events);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("LIST", "Current data: null");
                }
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    private void navigateToLoginActivity() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startCreateNewActivity();
                break;
        }
    }

    private void startCreateNewActivity() {
        Intent i = new Intent(this,AddEventActivity.class);
        startActivity(i);
    }
}
