package com.jackpan.schoolintroductory;

import android.location.Location;

/**
 * Created by krzysztofjackowski on 24/09/15.
 */
// 只是 接口 用不太到
public interface OnLocationChangedListener {
    void onLocationChanged(Location currentLocation);
}
