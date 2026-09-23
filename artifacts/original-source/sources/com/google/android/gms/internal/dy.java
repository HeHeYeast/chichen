package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dw;
import com.google.android.gms.internal.dz;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class dy implements Parcelable.Creator<dz.b> {
    static void a(dz.b bVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, bVar.lN, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) bVar.lO, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: D, reason: merged with bridge method [inline-methods] */
    public dz.b[] newArray(int i) {
        return new dz.b[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public dz.b createFromParcel(Parcel parcel) {
        dw.a aVar = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    aVar = (dw.a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, dw.a.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new dz.b(iF, strL, aVar);
    }
}
