package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class LatLngBoundsCreator implements Parcelable.Creator<LatLngBounds> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, latLngBounds.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) latLngBounds.southwest, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) latLngBounds.northeast, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LatLngBounds createFromParcel(Parcel parcel) {
        LatLng latLng;
        LatLng latLng2;
        int iF;
        LatLng latLng3 = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        LatLng latLng4 = null;
        while (parcel.dataPosition() < iJ) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    LatLng latLng5 = latLng3;
                    latLng2 = latLng4;
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    latLng = latLng5;
                    break;
                case 2:
                    LatLng latLng6 = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, LatLng.CREATOR);
                    iF = i;
                    latLng = latLng3;
                    latLng2 = latLng6;
                    break;
                case 3:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, LatLng.CREATOR);
                    latLng2 = latLng4;
                    iF = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    latLng = latLng3;
                    latLng2 = latLng4;
                    iF = i;
                    break;
            }
            i = iF;
            latLng4 = latLng2;
            latLng3 = latLng;
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new LatLngBounds(i, latLng4, latLng3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LatLngBounds[] newArray(int size) {
        return new LatLngBounds[size];
    }
}
