package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class e implements Parcelable.Creator<d> {
    static void a(d dVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, dVar.aK(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, dVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable[]) dVar.aL(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, dVar.getStatusCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, dVar.aM(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public d createFromParcel(Parcel parcel) {
        int iF = 0;
        Bundle bundleN = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        CursorWindow[] cursorWindowArr = null;
        String[] strArrW = null;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    strArrW = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i);
                    break;
                case 2:
                    cursorWindowArr = (CursorWindow[]) com.google.android.gms.common.internal.safeparcel.a.b(parcel, i, CursorWindow.CREATOR);
                    break;
                case 3:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 4:
                    bundleN = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i);
                    break;
                case 1000:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        d dVar = new d(iF2, strArrW, cursorWindowArr, iF, bundleN);
        dVar.aJ();
        return dVar;
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: s, reason: merged with bridge method [inline-methods] */
    public d[] newArray(int i) {
        return new d[i];
    }
}
