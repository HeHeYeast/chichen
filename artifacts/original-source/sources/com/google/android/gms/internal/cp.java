package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class cp implements Parcelable.Creator<co> {
    static void a(co coVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, coVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, coVar.hP, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, coVar.hQ);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, coVar.hR);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, coVar.hS);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public co createFromParcel(Parcel parcel) {
        boolean zC = false;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String strL = null;
        int iF = 0;
        int iF2 = 0;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 4:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new co(iF3, strL, iF2, iF, zC);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public co[] newArray(int i) {
        return new co[i];
    }
}
