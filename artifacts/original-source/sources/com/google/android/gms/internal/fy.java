package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class fy implements Parcelable.Creator<fv.b> {
    static void a(fv.b bVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = bVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) bVar.dM(), i, true);
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) bVar.dN(), i, true);
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, bVar.getLayout());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public fv.b createFromParcel(Parcel parcel) {
        fv.b.C0035b c0035b = null;
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        fv.b.a aVar = null;
        int iF2 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    fv.b.a aVar2 = (fv.b.a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fv.b.a.CREATOR);
                    hashSet.add(2);
                    aVar = aVar2;
                    break;
                case 3:
                    fv.b.C0035b c0035b2 = (fv.b.C0035b) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fv.b.C0035b.CREATOR);
                    hashSet.add(3);
                    c0035b = c0035b2;
                    break;
                case 4:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fv.b(hashSet, iF2, aVar, c0035b, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ak, reason: merged with bridge method [inline-methods] */
    public fv.b[] newArray(int i) {
        return new fv.b[i];
    }
}
