package com.dimovski.sportko.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dimovski.sportko.R;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.utils.DateTimeUtils;

import java.util.List;

public class EventAdapter extends
        RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;
    private ViewGroup parent;

    public EventAdapter(List<Event> eventList){
        events = eventList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
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
        viewHolder.date.setText(DateTimeUtils.formatDate(e.getScheduled(),parent.getContext()));
        viewHolder.time.setText(DateTimeUtils.formatTime(e.getScheduled(),parent.getContext()));
        viewHolder.location.setText(e.getLocationName());
        Glide.with(parent.getContext()).load(e.getImgSrc()).into(viewHolder.image);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public TextView date;
        public TextView time;
        public TextView location;
        public ImageView image;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.eventTitle);
            date = (TextView) itemView.findViewById(R.id.eventDate);
            time = itemView.findViewById(R.id.eventTime);
            image = itemView.findViewById(R.id.event_photo);
            location = itemView.findViewById(R.id.location);
        }
    }
}