package com.dimovski.sportko.adapter.attendee;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimovski.sportko.R;
import java.util.List;

/**Used to show which users attend an event
 * Used by @{@link com.dimovski.sportko.ui.dialog.AttendeesDialog} */
public class AttendeeListAdapter extends RecyclerView.Adapter<AttendeeViewHolder> {

    private List<String> usernames;

    public AttendeeListAdapter(List<String> userList){
        usernames = userList;
    }

    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.attendee_list_item, parent, false);
        return new AttendeeViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder viewHolder, int position) {
        viewHolder.username.setText(usernames.get(position));
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }


}