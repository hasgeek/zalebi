package com.hasgeek.zalebi.fragments.space.proposal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.model.Proposal;
import com.hasgeek.zalebi.api.model.Space;

/**
 * Created by karthik on 30-12-2014.
 */
public class SingleProposalFragment extends Fragment {

    String LOG_TAG = "SingleProposalFragment";
    public Space space;
    public Proposal proposal;

    public static SingleProposalFragment newInstance(Space s, Proposal p) {
        SingleProposalFragment f = new SingleProposalFragment();

        f.proposal = p;
        f.space = s;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_proposal_proposal, container, false);

        TextView title = (TextView) v.findViewById(R.id.fragment_proposal_proposal_title);
        TextView fullname = (TextView) v.findViewById(R.id.fragment_proposal_proposal_fullname);
        WebView description = (WebView) v.findViewById(R.id.fragment_proposal_proposal_description_webview);

        title.setText(proposal.getTitle());
        fullname.setText(proposal.getFullname());

        description.loadData(proposal.getDescription(), "text/html; charset=UTF-8", null);
        description.setBackgroundColor(0x00000000);

        return v;
    }

}
