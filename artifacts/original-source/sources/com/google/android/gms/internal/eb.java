package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dz;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class eb implements Parcelable.Creator<dz.a> {
    static void a(dz.a aVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, aVar.className, false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 3, aVar.lM, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public dz.a[] newArray(int i) {
        return new dz.a[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public dz.a createFromParcel(Parcel parcel) {
        ArrayList arrayListC = null;
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
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, dz.b.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new dz.a(iF, strL, arrayListC);
    }
}
