package com.hasgeek.zalebi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.model.Section;

import java.util.List;

/**
 * Created by karthik on 30-12-2014.
 */
public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ListItemViewHolder> {
    private final Context context;
    private final List<Section> sections;

    public SectionsAdapter(Context context, List<Section> sections) {
        this.context = context;
        this.sections = sections;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_space_section_list_row,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        Section s = sections.get(position);
        viewHolder.title.setText(s.getTitle());
        viewHolder.decription.setText(s.getDescription());

    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView decription;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.fragment_space_section_list_row_title);
            decription = (TextView) itemView.findViewById(R.id.fragment_space_section_list_row_description);
        }
    }

}
