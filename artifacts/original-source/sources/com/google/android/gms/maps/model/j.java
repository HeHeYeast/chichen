package com.google.android.gms.maps.model;

import android.os.Parcel;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class j {
    static void a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, tileOverlayOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, tileOverlayOptions.cP(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, tileOverlayOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, tileOverlayOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }
}
