package com.hasgeek.zalebi.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.activity.SingleSpaceActivity;
import com.hasgeek.zalebi.api.model.Space;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by karthik on 24-12-2014.
 */
public class SpacesAdapter extends RecyclerView.Adapter<SpacesAdapter.ListItemViewHolder> {
    private final String LOG_TAG = "SpacesAdapter";
    private final Context context;
    private final List<Space> spaces;

    public SpacesAdapter(Context context, List<Space> spaces) {
        this.context = context;
        this.spaces = spaces;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.spaces_listview_row,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView, new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        final Space s = spaces.get(position);
        viewHolder.name.setText(s.getTitle());
        String color = "#2a2a2a";
        if(s.getBgColor()!=null)
            if(!s.getBgColor().equals(""))
                color="#"+s.getBgColor();

        viewHolder.background.setBackgroundColor((Color.parseColor(color)));
        viewHolder.mListener = new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pg = new ProgressDialog(context);
                pg.setTitle("Loading");
                pg.setCancelable(false);
                pg.show();
                Intent i = new Intent(context, SingleSpaceActivity.class);
                Bundle extras = new Bundle();
                extras.setClassLoader(Thread.currentThread().getContextClassLoader());
                extras.putParcelable("space", Parcels.wrap(s));
                i.putExtra("bundle", extras);
                context.startActivity(i);
                if(pg.isShowing())
                    pg.dismiss();
            }
        };

    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public ImageView background;
        public ViewHolderClick mListener;

        public ListItemViewHolder(View itemView, ViewHolderClick listener) {
            super(itemView);
            mListener = listener;
            name = (TextView)itemView.findViewById(R.id.spaces_listview_row_name);
            background = (ImageView) itemView.findViewById(R.id.spaces_listview_row_image);
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
