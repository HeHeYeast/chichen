package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class gc implements Parcelable.Creator<fv.d> {
    static void a(fv.d dVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = dVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, dVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, dVar.getFamilyName(), true);
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, dVar.getFormatted(), true);
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, dVar.getGivenName(), true);
        }
        if (setDi.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, dVar.getHonorificPrefix(), true);
        }
        if (setDi.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, dVar.getHonorificSuffix(), true);
        }
        if (setDi.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, dVar.getMiddleName(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: J, reason: merged with bridge method [inline-methods] */
    public fv.d createFromParcel(Parcel parcel) {
        String strL = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int iF = 0;
        String strL2 = null;
        String strL3 = null;
        String strL4 = null;
        String strL5 = null;
        String strL6 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    strL6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(2);
                    break;
                case 3:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(3);
                    break;
                case 4:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(4);
                    break;
                case 5:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(5);
                    break;
                case 6:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(6);
                    break;
                case 7:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(7);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fv.d(hashSet, iF, strL6, strL5, strL4, strL3, strL2, strL);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ao, reason: merged with bridge method [inline-methods] */
    public fv.d[] newArray(int i) {
        return new fv.d[i];
    }
}
