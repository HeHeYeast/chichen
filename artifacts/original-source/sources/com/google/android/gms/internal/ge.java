package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ge implements Parcelable.Creator<fv.g> {
    static void a(fv.g gVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = gVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, gVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, gVar.isPrimary());
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, gVar.getValue(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: L, reason: merged with bridge method [inline-methods] */
    public fv.g createFromParcel(Parcel parcel) {
        boolean zC = false;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        String strL = null;
        int iF = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    hashSet.add(2);
                    break;
                case 3:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
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
        return new fv.g(hashSet, iF, zC, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aq, reason: merged with bridge method [inline-methods] */
    public fv.g[] newArray(int i) {
        return new fv.g[i];
    }
}
