package com.dimovski.sportko.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.dimovski.sportko.R;
import com.dimovski.sportko.adapter.attendee.AttendeeListAdapter;

import java.util.ArrayList;
import java.util.List;

/**Dialog that shows the users attending the event*/
public class AttendeesDialog extends DialogFragment {


    public static AttendeesDialog newInstance(List<String> dataToShow) {
        AttendeesDialog frag = new AttendeesDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("usernames", (ArrayList<String>) dataToShow);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> usernames = getArguments().getStringArrayList("usernames");
        if (usernames==null) return null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.attendees_dialog, null);

        RecyclerView recyclerView =  view.findViewById(R.id.recyclerDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AttendeeListAdapter adapter = new AttendeeListAdapter(usernames);
        recyclerView.setAdapter(adapter);

        builder.setView(view);
        Dialog dialog = builder.create();


        return dialog;

    }
}