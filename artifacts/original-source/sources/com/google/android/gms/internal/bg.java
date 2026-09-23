package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class bg implements Parcelable.Creator<bh> {
    static void a(bh bhVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bhVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) bhVar.fR, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, bhVar.U(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, bhVar.V(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, bhVar.W(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, bhVar.X(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, bhVar.fW, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, bhVar.fX);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, bhVar.fY, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, bhVar.Y(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 11, bhVar.orientation);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 12, bhVar.ga);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 13, bhVar.fz, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, (Parcelable) bhVar.eg, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public bh createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        be beVar = null;
        IBinder iBinderM = null;
        IBinder iBinderM2 = null;
        IBinder iBinderM3 = null;
        IBinder iBinderM4 = null;
        String strL = null;
        boolean zC = false;
        String strL2 = null;
        IBinder iBinderM5 = null;
        int iF2 = 0;
        int iF3 = 0;
        String strL3 = null;
        co coVar = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    beVar = (be) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, be.CREATOR);
                    break;
                case 3:
                    iBinderM = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i);
                    break;
                case 4:
                    iBinderM2 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i);
                    break;
                case 5:
                    iBinderM3 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i);
                    break;
                case 6:
                    iBinderM4 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i);
                    break;
                case 7:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 8:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 9:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 10:
                    iBinderM5 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i);
                    break;
                case 11:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 12:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 13:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 14:
                    coVar = (co) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, co.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new bh(iF, beVar, iBinderM, iBinderM2, iBinderM3, iBinderM4, strL, zC, strL2, iBinderM5, iF2, iF3, strL3, coVar);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public bh[] newArray(int i) {
        return new bh[i];
    }
}
