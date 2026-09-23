package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class k {
    static void a(VisibleRegion visibleRegion, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, visibleRegion.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) visibleRegion.nearLeft, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) visibleRegion.nearRight, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) visibleRegion.farLeft, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, (Parcelable) visibleRegion.farRight, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) visibleRegion.latLngBounds, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }
}
