package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dw;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class dx implements Parcelable.Creator<dw.a> {
    static void a(dw.a aVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, aVar.bn());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, aVar.bt());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, aVar.bo());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, aVar.bu());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, aVar.bv(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, aVar.bw());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, aVar.by(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable) aVar.bA(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: C, reason: merged with bridge method [inline-methods] */
    public dw.a[] newArray(int i) {
        return new dw.a[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: o, reason: merged with bridge method [inline-methods] */
    public dw.a createFromParcel(Parcel parcel) {
        dr drVar = null;
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String strL = null;
        String strL2 = null;
        boolean zC = false;
        int iF2 = 0;
        boolean zC2 = false;
        int iF3 = 0;
        int iF4 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 3:
                    zC2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 4:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 6:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 7:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 8:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 9:
                    drVar = (dr) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, dr.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new dw.a(iF4, iF3, zC2, iF2, zC, strL2, iF, strL, drVar);
    }
}
