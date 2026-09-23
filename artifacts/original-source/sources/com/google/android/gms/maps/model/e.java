package com.google.android.gms.maps.model;

import android.os.Parcel;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class e {
    static void a(LatLng latLng, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, latLng.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, latLng.latitude);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, latLng.longitude);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }
}
