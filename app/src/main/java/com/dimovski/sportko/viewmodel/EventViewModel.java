package com.dimovski.sportko.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.dimovski.sportko.db.model.Event;

import java.util.List;

public class EventViewModel extends ViewModel {

    private MutableLiveData<List<Event>> articles;
    public LiveData<List<Event>> getEvents() {
        if (articles == null) {
            articles = new MutableLiveData<List<Event>>();
            loadArticles();
        }
        return articles;
    }

    public void loadArticles() {
        // do async operation to fetch articles
    }

}
