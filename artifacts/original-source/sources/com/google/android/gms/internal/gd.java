package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class gd implements Parcelable.Creator<fv.f> {
    static void a(fv.f fVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = fVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fVar.getDepartment(), true);
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fVar.getDescription(), true);
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, fVar.getEndDate(), true);
        }
        if (setDi.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fVar.getLocation(), true);
        }
        if (setDi.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, fVar.getName(), true);
        }
        if (setDi.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fVar.isPrimary());
        }
        if (setDi.contains(8)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fVar.getStartDate(), true);
        }
        if (setDi.contains(9)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, fVar.getTitle(), true);
        }
        if (setDi.contains(10)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 10, fVar.getType());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: K, reason: merged with bridge method [inline-methods] */
    public fv.f createFromParcel(Parcel parcel) {
        int iF = 0;
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        String strL2 = null;
        boolean zC = false;
        String strL3 = null;
        String strL4 = null;
        String strL5 = null;
        String strL6 = null;
        String strL7 = null;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    strL7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(2);
                    break;
                case 3:
                    strL6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(3);
                    break;
                case 4:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(4);
                    break;
                case 5:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(5);
                    break;
                case 6:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(6);
                    break;
                case 7:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    hashSet.add(7);
                    break;
                case 8:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(8);
                    break;
                case 9:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(9);
                    break;
                case 10:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(10);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fv.f(hashSet, iF2, strL7, strL6, strL5, strL4, strL3, zC, strL2, strL, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ap, reason: merged with bridge method [inline-methods] */
    public fv.f[] newArray(int i) {
        return new fv.f[i];
    }
}
