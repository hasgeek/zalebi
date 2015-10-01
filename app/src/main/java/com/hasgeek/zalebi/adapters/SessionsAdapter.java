package com.hasgeek.zalebi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.ContactExchangeService;
import com.hasgeek.zalebi.api.model.Room;
import com.hasgeek.zalebi.api.model.Session;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ListItemViewHolder>{

    private final Context context;
    private final List<Session> sessions;

    public SessionsAdapter(Context context, List<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_space_schedule_list_row,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        final Session s = sessions.get(position);
        viewHolder.title.setText(s.getTitle());
        viewHolder.speaker.setText(s.getSpeaker());

        DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        final Calendar start = Calendar.getInstance();
        final Calendar end = Calendar.getInstance();

        try {
            start.setTime(m_ISO8601Local.parse(s.getStart()));
            end.setTime(m_ISO8601Local.parse(s.getEnd()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String duration = String.format("%d min", TimeUnit.MILLISECONDS.toMinutes(end.getTimeInMillis()-start.getTimeInMillis()));

        viewHolder.duration.setText(duration+"");

        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        sdfs.setTimeZone(TimeZone.getDefault());

        String session_time = sdfs.format(start.getTime()) + "\n" + sdfs.format(end.getTime());

        viewHolder.time.setText(session_time);


        viewHolder.mListener = new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle(s.getTitle())
                        .setMessage(Html.fromHtml(s.getDescription()))
                        .setCancelable(true)
                        .setPositiveButton("Ok",null)
                        .create().show();
            }
        };

        if(s.getIsBreak()) {
           viewHolder.colorIndicator.setBackgroundColor(Color.DKGRAY);
        }
        else {
            viewHolder.colorIndicator.setBackgroundColor(Color.LTGRAY);
        }

    }



    @Override
    public int getItemCount() {
        return sessions.size();
    }


    public final static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView speaker;
        public TextView time;
        public TextView duration;
        public LinearLayout colorIndicator;
        public ViewHolderClick mListener;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.fragment_space_schedule_list_row_title);
            speaker = (TextView) itemView.findViewById(R.id.fragment_space_schedule_list_row_speaker);
            time = (TextView) itemView.findViewById(R.id.fragment_space_schedule_list_row_time);
            duration = (TextView) itemView.findViewById(R.id.fragment_space_schedule_list_row_duration);
            colorIndicator = (LinearLayout) itemView.findViewById(R.id.fragment_space_schedule_list_row_color_indicator);
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
