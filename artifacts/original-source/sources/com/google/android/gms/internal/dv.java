package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dt;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class dv implements Parcelable.Creator<dt.a> {
    static void a(dt.a aVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, aVar.lx, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, aVar.ly);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public dt.a[] newArray(int i) {
        return new dt.a[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: n, reason: merged with bridge method [inline-methods] */
    public dt.a createFromParcel(Parcel parcel) {
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String strL = null;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
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
        return new dt.a(iF2, strL, iF);
    }
}
