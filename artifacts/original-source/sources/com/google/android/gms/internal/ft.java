package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ft implements Parcelable.Creator<fs> {
    static void a(fs fsVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = fsVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fsVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fsVar.getId(), true);
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fsVar.dz(), i, true);
        }
        if (setDi.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fsVar.getStartDate(), true);
        }
        if (setDi.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) fsVar.dA(), i, true);
        }
        if (setDi.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fsVar.getType(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: C, reason: merged with bridge method [inline-methods] */
    public fs createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int iF = 0;
        fq fqVar = null;
        String strL2 = null;
        fq fqVar2 = null;
        String strL3 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(2);
                    break;
                case 3:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
                case 4:
                    fq fqVar3 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(4);
                    fqVar2 = fqVar3;
                    break;
                case 5:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(5);
                    break;
                case 6:
                    fq fqVar4 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(6);
                    fqVar = fqVar4;
                    break;
                case 7:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(7);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fs(hashSet, iF, strL3, fqVar2, strL2, fqVar, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ah, reason: merged with bridge method [inline-methods] */
    public fs[] newArray(int i) {
        return new fs[i];
    }
}
