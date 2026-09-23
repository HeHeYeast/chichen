package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CircleOptionsCreator implements Parcelable.Creator<CircleOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(CircleOptions circleOptions, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, circleOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) circleOptions.getCenter(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, circleOptions.getRadius());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, circleOptions.getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, circleOptions.getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, circleOptions.getFillColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, circleOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, circleOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CircleOptions createFromParcel(Parcel parcel) {
        float fI = BitmapDescriptorFactory.HUE_RED;
        boolean zC = false;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        LatLng latLng = null;
        double dJ = 0.0d;
        int iF = 0;
        int iF2 = 0;
        float fI2 = 0.0f;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    dJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i);
                    break;
                case 4:
                    fI2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 5:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 6:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 7:
                    fI = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 8:
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
        return new CircleOptions(iF3, latLng, dJ, fI2, iF2, iF, fI, zC);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CircleOptions[] newArray(int size) {
        return new CircleOptions[size];
    }
}
