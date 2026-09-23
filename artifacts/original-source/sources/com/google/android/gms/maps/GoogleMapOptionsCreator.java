package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.CameraPosition;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GoogleMapOptionsCreator implements Parcelable.Creator<GoogleMapOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int iK = b.k(parcel);
        b.c(parcel, 1, googleMapOptions.getVersionCode());
        b.a(parcel, 2, googleMapOptions.cv());
        b.a(parcel, 3, googleMapOptions.cw());
        b.c(parcel, 4, googleMapOptions.getMapType());
        b.a(parcel, 5, (Parcelable) googleMapOptions.getCamera(), i, false);
        b.a(parcel, 6, googleMapOptions.cx());
        b.a(parcel, 7, googleMapOptions.cy());
        b.a(parcel, 8, googleMapOptions.cz());
        b.a(parcel, 9, googleMapOptions.cA());
        b.a(parcel, 10, googleMapOptions.cB());
        b.a(parcel, 11, googleMapOptions.cC());
        b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public GoogleMapOptions createFromParcel(Parcel parcel) {
        byte bD = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        CameraPosition cameraPosition = null;
        byte bD2 = 0;
        byte bD3 = 0;
        byte bD4 = 0;
        byte bD5 = 0;
        byte bD6 = 0;
        int iF = 0;
        byte bD7 = 0;
        byte bD8 = 0;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    bD8 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 3:
                    bD7 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 4:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    cameraPosition = (CameraPosition) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, CameraPosition.CREATOR);
                    break;
                case 6:
                    bD6 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 7:
                    bD5 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 8:
                    bD4 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 9:
                    bD3 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 10:
                    bD2 = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                case 11:
                    bD = com.google.android.gms.common.internal.safeparcel.a.d(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new GoogleMapOptions(iF2, bD8, bD7, iF, cameraPosition, bD6, bD5, bD4, bD3, bD2, bD);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public GoogleMapOptions[] newArray(int size) {
        return new GoogleMapOptions[size];
    }
}
