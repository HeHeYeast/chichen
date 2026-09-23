package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class bv implements Parcelable.Creator<bu> {
    static void a(bu buVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, buVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, buVar.gA, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) buVar.gB, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) buVar.ed, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, buVar.adUnitId, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) buVar.applicationInfo, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) buVar.gC, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, buVar.gD, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, buVar.gE, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, buVar.gF, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, (Parcelable) buVar.eg, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public bu createFromParcel(Parcel parcel) {
        co coVar = null;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL = null;
        String strL2 = null;
        String strL3 = null;
        PackageInfo packageInfo = null;
        ApplicationInfo applicationInfo = null;
        String strL4 = null;
        x xVar = null;
        v vVar = null;
        Bundle bundleN = null;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    bundleN = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i);
                    break;
                case 3:
                    vVar = (v) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, v.CREATOR);
                    break;
                case 4:
                    xVar = (x) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, x.CREATOR);
                    break;
                case 5:
                    strL4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 6:
                    applicationInfo = (ApplicationInfo) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, ApplicationInfo.CREATOR);
                    break;
                case 7:
                    packageInfo = (PackageInfo) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, PackageInfo.CREATOR);
                    break;
                case 8:
                    strL3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 9:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 10:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 11:
                    coVar = (co) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i, co.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new bu(iF, bundleN, vVar, xVar, strL4, applicationInfo, packageInfo, strL3, strL2, strL, coVar);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public bu[] newArray(int i) {
        return new bu[i];
    }
}
