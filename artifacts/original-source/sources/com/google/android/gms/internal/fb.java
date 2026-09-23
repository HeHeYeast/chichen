package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class fb implements Parcelable.Creator<fa> {
    static void a(fa faVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, faVar.getRequestId(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, faVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, faVar.getExpirationTime());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, faVar.co());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, faVar.getLatitude());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, faVar.getLongitude());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, faVar.cp());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, faVar.cq());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 8, faVar.getNotificationResponsiveness());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 9, faVar.cr());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ac, reason: merged with bridge method [inline-methods] */
    public fa[] newArray(int i) {
        return new fa[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: z, reason: merged with bridge method [inline-methods] */
    public fa createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL = null;
        int iF2 = 0;
        short sE = 0;
        double dJ = 0.0d;
        double dJ2 = 0.0d;
        float fI = BitmapDescriptorFactory.HUE_RED;
        long jG = 0;
        int iF3 = 0;
        int iF4 = -1;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 2:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 3:
                    sE = com.google.android.gms.common.internal.safeparcel.a.e(parcel, i);
                    break;
                case 4:
                    dJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i);
                    break;
                case 5:
                    dJ2 = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i);
                    break;
                case 6:
                    fI = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i);
                    break;
                case 7:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 8:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 9:
                    iF4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 1000:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fa(iF, strL, iF2, sE, dJ, dJ2, fI, jG, iF3, iF4);
    }
}
