package com.hasgeek.zalebi.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.activity.ProposalActivity;
import com.hasgeek.zalebi.api.model.Proposal;

import java.util.List;

/**
 * Created by karthik on 30-12-2014.
 */
public class ProposalsAdapter extends RecyclerView.Adapter<ProposalsAdapter.ListItemViewHolder> {
    private final Context context;
    private final List<Proposal> proposals;
    private final Bundle spaceBundle;

    public ProposalsAdapter(Context context, List<Proposal> proposals, Bundle space) {
        this.context = context;
        this.proposals = proposals;
        this.spaceBundle = space;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_space_proposal_list_row,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, final int position) {
        final Proposal p = proposals.get(position);
        viewHolder.title.setText(p.getTitle());
        viewHolder.fullname.setText(p.getFullname());
        viewHolder.mListener = new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProposalActivity.class);
                i.putExtra("proposal_index", position);
                i.putExtra("bundle", spaceBundle);
                context.startActivity(i);
            }
        };

    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView fullname;
        public ViewHolderClick mListener;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.fragment_space_proposal_list_row_title);
            fullname = (TextView) itemView.findViewById(R.id.fragment_space_proposal_list_row_fullname);
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
