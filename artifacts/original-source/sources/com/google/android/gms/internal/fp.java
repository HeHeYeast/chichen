package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class fp implements Parcelable.Creator<fn> {
    static void a(fn fnVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, fnVar.getAccountName(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, fnVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fnVar.cZ(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fnVar.da(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, fnVar.db(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fnVar.dc(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, fnVar.dd(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fnVar.de(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fnVar.df(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public fn createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL2 = null;
        String strL3 = null;
        String strL4 = null;
        String[] strArrW = null;
        String[] strArrW2 = null;
        String[] strArrW3 = null;
        String strL5 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 2:
                    strArrW3 = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i);
                    break;
                case 3:
                    strArrW2 = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i);
                    break;
                case 4:
                    strArrW = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i);
                    break;
                case 5:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 6:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 7:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 8:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
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
        return new fn(iF, strL5, strArrW3, strArrW2, strArrW, strL4, strL3, strL2, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: af, reason: merged with bridge method [inline-methods] */
    public fn[] newArray(int i) {
        return new fn[i];
    }
}
