package com.hasgeek.zalebi.fragments.space;

/**
 * Created by karthikbalakrishnan on 25/03/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.contactexchange.ContactScannedEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerFragment extends Fragment implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    private Bus mBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        ArrayList<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODE128);
        list.add(BarcodeFormat.QRCODE);
        mScannerView.setFormats(list);
        mBus= BusProvider.getInstance();
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
    public void handleResult(Result rawResult) {
        mBus.post(new ContactScannedEvent(rawResult.getContents()));
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
        mScannerView.stopCamera();
    }
}
