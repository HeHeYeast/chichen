package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class bd implements Parcelable.Creator<be> {
    static void a(be beVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, beVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, beVar.fy, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, beVar.fz, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, beVar.mimeType, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, beVar.packageName, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, beVar.fA, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, beVar.fB, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, beVar.fC, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public be createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL2 = null;
        String strL3 = null;
        String strL4 = null;
        String strL5 = null;
        String strL6 = null;
        String strL7 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
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
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new be(iF, strL7, strL6, strL5, strL4, strL3, strL2, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public be[] newArray(int i) {
        return new be[i];
    }
}
