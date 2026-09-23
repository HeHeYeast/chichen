package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class fx implements Parcelable.Creator<fv.a> {
    static void a(fv.a aVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = aVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, aVar.getMax());
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, aVar.getMin());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: E, reason: merged with bridge method [inline-methods] */
    public fv.a createFromParcel(Parcel parcel) {
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int iF2 = 0;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(2);
                    break;
                case 3:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fv.a(hashSet, iF3, iF2, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aj, reason: merged with bridge method [inline-methods] */
    public fv.a[] newArray(int i) {
        return new fv.a[i];
    }
}
