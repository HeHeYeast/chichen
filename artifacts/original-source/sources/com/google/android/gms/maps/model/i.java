package com.google.android.gms.maps.model;

import android.os.Parcel;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class i {
    static void a(Tile tile, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, tile.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, tile.width);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, tile.height);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, tile.data, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }
}
