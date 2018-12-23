package com.dimovski.sportko.adapter.event;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.dimovski.sportko.R;
import com.dimovski.sportko.adapter.ItemClickHandler;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventAdapter extends
        RecyclerView.Adapter<EventViewHolder> implements Filterable {

    private List<Event> events;
    private List<Event> filteredEvents;
    private ViewGroup parent;
    private ItemClickHandler handler;

    public EventAdapter(List<Event> eventList, ItemClickHandler handler){
        this.handler=handler;
        filteredEvents = eventList;
        this.events = eventList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.parent = parent;
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_list_item, parent, false);

        // Return a new holder instance
        return new EventViewHolder(contactView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder viewHolder, int position) {

        final Event e = filteredEvents.get(position);
        viewHolder.title.setText(e.getTitle());
        viewHolder.date.setText(DateTimeUtils.formatDate(e.getScheduled(),parent.getContext()));
        viewHolder.time.setText(DateTimeUtils.formatTime(e.getScheduled(),parent.getContext()));
        viewHolder.location.setText(e.getLocationName());
        Glide.with(parent.getContext()).load(e.getImgSrc()).into(viewHolder.image);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.eventClicked(e);
            }
        });
        viewHolder.badgeContainer.setVisibility(View.GONE);
        if (e.getAttendees().size()==e.getMaxAttendees()) {
            viewHolder.badge.setText(R.string.full);
            viewHolder.badge.setTextColor(parent.getContext().getResources().getColor(android.R.color.darker_gray));
            viewHolder.badgeContainer.setBackground(parent.getContext().getResources().getDrawable(R.drawable.custom_border_gray));
            viewHolder.badgeContainer.setVisibility(View.VISIBLE);
        }
        if (e.getScheduled().before(Calendar.getInstance().getTime())) {
            viewHolder.badge.setText(R.string.finished);
            viewHolder.badge.setTextColor(parent.getContext().getResources().getColor(android.R.color.holo_red_dark));
            viewHolder.badgeContainer.setBackground(parent.getContext().getResources().getDrawable(R.drawable.custom_border_red));
            viewHolder.badgeContainer.setVisibility(View.VISIBLE);
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return filteredEvents.size();
    }

    public void setEvents(List<Event> events) {
        this.filteredEvents = events;
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.length() == 0) {

                    filteredEvents = events;
                } else {

                    ArrayList<Event> newEvents = new ArrayList<>();
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Event event : events) {
                        if (event.getTitle().toLowerCase().contains(filterPattern) || event.getTitle().contains(filterPattern) || event.getLocationName().toLowerCase().trim().contains(filterPattern)) {

                            newEvents.add(event);
                        }
                    }
                    filteredEvents = newEvents;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredEvents;


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredEvents = (ArrayList<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}