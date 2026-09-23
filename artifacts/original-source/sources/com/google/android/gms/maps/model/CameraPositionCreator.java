package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CameraPositionCreator implements Parcelable.Creator<CameraPosition> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, cameraPosition.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) cameraPosition.target, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, cameraPosition.zoom);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, cameraPosition.tilt);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, cameraPosition.bearing);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CameraPosition createFromParcel(Parcel parcel) {
        float fI = BitmapDescriptorFactory.HUE_RED;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        LatLng latLng = null;
        float fI2 = 0.0f;
        float fI3 = 0.0f;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, LatLng.CREATOR);
                    break;
                case 3:
                    fI3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 4:
                    fI2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 5:
                    fI = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new CameraPosition(iF, latLng, fI3, fI2, fI);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CameraPosition[] newArray(int size) {
        return new CameraPosition[size];
    }
}
