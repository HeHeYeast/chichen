package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class d {
    static void a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, latLngBounds.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) latLngBounds.southwest, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) latLngBounds.northeast, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }
}
