package com.dimovski.sportko.adapter.event;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dimovski.sportko.R;

/**ViewHolder used by @{@link EventAdapter}*/
// Provide a direct reference to each of the views within a data item
// Used to cache the views within the item layout for fast access
public class EventViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public TextView title;
    public TextView date;
    public TextView time;
    public TextView location;
    public ImageView image;
    public CardView container;
    public TextView badge;
    public LinearLayout badgeContainer;
    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    protected EventViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);
        container =  itemView.findViewById(R.id.containerRvItem);
        title = (TextView) itemView.findViewById(R.id.eventTitle);
        date = (TextView) itemView.findViewById(R.id.eventDate);
        time = itemView.findViewById(R.id.eventTime);
        image = itemView.findViewById(R.id.event_photo);
        location = itemView.findViewById(R.id.location);
        badge = itemView.findViewById(R.id.badge);
        badgeContainer = itemView.findViewById(R.id.badgeContainer);
    }
}