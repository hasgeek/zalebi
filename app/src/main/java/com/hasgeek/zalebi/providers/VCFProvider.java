package com.hasgeek.zalebi.providers;

import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;

import java.io.FileNotFoundException;

/**
 * Created by karthikbalakrishnan on 08/04/15.
 */
public class VCFProvider extends FileProvider {
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }
}
