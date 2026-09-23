package com.google.android.gms.maps.model;

import android.os.Parcel;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class g {
    static void a(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, polygonOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, polygonOptions.getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, polygonOptions.cO(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, polygonOptions.getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, polygonOptions.getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, polygonOptions.getFillColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, polygonOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, polygonOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, polygonOptions.isGeodesic());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }
}
