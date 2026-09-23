package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class w implements Parcelable.Creator<v> {
    static void a(v vVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, vVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, vVar.es);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, vVar.extras, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, vVar.et);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, vVar.eu, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, vVar.ev);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, vVar.tagForChildDirectedTreatment);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public v createFromParcel(Parcel parcel) {
        ArrayList<String> arrayListX = null;
        int iF = 0;
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        long jG = 0;
        boolean zC = false;
        int iF2 = 0;
        Bundle bundleN = null;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 3:
                    bundleN = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i);
                    break;
                case 4:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 5:
                    arrayListX = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i);
                    break;
                case 6:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 7:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new v(iF3, jG, bundleN, iF2, arrayListX, zC, iF);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public v[] newArray(int i) {
        return new v[i];
    }
}
