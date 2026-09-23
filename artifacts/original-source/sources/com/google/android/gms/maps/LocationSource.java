package com.google.android.gms.maps;

import android.location.Location;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface LocationSource {

    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }

    void activate(OnLocationChangedListener onLocationChangedListener);

    void deactivate();
}
