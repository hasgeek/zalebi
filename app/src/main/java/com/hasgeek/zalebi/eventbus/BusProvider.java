package com.hasgeek.zalebi.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by karthik on 24-12-2014.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}