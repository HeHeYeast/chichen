package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class bx implements Parcelable.Creator<bw> {
    static void a(bw bwVar, Parcel parcel, int i) {
        int iK = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bwVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, bwVar.fW, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, bwVar.gG, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, bwVar.eW, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, bwVar.errorCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, bwVar.eX, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, bwVar.gH);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, bwVar.gI);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, bwVar.gJ);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, bwVar.gK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, bwVar.fa);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 12, bwVar.orientation);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, iK);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public bw createFromParcel(Parcel parcel) {
        int iJ = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int iF = 0;
        String strL = null;
        String strL2 = null;
        ArrayList<String> arrayListX = null;
        int iF2 = 0;
        ArrayList<String> arrayListX2 = null;
        long jG = 0;
        boolean zC = false;
        long jG2 = 0;
        ArrayList<String> arrayListX3 = null;
        long jG3 = 0;
        int iF3 = 0;
        while (parcel.dataPosition() < iJ) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i)) {
                case 1:
                    iF = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 2:
                    strL = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 3:
                    strL2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i);
                    break;
                case 4:
                    arrayListX = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i);
                    break;
                case 5:
                    iF2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                case 6:
                    arrayListX2 = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i);
                    break;
                case 7:
                    jG = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 8:
                    zC = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i);
                    break;
                case 9:
                    jG2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 10:
                    arrayListX3 = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i);
                    break;
                case 11:
                    jG3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i);
                    break;
                case 12:
                    iF3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i);
                    break;
            }
        }
        if (parcel.dataPosition() != iJ) {
            throw new a.C0004a("Overread allowed size end=" + iJ, parcel);
        }
        return new bw(iF, strL, strL2, arrayListX, iF2, arrayListX2, jG, zC, jG2, arrayListX3, jG3, iF3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public bw[] newArray(int i) {
        return new bw[i];
    }
}
