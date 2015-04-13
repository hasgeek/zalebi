package com.hasgeek.zalebi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.ContactExchangeService;
import com.hasgeek.zalebi.api.model.Attendee;
import com.hasgeek.zalebi.api.model.ExchangeContact;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncAttendeesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncContactsEvent;

import java.util.List;

/**
 * Created by karthik on 30-12-2014.
 */
public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ListItemViewHolder> {
    private final Context context;
    private final List<Attendee> attendees;

    public AttendeesAdapter(Context context, List<Attendee> attendees) {
        this.context = context;
        this.attendees = attendees;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_space_contactexchange_attendee_list_row,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        final Attendee a = attendees.get(position);
        viewHolder.name.setText(a.getFullname()+"");
        viewHolder.company.setText(a.getCompany()+"");
        viewHolder.mListener = new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                    .setTitle("Sync with server")
                    .setMessage("Name: "+a.getFullname()+"\nKey: "+a.getKey())
                    .setCancelable(false)
                    .setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContactExchangeService.addAttendeeToSyncQueue(a);
                        }
                    })
                    .setNegativeButton("No", null)
                    .create().show();

            }
        };

    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView company;
        public ViewHolderClick mListener;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.fragment_space_contactexchange_attendee_list_row_name);
            company = (TextView) itemView.findViewById(R.id.fragment_space_contactexchange_attendee_list_row_company);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        public static interface ViewHolderClick {
            public void onClick(View v);
        }
    }

}
