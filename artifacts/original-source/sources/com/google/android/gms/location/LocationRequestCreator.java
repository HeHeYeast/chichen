package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class LocationRequestCreator implements Parcelable.Creator<LocationRequest> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(LocationRequest locationRequest, Parcel parcel, int i) {
        int iK = b.k(parcel);
        b.c(parcel, 1, locationRequest.mPriority);
        b.c(parcel, 1000, locationRequest.getVersionCode());
        b.a(parcel, 2, locationRequest.oJ);
        b.a(parcel, 3, locationRequest.oK);
        b.a(parcel, 4, locationRequest.oL);
        b.a(parcel, 5, locationRequest.oC);
        b.c(parcel, 6, locationRequest.oM);
        b.a(parcel, 7, locationRequest.oN);
        b.C(parcel, iK);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LocationRequest createFromParcel(Parcel parcel) {
        boolean zC = false;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        long jG = 3600000;
        long jG2 = 600000;
        long jG3 = Long.MAX_VALUE;
        int iF2 = Integer.MAX_VALUE;
        float fI = BitmapDescriptorFactory.HUE_RED;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 3:
                    jG2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 4:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 5:
                    jG3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 6:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 7:
                    fI = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 1000:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new LocationRequest(iF3, iF, jG, jG2, zC, jG3, iF2, fI);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LocationRequest[] newArray(int size) {
        return new LocationRequest[size];
    }
}
