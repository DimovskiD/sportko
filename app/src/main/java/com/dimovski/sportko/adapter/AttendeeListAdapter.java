package com.dimovski.sportko.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dimovski.sportko.R;
import java.util.List;

public class AttendeeListAdapter extends RecyclerView.Adapter<AttendeeListAdapter.ViewHolder> {

    private List<String> usernames;

    public AttendeeListAdapter(List<String> userList){
        usernames = userList;
    }

    @NonNull
    @Override
    public AttendeeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.attendee_list_item, parent, false);
        return new AttendeeListAdapter.ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.username.setText(usernames.get(position));
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView username;
        public TextView description;
        public ImageView image;


        protected ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.profile_photo);
        }

    }
}