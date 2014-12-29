package com.hasgeek.zalebi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.model.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karthik on 24-12-2014.
 */
public class SpacesAdapter extends RecyclerView.Adapter<SpacesAdapter.ListItemViewHolder> {
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

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        Space s = spaces.get(position);
        viewHolder.name.setText(s.getTitle());
        String color = "#2a2a2a";
        if(s.getBgColor()!=null)
            if(!s.getBgColor().equals(""))
                color="#"+s.getBgColor();

        viewHolder.background.setBackgroundColor((Color.parseColor(color)));

    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView background;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.spaces_listview_row_name);
            background = (ImageView) itemView.findViewById(R.id.spaces_listview_row_image);
        }
    }
}
