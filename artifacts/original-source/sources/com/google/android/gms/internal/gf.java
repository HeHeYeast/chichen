package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class gf implements Parcelable.Creator<fv.h> {
    static void a(fv.h hVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = hVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, hVar.getVersionCode());
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, hVar.dV());
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, hVar.getValue(), true);
        }
        if (setDi.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, hVar.getLabel(), true);
        }
        if (setDi.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, hVar.getType());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: M, reason: merged with bridge method [inline-methods] */
    public fv.h createFromParcel(Parcel parcel) {
        String strL = null;
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int iF2 = 0;
        String strL2 = null;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
                case 3:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(3);
                    break;
                case 4:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(4);
                    break;
                case 5:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(5);
                    break;
                case 6:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(6);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fv.h(hashSet, iF3, strL2, iF2, strL, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ar, reason: merged with bridge method [inline-methods] */
    public fv.h[] newArray(int i) {
        return new fv.h[i];
    }
}
