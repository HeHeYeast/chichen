package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.TimeUtils;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class fr implements Parcelable.Creator<fq> {
    static void a(fq fqVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> setDi = fqVar.di();
        if (setDi.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fqVar.getVersionCode());
        }
        if (setDi.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) fqVar.dj(), i, true);
        }
        if (setDi.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fqVar.getAdditionalName(), true);
        }
        if (setDi.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fqVar.dk(), i, true);
        }
        if (setDi.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fqVar.getAddressCountry(), true);
        }
        if (setDi.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, fqVar.getAddressLocality(), true);
        }
        if (setDi.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fqVar.getAddressRegion(), true);
        }
        if (setDi.contains(8)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 8, fqVar.dl(), true);
        }
        if (setDi.contains(9)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 9, fqVar.getAttendeeCount());
        }
        if (setDi.contains(10)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 10, fqVar.dm(), true);
        }
        if (setDi.contains(11)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, (Parcelable) fqVar.dn(), i, true);
        }
        if (setDi.contains(12)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 12, fqVar.m1do(), true);
        }
        if (setDi.contains(13)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 13, fqVar.getBestRating(), true);
        }
        if (setDi.contains(14)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, fqVar.getBirthDate(), true);
        }
        if (setDi.contains(15)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 15, (Parcelable) fqVar.dp(), i, true);
        }
        if (setDi.contains(17)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 17, fqVar.getContentSize(), true);
        }
        if (setDi.contains(16)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 16, fqVar.getCaption(), true);
        }
        if (setDi.contains(19)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 19, fqVar.dq(), true);
        }
        if (setDi.contains(18)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 18, fqVar.getContentUrl(), true);
        }
        if (setDi.contains(21)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 21, fqVar.getDateModified(), true);
        }
        if (setDi.contains(20)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 20, fqVar.getDateCreated(), true);
        }
        if (setDi.contains(23)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 23, fqVar.getDescription(), true);
        }
        if (setDi.contains(22)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 22, fqVar.getDatePublished(), true);
        }
        if (setDi.contains(25)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 25, fqVar.getEmbedUrl(), true);
        }
        if (setDi.contains(24)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 24, fqVar.getDuration(), true);
        }
        if (setDi.contains(27)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 27, fqVar.getFamilyName(), true);
        }
        if (setDi.contains(26)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 26, fqVar.getEndDate(), true);
        }
        if (setDi.contains(29)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 29, (Parcelable) fqVar.dr(), i, true);
        }
        if (setDi.contains(28)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 28, fqVar.getGender(), true);
        }
        if (setDi.contains(31)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 31, fqVar.getHeight(), true);
        }
        if (setDi.contains(30)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 30, fqVar.getGivenName(), true);
        }
        if (setDi.contains(34)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 34, (Parcelable) fqVar.ds(), i, true);
        }
        if (setDi.contains(32)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 32, fqVar.getId(), true);
        }
        if (setDi.contains(33)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 33, fqVar.getImage(), true);
        }
        if (setDi.contains(38)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 38, fqVar.getLongitude());
        }
        if (setDi.contains(39)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 39, fqVar.getName(), true);
        }
        if (setDi.contains(36)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 36, fqVar.getLatitude());
        }
        if (setDi.contains(37)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 37, (Parcelable) fqVar.dt(), i, true);
        }
        if (setDi.contains(42)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 42, fqVar.getPlayerType(), true);
        }
        if (setDi.contains(43)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 43, fqVar.getPostOfficeBoxNumber(), true);
        }
        if (setDi.contains(40)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 40, (Parcelable) fqVar.du(), i, true);
        }
        if (setDi.contains(41)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 41, fqVar.dv(), true);
        }
        if (setDi.contains(46)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 46, (Parcelable) fqVar.dw(), i, true);
        }
        if (setDi.contains(47)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 47, fqVar.getStartDate(), true);
        }
        if (setDi.contains(44)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 44, fqVar.getPostalCode(), true);
        }
        if (setDi.contains(45)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 45, fqVar.getRatingValue(), true);
        }
        if (setDi.contains(51)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 51, fqVar.getThumbnailUrl(), true);
        }
        if (setDi.contains(50)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 50, (Parcelable) fqVar.dx(), i, true);
        }
        if (setDi.contains(49)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 49, fqVar.getText(), true);
        }
        if (setDi.contains(48)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 48, fqVar.getStreetAddress(), true);
        }
        if (setDi.contains(55)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 55, fqVar.getWidth(), true);
        }
        if (setDi.contains(54)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 54, fqVar.getUrl(), true);
        }
        if (setDi.contains(53)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 53, fqVar.getType(), true);
        }
        if (setDi.contains(52)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 52, fqVar.getTickerSymbol(), true);
        }
        if (setDi.contains(56)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 56, fqVar.getWorstRating(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public fq createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int iF = 0;
        fq fqVar = null;
        ArrayList<String> arrayListX = null;
        fq fqVar2 = null;
        String strL = null;
        String strL2 = null;
        String strL3 = null;
        ArrayList arrayListC = null;
        int iF2 = 0;
        ArrayList arrayListC2 = null;
        fq fqVar3 = null;
        ArrayList arrayListC3 = null;
        String strL4 = null;
        String strL5 = null;
        fq fqVar4 = null;
        String strL6 = null;
        String strL7 = null;
        String strL8 = null;
        ArrayList arrayListC4 = null;
        String strL9 = null;
        String strL10 = null;
        String strL11 = null;
        String strL12 = null;
        String strL13 = null;
        String strL14 = null;
        String strL15 = null;
        String strL16 = null;
        String strL17 = null;
        fq fqVar5 = null;
        String strL18 = null;
        String strL19 = null;
        String strL20 = null;
        String strL21 = null;
        fq fqVar6 = null;
        double dJ = 0.0d;
        fq fqVar7 = null;
        double dJ2 = 0.0d;
        String strL22 = null;
        fq fqVar8 = null;
        ArrayList arrayListC5 = null;
        String strL23 = null;
        String strL24 = null;
        String strL25 = null;
        String strL26 = null;
        fq fqVar9 = null;
        String strL27 = null;
        String strL28 = null;
        String strL29 = null;
        fq fqVar10 = null;
        String strL30 = null;
        String strL31 = null;
        String strL32 = null;
        String strL33 = null;
        String strL34 = null;
        String strL35 = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(1);
                    break;
                case 2:
                    fq fqVar11 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(2);
                    fqVar = fqVar11;
                    break;
                case 3:
                    arrayListX = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i);
                    hashSet.add(3);
                    break;
                case 4:
                    fq fqVar12 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(4);
                    fqVar2 = fqVar12;
                    break;
                case 5:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(5);
                    break;
                case 6:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(6);
                    break;
                case 7:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(7);
                    break;
                case 8:
                    arrayListC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fq.CREATOR);
                    hashSet.add(8);
                    break;
                case 9:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    hashSet.add(9);
                    break;
                case 10:
                    arrayListC2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fq.CREATOR);
                    hashSet.add(10);
                    break;
                case 11:
                    fq fqVar13 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(11);
                    fqVar3 = fqVar13;
                    break;
                case 12:
                    arrayListC3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fq.CREATOR);
                    hashSet.add(12);
                    break;
                case 13:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(13);
                    break;
                case 14:
                    strL5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(14);
                    break;
                case 15:
                    fq fqVar14 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(15);
                    fqVar4 = fqVar14;
                    break;
                case 16:
                    strL6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(16);
                    break;
                case 17:
                    strL7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(17);
                    break;
                case 18:
                    strL8 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(18);
                    break;
                case TimeUtils.HUNDRED_DAY_FIELD_LEN /* 19 */:
                    arrayListC4 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fq.CREATOR);
                    hashSet.add(19);
                    break;
                case 20:
                    strL9 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(20);
                    break;
                case 21:
                    strL10 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(21);
                    break;
                case 22:
                    strL11 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(22);
                    break;
                case 23:
                    strL12 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(23);
                    break;
                case 24:
                    strL13 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(24);
                    break;
                case 25:
                    strL14 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(25);
                    break;
                case 26:
                    strL15 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(26);
                    break;
                case 27:
                    strL16 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(27);
                    break;
                case 28:
                    strL17 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(28);
                    break;
                case 29:
                    fq fqVar15 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(29);
                    fqVar5 = fqVar15;
                    break;
                case 30:
                    strL18 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(30);
                    break;
                case 31:
                    strL19 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(31);
                    break;
                case 32:
                    strL20 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(32);
                    break;
                case 33:
                    strL21 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(33);
                    break;
                case 34:
                    fq fqVar16 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(34);
                    fqVar6 = fqVar16;
                    break;
                case 35:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
                case 36:
                    dJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i);
                    hashSet.add(36);
                    break;
                case 37:
                    fq fqVar17 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(37);
                    fqVar7 = fqVar17;
                    break;
                case 38:
                    dJ2 = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i);
                    hashSet.add(38);
                    break;
                case 39:
                    strL22 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(39);
                    break;
                case FluctConstants.FRAME_SIZE /* 40 */:
                    fq fqVar18 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(40);
                    fqVar8 = fqVar18;
                    break;
                case 41:
                    arrayListC5 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i, fq.CREATOR);
                    hashSet.add(41);
                    break;
                case 42:
                    strL23 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(42);
                    break;
                case 43:
                    strL24 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(43);
                    break;
                case 44:
                    strL25 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(44);
                    break;
                case 45:
                    strL26 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(45);
                    break;
                case 46:
                    fq fqVar19 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(46);
                    fqVar9 = fqVar19;
                    break;
                case 47:
                    strL27 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(47);
                    break;
                case 48:
                    strL28 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(48);
                    break;
                case 49:
                    strL29 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(49);
                    break;
                case 50:
                    fq fqVar20 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, fq.CREATOR);
                    hashSet.add(50);
                    fqVar10 = fqVar20;
                    break;
                case 51:
                    strL30 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(51);
                    break;
                case 52:
                    strL31 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(52);
                    break;
                case 53:
                    strL32 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(53);
                    break;
                case 54:
                    strL33 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(54);
                    break;
                case 55:
                    strL34 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(55);
                    break;
                case 56:
                    strL35 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    hashSet.add(56);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new fq(hashSet, iF, fqVar, arrayListX, fqVar2, strL, strL2, strL3, arrayListC, iF2, arrayListC2, fqVar3, arrayListC3, strL4, strL5, fqVar4, strL6, strL7, strL8, arrayListC4, strL9, strL10, strL11, strL12, strL13, strL14, strL15, strL16, strL17, fqVar5, strL18, strL19, strL20, strL21, fqVar6, dJ, fqVar7, dJ2, strL22, fqVar8, arrayListC5, strL23, strL24, strL25, strL26, fqVar9, strL27, strL28, strL29, fqVar10, strL30, strL31, strL32, strL33, strL34, strL35);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ag, reason: merged with bridge method [inline-methods] */
    public fq[] newArray(int i) {
        return new fq[i];
    }
}
