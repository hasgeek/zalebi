package com.hasgeek.funnel.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hasgeek.funnel.R;
import com.hasgeek.funnel.service.APIService;


public class SubmitFeedbackFragment extends DialogFragment {

    private String mFeedbackUrl;
    private Button mSubmitButton;


    public SubmitFeedbackFragment() {
        // required for DialogFragment
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedbackUrl = getArguments().getString("url") + "/feedback";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.dialog_submitfeedback);
        View v = inflater.inflate(R.layout.fragment_dialog_submitfeedback, container, false);

        mSubmitButton = (Button) v.findViewById(R.id.btn_dialog_feedback_submit);
        mSubmitButton.setOnClickListener(submitButtonClickListener);

        return v;
    }


    private View.OnClickListener submitButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String[] googleAccount = new String[1];
            if (sp.contains("preferred_google_account")) {
                googleAccount[0] = sp.getString("preferred_google_account", "");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.select_google_account);
                AccountManager accountManager = AccountManager.get(getActivity());
                final Account[] accounts = accountManager.getAccountsByType("com.google");
                final int size = accounts.length;
                final String[] names = new String[size];
                for (int k = 0; k < size; k++) {
                    names[k] = accounts[k].name;
                }
                builder.setItems(names, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sp.edit().putString("preferred_google_account", names[which]).commit();
                        googleAccount[0] = names[which];
                    }
                });
                builder.create().show();
                return;
            }

            RadioGroup con = (RadioGroup) getView().findViewById(R.id.rg_dialog_feedback_content);
            RadioGroup pres = (RadioGroup) getView().findViewById(R.id.rg_dialog_feedback_presentation);

            if (con.getCheckedRadioButtonId() > 0 && pres.getCheckedRadioButtonId() > 0) {
                int contentVote, presentationVote;

                RadioButton conselected = (RadioButton) con.findViewById(con.getCheckedRadioButtonId());
                switch (conselected.getId()) {
                    case R.id.rb_content_ok:
                        contentVote = 0;
                        break;
                    case R.id.rb_content_good:
                        contentVote = 1;
                        break;
                    case R.id.rb_content_awesome:
                        contentVote = 2;
                        break;
                    default:
                        throw new RuntimeException("No radio button was selected!");
                }


                RadioButton presSelected = (RadioButton) pres.findViewById(pres.getCheckedRadioButtonId());
                switch (presSelected.getId()) {
                    case R.id.rb_presentation_ok:
                        presentationVote = 0;
                        break;
                    case R.id.rb_presentation_good:
                        presentationVote = 1;
                        break;
                    case R.id.rb_presentation_awesome:
                        presentationVote = 2;
                        break;
                    default:
                        throw new RuntimeException("No radio button was selected!");
                }

                Log.w("ASD!asdaSD", mFeedbackUrl + " " + googleAccount[0] + " " + contentVote + "/" + presentationVote);

                Intent i = new Intent(getActivity(), APIService.class);
                i.putExtra(APIService.MODE, APIService.POST_FEEDBACK);
                i.putExtra("url", mFeedbackUrl);
                i.putExtra("userid", googleAccount[0]);
                i.putExtra("content", String.valueOf(contentVote));
                i.putExtra("presentation", String.valueOf(presentationVote));
                getActivity().startService(i);
                dismiss();
            }
        }
    };


}
