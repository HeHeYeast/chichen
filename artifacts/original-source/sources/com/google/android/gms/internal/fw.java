package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.TimeUtils;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class fw implements Parcelable.Creator<fv> {
    static void a(fv fvVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = fvVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fvVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fvVar.getAboutMe(), true);
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) fvVar.dD(), i, true);
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, fvVar.getBirthday(), true);
        }
        if (setDi.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fvVar.getBraggingRights(), true);
        }
        if (setDi.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, fvVar.getCircledByCount());
        }
        if (setDi.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) fvVar.dE(), i, true);
        }
        if (setDi.contains(8)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fvVar.getCurrentLocation(), true);
        }
        if (setDi.contains(9)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, fvVar.getDisplayName(), true);
        }
        if (setDi.contains(12)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 12, fvVar.getGender());
        }
        if (setDi.contains(14)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, fvVar.getId(), true);
        }
        if (setDi.contains(15)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 15, (Parcelable) fvVar.dF(), i, true);
        }
        if (setDi.contains(16)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 16, fvVar.isPlusUser());
        }
        if (setDi.contains(19)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 19, (Parcelable) fvVar.dG(), i, true);
        }
        if (setDi.contains(18)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 18, fvVar.getLanguage(), true);
        }
        if (setDi.contains(21)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 21, fvVar.getObjectType());
        }
        if (setDi.contains(20)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 20, fvVar.getNickname(), true);
        }
        if (setDi.contains(23)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 23, fvVar.dI(), true);
        }
        if (setDi.contains(22)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 22, fvVar.dH(), true);
        }
        if (setDi.contains(25)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 25, fvVar.getRelationshipStatus());
        }
        if (setDi.contains(24)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 24, fvVar.getPlusOneCount());
        }
        if (setDi.contains(27)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 27, fvVar.getUrl(), true);
        }
        if (setDi.contains(26)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 26, fvVar.getTagline(), true);
        }
        if (setDi.contains(29)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 29, fvVar.isVerified());
        }
        if (setDi.contains(28)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 28, fvVar.dJ(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: D, reason: merged with bridge method [inline-methods] */
    public fv createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int iF = 0;
        String strL = null;
        fv.a aVar = null;
        String strL2 = null;
        String strL3 = null;
        int iF2 = 0;
        fv.b bVar = null;
        String strL4 = null;
        String strL5 = null;
        int iF3 = 0;
        String strL6 = null;
        fv.c cVar = null;
        boolean zC = false;
        String strL7 = null;
        fv.d dVar = null;
        String strL8 = null;
        int iF4 = 0;
        ArrayList arrayListC = null;
        ArrayList arrayListC2 = null;
        int iF5 = 0;
        int iF6 = 0;
        String strL9 = null;
        String strL10 = null;
        ArrayList arrayListC3 = null;
        boolean zC2 = false;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(2);
                    break;
                case 3:
                    fv.a aVar2 = (fv.a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fv.a.CREATOR);
                    hashSet.add(3);
                    aVar = aVar2;
                    break;
                case 4:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(4);
                    break;
                case 5:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(5);
                    break;
                case 6:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(6);
                    break;
                case 7:
                    fv.b bVar2 = (fv.b) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fv.b.CREATOR);
                    hashSet.add(7);
                    bVar = bVar2;
                    break;
                case 8:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(8);
                    break;
                case 9:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(9);
                    break;
                case 10:
                case 11:
                case 13:
                case 17:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
                case 12:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(12);
                    break;
                case 14:
                    strL6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(14);
                    break;
                case 15:
                    fv.c cVar2 = (fv.c) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fv.c.CREATOR);
                    hashSet.add(15);
                    cVar = cVar2;
                    break;
                case 16:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    hashSet.add(16);
                    break;
                case 18:
                    strL7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(18);
                    break;
                case TimeUtils.HUNDRED_DAY_FIELD_LEN /* 19 */:
                    fv.d dVar2 = (fv.d) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fv.d.CREATOR);
                    hashSet.add(19);
                    dVar = dVar2;
                    break;
                case 20:
                    strL8 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(20);
                    break;
                case 21:
                    iF4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(21);
                    break;
                case 22:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fv.f.CREATOR);
                    hashSet.add(22);
                    break;
                case 23:
                    arrayListC2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fv.g.CREATOR);
                    hashSet.add(23);
                    break;
                case 24:
                    iF5 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(24);
                    break;
                case 25:
                    iF6 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(25);
                    break;
                case 26:
                    strL9 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(26);
                    break;
                case 27:
                    strL10 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(27);
                    break;
                case 28:
                    arrayListC3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fv.h.CREATOR);
                    hashSet.add(28);
                    break;
                case 29:
                    zC2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    hashSet.add(29);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fv(hashSet, iF, strL, aVar, strL2, strL3, iF2, bVar, strL4, strL5, iF3, strL6, cVar, zC, strL7, dVar, strL8, iF4, arrayListC, arrayListC2, iF5, iF6, strL9, strL10, arrayListC3, zC2);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ai, reason: merged with bridge method [inline-methods] */
    public fv[] newArray(int i) {
        return new fv[i];
    }
}
