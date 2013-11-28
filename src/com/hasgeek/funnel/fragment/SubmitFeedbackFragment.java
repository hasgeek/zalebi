package com.hasgeek.funnel.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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


    public SubmitFeedbackFragment() {
        // required for DialogFragment
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedbackUrl = getArguments().getString("url");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.dialog_submitfeedback);
        View v = inflater.inflate(R.layout.fragment_dialog_submitfeedback, container, false);

        Button submit = (Button) v.findViewById(R.id.btn_dialog_feedback_submit);
        submit.setOnClickListener(submitButtonClickListener);

        return v;
    }


    private View.OnClickListener submitButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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

                String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                Intent i = new Intent(getActivity(), APIService.class);
                i.putExtra(APIService.MODE, APIService.POST_FEEDBACK);
                i.putExtra("url", mFeedbackUrl);
                i.putExtra("userid", android_id);
                i.putExtra("content", String.valueOf(contentVote));
                i.putExtra("presentation", String.valueOf(presentationVote));
                getActivity().startService(i);
                dismiss();
            }
        }
    };


}
