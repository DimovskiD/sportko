package com.dimovski.sportko.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dimovski.sportko.R;
import com.dimovski.sportko.db.model.Event;

import java.util.List;

public class EventAdapter extends
        RecyclerView.Adapter<EventAdapter.ViewHolder> {

    List<Event> events;

    public EventAdapter(List<Event> eventList){
        events = eventList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_list_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder viewHolder, int position) {

        Event e = events.get(position);
        viewHolder.title.setText(e.getTitle());
        viewHolder.description.setText(e.getDescription());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return events.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public TextView description;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.eventTitle);
            description = (TextView) itemView.findViewById(R.id.eventDescription);
        }
    }
}