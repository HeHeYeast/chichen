package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class y implements Parcelable.Creator<x> {
    static void a(x xVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, xVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, xVar.ew, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, xVar.height);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, xVar.heightPixels);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, xVar.ex);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, xVar.width);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, xVar.widthPixels);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public x createFromParcel(Parcel parcel) {
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String strL = null;
        int iF2 = 0;
        boolean zC = false;
        int iF3 = 0;
        int iF4 = 0;
        int iF5 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF5 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    iF4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 4:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 6:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 7:
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
        return new x(iF5, strL, iF4, iF3, zC, iF2, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public x[] newArray(int i) {
        return new x[i];
    }
}
