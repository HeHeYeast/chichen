package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dz;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ea implements Parcelable.Creator<dz> {
    static void a(dz dzVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, dzVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, dzVar.bE(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, dzVar.bF(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: E, reason: merged with bridge method [inline-methods] */
    public dz[] newArray(int i) {
        return new dz[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: q, reason: merged with bridge method [inline-methods] */
    public dz createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        ArrayList arrayListC = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, dz.a.CREATOR);
                    break;
                case 3:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new dz(iF, arrayListC, strL);
    }
}
