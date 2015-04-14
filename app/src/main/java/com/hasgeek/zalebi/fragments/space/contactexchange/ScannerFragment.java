package com.hasgeek.zalebi.fragments.space.contactexchange;

/**
 * Created by karthikbalakrishnan on 25/03/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hasgeek.zalebi.activity.SingleSpaceActivity;
import com.hasgeek.zalebi.api.ContactExchangeService;
import com.hasgeek.zalebi.api.model.Attendee;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.squareup.otto.Bus;

import org.parceler.Parcels;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerFragment extends DialogFragment implements ZBarScannerView.ResultHandler {
    public ZBarScannerView mScannerView;

    private Bus mBus;

    private Space space;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        space = Parcels.unwrap(getArguments().getParcelable("space"));
        mScannerView = new ZBarScannerView(getActivity());
        ArrayList<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODE128);
        list.add(BarcodeFormat.QRCODE);
        mScannerView.setFormats(list);
        mBus= BusProvider.getInstance();
        getDialog().setTitle("Scan Participant Badge");
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof SingleSpaceActivity) {
            ((SingleSpaceActivity) activity).updatePages();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.i("handleResult()", "Raw Data:"+rawResult.getContents());
        mScannerView.stopCamera();
        final Attendee a = ContactExchangeService.getAttendeeFromScannedData(rawResult.getContents(), space.getJsonUrl(), getActivity());
        if(a != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Add attendee?")
                    .setMessage("Name: "+a.getFullname()+"\nCompany: "+a.getCompany())
                    .setCancelable(false)
                    .setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContactExchangeService.addAttendeeToSyncQueue(a);
                            mScannerView.startCamera();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mScannerView.startCamera();
                        }
                    })
                    .create().show();
        }
        else {
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
        mScannerView.stopCamera();
    }
}
