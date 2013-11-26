package com.hasgeek.funnel.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hasgeek.funnel.R;


public class SessionDetailRowPart extends LinearLayout {

    public SessionDetailRowPart(Context context, String _title, String _speaker, String _track, int _roomcolor, String _roomtitle) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.row_session_bottom_part, this, true);

        TextView title = (TextView) v.findViewById(R.id.tv_session_bottom_title);
        title.setText(_title);

        TextView speaker = (TextView) v.findViewById(R.id.tv_session_bottom_speaker);
        if (_speaker.equalsIgnoreCase("null")) {
            speaker.setVisibility(View.GONE);
        } else {
            speaker.setText(_speaker);
            speaker.setVisibility(View.VISIBLE);
        }

        TextView track = (TextView) v.findViewById(R.id.tv_session_bottom_track);
        if (_track.equalsIgnoreCase("null")) {
            track.setVisibility(View.GONE);
        } else {
            track.setText(_track);
            track.setVisibility(View.VISIBLE);
        }

        View roomcolor = v.findViewById(R.id.iv_session_bottom_roomcolor);
        roomcolor.setBackgroundColor(_roomcolor);

        TextView roomtitle = (TextView) v.findViewById(R.id.tv_session_bottom_roomtitle);
        roomtitle.setText(_roomtitle);

        Button bookmark = (Button) v.findViewById(R.id.btn_session_bottom_bookmark);
    }

}
