package com.dimovski.sportko.adapter.attendee;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dimovski.sportko.R;

public class AttendeeViewHolder extends RecyclerView.ViewHolder  {
    public TextView username;
    public TextView description;
    public ImageView image;


    protected AttendeeViewHolder(View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.username);
        description = itemView.findViewById(R.id.description);
        image = itemView.findViewById(R.id.profile_photo);
    }

}