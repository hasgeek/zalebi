package com.hasgeek.zalebi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.ContactExchangeService;
import com.hasgeek.zalebi.api.model.ExchangeContact;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncContactsEvent;

import java.util.List;

/**
 * Created by karthik on 30-12-2014.
 */
public class ExchangeContactsAdapter extends RecyclerView.Adapter<ExchangeContactsAdapter.ListItemViewHolder> {
    private final Context context;
    private final List<ExchangeContact> contacts;

    public ExchangeContactsAdapter(Context context, List<ExchangeContact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_space_contactexchange_contact_list_row,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, final int position) {
        final ExchangeContact c = contacts.get(position);
        viewHolder.name.setText(c.getFullname()+"");
        viewHolder.email.setText(c.getEmail() + "");
//        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                contacts.remove(position);
//                ContactExchangeService.deleteExchangeContact(c);
//                notifyDataSetChanged();
//            }
//        });
        viewHolder.mListener = new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(c.getFullname()+"")
                        .setMessage("Company: "+c.getCompany()+"\nPhone: "+c.getPhone()+"\nEmail: "+c.getEmail())
                        .setCancelable(true)
                        .setPositiveButton("Add to phone", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_INSERT);
                                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                                intent.putExtra(ContactsContract.Intents.Insert.NAME, c.getFullname());
                                intent.putExtra(ContactsContract.Intents.Insert.PHONE, c.getPhone());
                                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, c.getCompany());
                                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, c.getEmail());
                                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, c.getJobTitle());
                                context.startActivity(intent);
                            }
                        })
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Confirm")
                                        .setMessage("Are you sure you want to delete this contact?")
                                        .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                contacts.remove(position);
                                                ContactExchangeService.deleteExchangeContact(c);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .create()
                                        .show();
                            }
                        })
                        .setNegativeButton("Ok", null)
                        .create().show();
            }
        };

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public List<ExchangeContact> getContacts() {
        return contacts;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView email;
        public Button delete;
        public ViewHolderClick mListener;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.fragment_space_contactexchange_contact_list_row_name);
            email = (TextView) itemView.findViewById(R.id.fragment_space_contactexchange_contact_list_row_email);
            delete = (Button) itemView.findViewById(R.id.fragment_space_contactexchange_contact_list_row_btn_delete);
            delete.setVisibility(View.GONE);
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
