package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class PolylineOptionsCreator implements Parcelable.Creator<PolylineOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, polylineOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, polylineOptions.getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, polylineOptions.getWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, polylineOptions.getColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, polylineOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, polylineOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, polylineOptions.isGeodesic());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolylineOptions createFromParcel(Parcel parcel) {
        float fI = BitmapDescriptorFactory.HUE_RED;
        boolean zC = false;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        ArrayList arrayListC = null;
        boolean zC2 = false;
        int iF = 0;
        float fI2 = 0.0f;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    fI2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 4:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    fI = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 6:
                    zC2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 7:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new PolylineOptions(iF2, arrayListC, fI2, iF, fI, zC2, zC);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolylineOptions[] newArray(int size) {
        return new PolylineOptions[size];
    }
}
