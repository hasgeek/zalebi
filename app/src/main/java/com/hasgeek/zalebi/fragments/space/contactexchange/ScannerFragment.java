package com.hasgeek.zalebi.fragments.space.contactexchange;

/**
 * Created by karthikbalakrishnan on 25/03/15.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasgeek.zalebi.R;
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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ScannerFragment extends DialogFragment implements ZBarScannerView.ResultHandler {
    public ZBarScannerView mScannerView;

    private Bus mBus;

    private Space space;
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        space = Parcels.unwrap(getArguments().getParcelable("space"));
        mScannerView = new ZBarScannerView(getActivity());
        ArrayList<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODE128);
        list.add(BarcodeFormat.QRCODE);
        mScannerView.setFormats(list);
        mBus = BusProvider.getInstance();
        getDialog().setTitle("Scan Participant Badge");
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
        initScanner();
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERMISSION)
    private void initScanner() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_message_for_camera_permission_in_scanner),
                    REQUEST_CAMERA_PERMISSION, perms);
        }
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
        Log.i("handleResult()", "Raw Data:" + rawResult.getContents());
        final Attendee a = ContactExchangeService.getAttendeeFromScannedData(rawResult.getContents(), space.getId(), space.getUrl(), getActivity());
        if (a != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Add attendee?")
                    .setMessage("Name: " + a.getFullname() + "\nCompany: " + a.getCompany())
                    .setCancelable(false)
                    .setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContactExchangeService.addAttendeeToSyncQueue(a, space.getId(), space.getUrl());
                            resumeScannerView();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resumeScannerView();
                        }
                    })
                    .create().show();
        } else {
            resumeScannerView();
        }
    }

    private void resumeScannerView() {
        // Note from author:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScannerFragment.this);
            }
        }, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
        mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
